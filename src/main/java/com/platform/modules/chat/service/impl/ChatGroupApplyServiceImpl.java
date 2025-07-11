package com.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupApplyDao;
import com.platform.modules.chat.domain.ChatFriendApply;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupApply;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.GroupSourceEnum;
import com.platform.modules.chat.service.ChatGroupApplyService;
import com.platform.modules.chat.service.ChatGroupMemberService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.GroupVo25;
import com.platform.modules.push.enums.PushBadgerEnum;
import com.platform.modules.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 群组申请表 服务层实现
 * </p>
 */
@Service("chatGroupApplyService")
public class ChatGroupApplyServiceImpl extends BaseServiceImpl<ChatGroupApply> implements ChatGroupApplyService {

    @Resource
    private ChatGroupApplyDao chatGroupApplyDao;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private PushService pushService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupApplyDao);
    }

    @Override
    public List<ChatGroupApply> queryList(ChatGroupApply t) {
        List<ChatGroupApply> dataList = chatGroupApplyDao.queryList(t);
        return dataList;
    }

    @Override
    public void apply(ChatGroup chatGroup, List<Long> userList, GroupSourceEnum source) {
        // 判空
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Long groupId = chatGroup.getGroupId();
        String groupName = chatGroup.getGroupName();
        // 查询管理员
        List<Long> managerList = chatGroupMemberService.getGroupAdmin(groupId);
        // 查询用户
        List<ChatUser> chatUserList = chatUserService.getByIds(userList);
        Date now = DateUtil.date();
        List<ChatGroupApply> dataList = new ArrayList<>();
        for (ChatUser chatUser : chatUserList) {
            Long userId = chatUser.getUserId();
            // 查询申请
            ChatGroupApply query = new ChatGroupApply()
                    .setUserId(userId)
                    .setGroupId(groupId)
                    .setApplyStatus(ApproveEnum.APPLY);
            if (this.queryOne(query) != null) {
                break;
            }
            // 新增申请
            for (Long manager : managerList) {
                ChatGroupApply applyGroup = new ChatGroupApply()
                        .setGroupId(groupId)
                        .setGroupName(groupName)
                        .setUserId(userId)
                        .setNickname(chatUser.getNickname())
                        .setPortrait(chatUser.getPortrait())
                        .setReceiveId(manager)
                        .setApplyStatus(ApproveEnum.APPLY)
                        .setApplySource(source)
                        .setCreateTime(now);
                dataList.add(applyGroup);
            }
        }
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 批量新增
        this.batchAdd(dataList);
        // 批量推送
        managerList.forEach(receiveId -> {
            pushService.pushBadger(receiveId, PushBadgerEnum.GROUP, userList);
        });
    }

    @Override
    public PageInfo queryDataList() {
        Long current = ShiroUtils.getUserId();
        // 清空角标
        redisUtils.delete(StrUtil.format(PushBadgerEnum.GROUP.getType(), current));
        pushService.pushBadger(current, PushBadgerEnum.GROUP, null);
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase("createTime desc"));
        // 查询
        QueryWrapper<ChatGroupApply> wrapper = new QueryWrapper();
        wrapper.eq(ChatGroupApply.COLUMN_RECEIVE_ID, current);
        wrapper.gt(ChatFriendApply.COLUMN_CREATE_TIME, DateUtil.lastMonth());
        List<ChatGroupApply> dataList = this.queryList(wrapper);
        // 转换数据
        List<GroupVo25> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(BeanUtil.toBean(y, GroupVo25.class).setStatus(y.getApplyStatus()).setRemark(y.getApplySourceLabel()));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public void ignore(Long applyId) {
        // 校验申请
        ChatGroupApply apply = verifyApply(applyId);
        if (apply == null) {
            return;
        }
        dealApply(apply, ApproveEnum.REJECT);
    }

    @Override
    public void applyDelete(Long applyId) {
        ChatGroupApply apply = this.getById(applyId);
        if (apply == null) {
            return;
        }
        Long current = ShiroUtils.getUserId();
        if (!apply.getReceiveId().equals(current)) {
            return;
        }
        // 更新申请
        this.deleteById(applyId);
    }

    @Transactional
    @Override
    public void agree(Long applyId) {
        // 校验申请
        ChatGroupApply apply = verifyApply(applyId);
        if (apply == null) {
            return;
        }
        // 处理申请
        dealApply(apply, ApproveEnum.PASS);
        // 查询群组
        ChatGroup chatGroup = chatGroupService.getById(apply.getGroupId());
        if (chatGroup == null) {
            return;
        }
        // 查询用户
        ChatUser chatUser = chatUserService.getById(apply.getUserId());
        // 加入群聊
        chatGroupService.addGroup(chatGroup, chatUser, apply.getApplySource());
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<ChatGroupApply>lambdaUpdate()
                .set(ChatGroupApply::getNickname, nickname)
                .eq(ChatGroupApply::getUserId, current));
    }

    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<ChatGroupApply>lambdaUpdate()
                .set(ChatGroupApply::getPortrait, portrait)
                .eq(ChatGroupApply::getUserId, current));
    }

    @Override
    public void editGroupName(Long groupId, String groupName) {
        // 修改数据
        this.update(Wrappers.<ChatGroupApply>lambdaUpdate()
                .set(ChatGroupApply::getGroupName, groupName)
                .eq(ChatGroupApply::getGroupId, groupId));
    }

    /**
     * 验证申请
     */
    private ChatGroupApply verifyApply(Long applyId) {
        Long current = ShiroUtils.getUserId();
        ChatGroupApply apply = getById(applyId);
        if (apply == null) {
            return null;
        }
        if (!ApproveEnum.APPLY.equals(apply.getApplyStatus())) {
            return null;
        }
        Long groupId = apply.getGroupId();
        // 验证权限
        chatGroupMemberService.verifyGroupManager(groupId, current);
        return apply;
    }

    /**
     * 处理申请
     */
    private void dealApply(ChatGroupApply apply, ApproveEnum applyStatus) {
        this.update(Wrappers.<ChatGroupApply>lambdaUpdate()
                .set(ChatGroupApply::getApplyStatus, applyStatus)
                .eq(ChatGroupApply::getGroupId, apply.getGroupId())
                .eq(ChatGroupApply::getUserId, apply.getUserId()));
    }

}
