package com.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.filter.FilterUtils;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupDao;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.*;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.*;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushGroup;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.service.WalletTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组 服务层实现
 * </p>
 */
@Service("chatGroupService")
@CacheConfig(cacheNames = AppConstants.REDIS_CHAT_GROUP)
public class ChatGroupServiceImpl extends BaseServiceImpl<ChatGroup> implements ChatGroupService {

    @Resource
    private ChatGroupDao chatGroupDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatNumberService chatNumberService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private ChatGroupApplyService chatGroupApplyService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatRefreshService chatRefreshService;

    @Resource
    private ChatPortraitService chatPortraitService;

    @Resource
    private ChatGroupLogService chatGroupLogService;

    @Resource
    private ChatGroupLevelService chatGroupLevelService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private ChatResourceService chatResourceService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupDao);
    }

    @Override
    public List<ChatGroup> queryList(ChatGroup t) {
        List<ChatGroup> dataList = chatGroupDao.queryList(t);
        return dataList;
    }

    @Override
    @Cacheable(key = "#groupId", unless = "#result == null")
    public ChatGroup getById(Long groupId) {
        return super.getById(groupId);
    }

    @Override
    public Integer updateById(ChatGroup chatGroup) {
        Integer result = super.updateById(chatGroup);
        this.clearCache(chatGroup.getGroupId());
        return result;
    }

    private GroupVo39 searchNo(String groupNo) {
        // 按编号搜索
        ChatGroup chatGroup = this.queryOne(new ChatGroup().setGroupNo(groupNo));
        // 搜索结果
        return _search(chatGroup, GroupSourceEnum.GROUP_NO);
    }

    @Override
    public PageInfo searchGroup(String param) {
        // 搜索参数
        YesOrNoEnum search = chatConfigService.queryConfig(ChatConfigEnum.GROUP_NAME_SEARCH).getYesOrNo();
        // 编码搜索
        if (YesOrNoEnum.NO.equals(search)) {
            GroupVo39 data = this.searchNo(param);
            if (data == null) {
                throw new BaseException("暂无结果，对方可能开启了隐私保护");
            }
            return getPageInfo(Arrays.asList(data), 1);
        }
        // 名称+编码搜索
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        // 搜索
        List<ChatGroup> dataList = chatGroupDao.querySearch(ShiroUtils.getUserId(), param);
        // list转Obj
        List<GroupVo39> resultList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            GroupSourceEnum source = GroupSourceEnum.GROUP_NO;
            String groupNo = y.getGroupNo();
            if (!param.equalsIgnoreCase(y.getGroupNo())) {
                source = GroupSourceEnum.GROUP_NAME;
                groupNo = DesensitizedUtil.idCardNum(groupNo, 3, 3);
            }
            GroupVo39 data = BeanUtil.toBean(y, GroupVo39.class)
                    .setSource(source)
                    .setGroupNo(groupNo)
                    .setIsMember(YesOrNoEnum.transform(y.getCount().intValue() == 1));
            x.add(data);
        }, ArrayList::addAll);
        return getPageInfo(resultList, dataList);
    }

    @Override
    public GroupVo39 scan(Long groupId) {
        // 通过ID搜索
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        // 搜索结果
        return _search(chatGroup, GroupSourceEnum.SCAN);
    }

    /**
     * 搜索结果
     */
    private GroupVo39 _search(ChatGroup chatGroup, GroupSourceEnum source) {
        String errMsg = "暂无结果，群组可能开启了隐私保护";
        if (chatGroup == null) {
            throw new BaseException(errMsg);
        }
        if (GroupSourceEnum.GROUP_NO.equals(source)) {
            if (YesOrNoEnum.NO.equals(chatGroup.getPrivacyNo())) {
                throw new BaseException(errMsg);
            }
        }
        if (GroupSourceEnum.SCAN.equals(source)) {
            if (YesOrNoEnum.NO.equals(chatGroup.getPrivacyScan())) {
                throw new BaseException(errMsg);
            }
        }
        Long groupId = chatGroup.getGroupId();
        Long current = ShiroUtils.getUserId();
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        YesOrNoEnum isMember = YesOrNoEnum.transform(groupMember != null);
        return BeanUtil.toBean(chatGroup, GroupVo39.class)
                .setSource(source)
                .setIsMember(isMember);
    }

    @Transactional
    @Override
    public void create(GroupVo16 groupVo) {
        // 群聊名称
        String groupName = groupVo.getGroupName();
        if (StringUtils.isEmpty(groupName)) {
            groupName = StrUtil.format("{}的群聊-{}", ShiroUtils.getNickname(), RandomUtil.randomString(4));
        }
        // 名称过滤
        else {
            groupName = FilterUtils.filter(groupName, PlatformConfig.FILTER);
        }
        // 好友列表
        List<Long> friendList = groupVo.getFriendList();
        // 校验
        Integer levelCount = chatConfigService.queryConfig(ChatConfigEnum.GROUP_LEVEL_COUNT).getInt();
        if (friendList.size() > levelCount) {
            throw new BaseException("群组人数不能大于" + levelCount);
        }
        // 校验
        friendList = chatFriendService.verifyFriend(friendList);
        // 建立群组
        Date now = DateUtil.date();
        String portrait = chatPortraitService.queryGroupPortrait();
        String groupNo = chatNumberService.queryNextNo();
        ChatGroup chatGroup = new ChatGroup()
                .setGroupName(groupName)
                .setGroupNo(groupNo)
                .setPortrait(portrait)
                .setNoticeTop(YesOrNoEnum.NO)
                .setConfigMember(YesOrNoEnum.NO)
                .setConfigInvite(YesOrNoEnum.YES)
                .setConfigSpeak(YesOrNoEnum.NO)
                .setConfigTitle(YesOrNoEnum.YES)
                .setConfigAudit(YesOrNoEnum.NO)
                .setConfigMedia(YesOrNoEnum.YES)
                .setConfigAssign(YesOrNoEnum.NO)
                .setConfigNickname(YesOrNoEnum.YES)
                .setConfigPacket(YesOrNoEnum.YES)
                .setConfigAmount(YesOrNoEnum.YES)
                .setConfigScan(YesOrNoEnum.YES)
                .setConfigReceive(YesOrNoEnum.NO)
                .setGroupLevel(0)
                .setGroupLevelCount(levelCount)
                .setGroupLevelPrice(BigDecimal.ZERO)
                .setGroupLevelTime(now)
                .setPrivacyNo(YesOrNoEnum.YES)
                .setPrivacyScan(YesOrNoEnum.YES)
                .setPrivacyName(YesOrNoEnum.YES)
                .setCreateTime(now);
        this.add(chatGroup);
        Long groupId = chatGroup.getGroupId();
        // 群组明细
        List<ChatGroupMember> memberList = new ArrayList<>();
        // 群主
        memberList.add(new ChatGroupMember(groupId, ChatUser.current()).setMemberType(GroupMemberEnum.MASTER));
        // 成员
        List<ChatUser> userList = chatUserService.getByIds(friendList);
        for (ChatUser chatUser : userList) {
            memberList.add(new ChatGroupMember(groupId, chatUser));
        }
        // 批量添加
        chatGroupMemberService.batchAdd(memberList);
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CREATE, groupName);
        // 准备推送
        PushGroup pushGroup = chatGroup.getPushGroup();
        PushFrom pushFrom = chatGroup.getPushFrom();
        Long masterId = ShiroUtils.getUserId();
        String nickname = ShiroUtils.getNickname();
        // 通知群主
        String content1 = StrUtil.format(AppConstants.TIPS_GROUP_CREATE_MASTER, groupName);
        pushService.pushGroup(pushFrom, pushGroup, Arrays.asList(masterId), content1, PushMsgTypeEnum.TIPS);
        // 通知组员
        String content2 = StrUtil.format(AppConstants.TIPS_GROUP_CREATE_MEMBER, nickname);
        pushService.pushGroup(pushFrom, pushGroup, friendList, content2, PushMsgTypeEnum.TIPS);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_CREATE, "", friendList, masterId);
    }

    @Override
    public List<GroupVo11> groupList() {
        Long current = ShiroUtils.getUserId();
        // 查询明细
        return chatGroupDao.groupList(current);
    }

    @Override
    public GroupVo11 getInfo(Long groupId) {
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验成员
        ChatGroupMember groupMember = chatGroupMemberService.verifyGroupMember(groupId, current);
        Integer memberSize = chatGroupMemberService.getMemberSize(groupId);
        return BeanUtil.toBean(chatGroup, GroupVo11.class)
                .setConfigScan(chatGroup.getConfigScan() == null ? YesOrNoEnum.YES : chatGroup.getConfigScan())
                .setMemberType(groupMember.getMemberType())
                .setMemberRemark(groupMember.getRemark())
                .setMemberTop(groupMember.getTop())
                .setMemberDisturb(groupMember.getDisturb())
                .setMemberSpeak(groupMember.getSpeak())
                .setMemberWhite(groupMember.getPacketWhite())
                .setMemberSize(NumberUtil.toStr(memberSize))
                .setMemberTotal(NumberUtil.toStr(chatGroup.getGroupLevelCount()));
    }

    @Transactional
    @Override
    public void join(GroupVo23 groupVo) {
        Long groupId = groupVo.getGroupId();
        GroupSourceEnum source = groupVo.getSource();
        // 查询
        String errMsg = "群不存在或群开启了隐私保护";
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            throw new BaseException(errMsg);
        }
        Long current = ShiroUtils.getUserId();
        // 查询成员
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (groupMember != null) {
            return;
        }
        if (GroupSourceEnum.GROUP_NO.equals(source)) {
            if (YesOrNoEnum.NO.equals(chatGroup.getPrivacyNo())) {
                throw new BaseException(errMsg);
            }
        } else if (GroupSourceEnum.SCAN.equals(source)) {
            if (YesOrNoEnum.NO.equals(chatGroup.getPrivacyScan())) {
                throw new BaseException(errMsg);
            }
        } else if (GroupSourceEnum.GROUP_NAME.equals(source)) {
            if (YesOrNoEnum.NO.equals(chatGroup.getPrivacyName())) {
                throw new BaseException(errMsg);
            }
        } else {
            throw new BaseException(errMsg);
        }
        // 设置计数
        Long increment = getIncrement();
        Integer applyCount = chatConfigService.queryConfig(ChatConfigEnum.APPLY_GROUP).getInt();
        if (increment > applyCount) {
            throw new BaseException("已超过群组申请上限，请明日再试");
        }
        // 审核开关
        if (YesOrNoEnum.YES.equals(chatGroup.getConfigAudit())) {
            chatGroupApplyService.apply(chatGroup, Arrays.asList(current), source);
        }
        // 加入群聊
        else {
            this.addGroup(chatGroup, ChatUser.current(), source);
        }
    }

    /**
     * 计数器
     */
    private Long getIncrement() {
        Date now = DateUtil.date();
        Long current = ShiroUtils.getUserId();
        String time = DateUtil.format(now, DatePattern.PURE_DATE_PATTERN);
        String redisKey = StrUtil.format(AppConstants.REDIS_APPLY_GROUP, time, current);
        return redisUtils.increment(redisKey, 1, 1, TimeUnit.DAYS);
    }

    /**
     * 加入群聊
     */
    @Override
    public void addGroup(ChatGroup chatGroup, ChatUser chatUser, GroupSourceEnum source) {
        Long groupId = chatGroup.getGroupId();
        Long userId = chatUser.getUserId();
        String nickname = chatUser.getNickname();
        // 查询群明细
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, userId);
        if (groupMember != null) {
            return;
        }
        // 成员数量
        Integer memberSize = chatGroupMemberService.getMemberSize(groupId);
        // 校验
        Integer levelCount = chatGroup.getGroupLevelCount();
        if (memberSize + 1 > levelCount) {
            throw new BaseException("群组人数不能大于" + levelCount);
        }
        // 新增
        chatGroupMemberService.add(new ChatGroupMember(groupId, chatUser).setMemberSource(source));
        // 增加接收人
        chatGroupMemberService.addMember(groupId, Arrays.asList(userId));
        // 成员列表
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 通知推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        String content = StrUtil.format(AppConstants.TIPS_GROUP_JOIN, nickname);
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_CREATE, "", userId);
    }

    @Override
    public List<GroupVo08> getMemberList(Long groupId) {
        // 验证群组
        this.verifyGroup(groupId);
        List<GroupVo08> dataList = new ArrayList<>();
        Date now = DateUtil.date();
        // 获取成员
        List<ChatGroupMember> memberList = chatGroupMemberService.queryMemberList(groupId);
        memberList.forEach(groupMember -> {
            dataList.add(BeanUtil.toBean(groupMember, GroupVo08.class)
                    .setRemark(groupMember.getDefaultRemark())
                    .setSpeakTimeLabel(now, groupMember.getSpeakTime())
            );
        });
        // 顺序
        Collections.sort(dataList, Comparator.comparing(GroupVo08::getMemberType));
        return dataList;
    }

    @Transactional
    @Override
    public void invite(GroupVo01 groupVo) {
        Long groupId = groupVo.getGroupId();
        List<Long> friendList = groupVo.getFriendList();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 验证身份
        ChatGroupMember groupMember = chatGroupMemberService.verifyGroupMember(groupId, current);
        // 成员类型
        GroupMemberEnum memberType = groupMember.getMemberType();
        // 邀请开关
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigInvite())) {
            if (GroupMemberEnum.NORMAL.equals(memberType)) {
                throw new BaseException("你不是管理员，不能操作");
            }
        }
        // 校验好友
        friendList = chatFriendService.verifyFriend(friendList);
        // 审核开关
        if (YesOrNoEnum.YES.equals(chatGroup.getConfigAudit())) {
            if (GroupMemberEnum.NORMAL.equals(memberType)) {
                chatGroupApplyService.apply(chatGroup, friendList, GroupSourceEnum.INVITE);
                return;
            }
        }
        // 校验好友
        Integer memberSize = chatGroupMemberService.getMemberSize(groupId);
        Integer levelCount = chatGroup.getGroupLevelCount();
        if (friendList.size() + memberSize > levelCount) {
            throw new BaseException("群组人数不能大于" + levelCount);
        }
        // 邀请
        _doInvite(chatGroup, friendList, groupMember.getNickname());
    }

    /**
     * 邀请
     */
    private void _doInvite(ChatGroup chatGroup, List<Long> friendList, String nickname) {
        Long groupId = chatGroup.getGroupId();
        // 查询用户
        List<ChatUser> userList = chatUserService.getByIds(friendList);
        // 成员列表
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 群组明细
        List<ChatGroupMember> dataList = new ArrayList<>();
        friendList.clear();
        userList.forEach(chatUser -> {
            Long userId = chatUser.getUserId();
            if (!memberList.contains(userId)) {
                dataList.add(new ChatGroupMember(groupId, chatUser));
                memberList.add(userId);
                friendList.add(userId);
            }
        });
        // 判断邀请集合
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 批量新增
        chatGroupMemberService.batchAdd(dataList);
        // 增加成员
        chatGroupMemberService.addMember(groupId, friendList);
        // 准备推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        // 集合取属性
        List<String> contentList = dataList.stream().map(ChatGroupMember::getNickname).collect(Collectors.toList());
        // [{}]邀请[{}]加入了群聊
        String content = StrUtil.format(AppConstants.TIPS_GROUP_INVITE, nickname, StrUtil.join("、", contentList));
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_CREATE, "", friendList);
    }

    @Transactional
    @Override
    public void kicked(GroupVo24 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        List<Long> dataList = groupVo.getDataList();
        // 验证群组
        ChatGroupMember chatGroupMember = chatGroupMemberService.verifyGroupManager(groupId, current);
        GroupMemberEnum memberType = chatGroupMember.getMemberType();
        // 管理员
        List<Long> managerList = chatGroupMemberService.getGroupAdmin(groupId);
        // 成员
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 管理员
        // 查询群组成员
        List<Long> kickedList = new ArrayList<>();
        for (Long data : dataList) {
            // 不能移除自己
            if (current.equals(data)) {
                continue;
            }
            // 不能删除其他
            if (!memberList.contains(data)) {
                continue;
            }
            // 管理员只能删除普通用户
            if (GroupMemberEnum.MANAGER.equals(memberType)) {
                if (managerList.contains(data)) {
                    continue;
                }
            }
            kickedList.add(data);
        }
        // 删除选中
        if (CollectionUtils.isEmpty(kickedList)) {
            return;
        }
        // 删除选中
        chatGroupMemberService.removeMember(groupId, kickedList);
        // 准备推送
        PushGroup pushGroup = chatGroup.getPushGroup();
        PushFrom pushFrom = chatGroup.getPushFrom();
        String content = AppConstants.TIPS_GROUP_KICKED;
        // 通知推送
        pushService.pushGroup(pushFrom, pushGroup, kickedList, content, PushMsgTypeEnum.TIPS);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_DELETE, "", dataList);
    }

    @Override
    public void setTop(GroupVo04 groupVo) {
        Long groupId = groupVo.getGroupId();
        Long current = ShiroUtils.getUserId();
        // 验证群组
        this.verifyGroup(groupId);
        YesOrNoEnum top = groupVo.getTop();
        // 校验成员
        ChatGroupMember chatGroupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (chatGroupMember == null) {
            return;
        }
        // 更新
        chatGroupMemberService.updateById(new ChatGroupMember(chatGroupMember.getMemberId()).setTop(top));
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TOP, top.getCode(), current);
    }

    @Override
    public void setDisturb(GroupVo05 groupVo) {
        Long groupId = groupVo.getGroupId();
        Long current = ShiroUtils.getUserId();
        // 验证群组
        this.verifyGroup(groupId);
        YesOrNoEnum disturb = groupVo.getDisturb();
        // 校验成员
        ChatGroupMember groupMember = chatGroupMemberService.verifyGroupMember(groupId, current);
        // 更新
        chatGroupMemberService.updateById(new ChatGroupMember(groupMember.getMemberId()).setDisturb(disturb));
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_DISTURB, disturb.getCode(), current);
    }

    @Override
    public void setRemark(GroupVo10 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        String remark = FilterUtils.filter(groupVo.getRemark(), PlatformConfig.FILTER);
        Long current = ShiroUtils.getUserId();
        // 校验成员
        ChatGroupMember chatGroupMember = chatGroupMemberService.verifyGroupMember(groupId, current);
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigNickname())) {
            if (GroupMemberEnum.NORMAL.equals(chatGroupMember.getMemberType())) {
                throw new BaseException("群主已禁止成员修改昵称");
            }
        }
        // 更新
        chatGroupMemberService.editRemark(groupId, current, remark);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_REMARK, remark, current);
    }

    @Transactional
    @Override
    public void setNickname(GroupVo21 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        // 校验管理员
        Long current = ShiroUtils.getUserId();
        ChatGroupMember manager = chatGroupMemberService.verifyGroupManager(groupId, current);
        Long userId = groupVo.getUserId();
        String nickname = FilterUtils.filter(groupVo.getNickname(), PlatformConfig.FILTER);
        // 校验成员
        ChatGroupMember member = chatGroupMemberService.queryGroupMember(groupId, userId);
        if (member == null) {
            throw new BaseException("用户不在当前群中");
        }
        if (GroupMemberEnum.MANAGER.equals(manager.getMemberType())
                && !GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
            throw new BaseException("不能修改管理员的群昵称");
        }
        // 更新
        chatGroupMemberService.editRemark(groupId, userId, nickname);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_REMARK, nickname, userId);
    }

    @Transactional
    @Override
    public void editConfigMember(GroupVo06 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        ChatGroupMember groupMember = chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configMember = groupVo.getConfigMember();
        GroupMemberEnum memberType = groupMember.getMemberType();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigMember(configMember));
        // 添加日志
        String tips = YesOrNoEnum.YES.equals(configMember) ? "开启" : "关闭";
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_MEMBER, tips);
        // 推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup paramGroup = chatGroup.getPushGroup();
        String content = StrUtil.format(AppConstants.TIPS_GROUP_PROTECT, memberType.getInfo(), tips);
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushService.pushGroup(pushFrom, paramGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_MEMBER, configMember.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigInvite(GroupVo12 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configInvite = groupVo.getConfigInvite();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigInvite(configInvite));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_INVITE, configInvite);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_INVITE, configInvite.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigTitle(GroupVo17 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configTitle = groupVo.getConfigTitle();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigTitle(configTitle));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_TITLE, configTitle);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_TITLE, configTitle.getCode(), memberList);
    }

    @Override
    public List<GroupVo27> groupLevelPrice(Long groupId) {
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 验证群主
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 查询
        return chatGroupLevelService.queryPriceList(chatGroup);
    }

    @Transactional
    @Override
    public void groupLevelPay(GroupVo28 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 验证群主
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 查询价格
        Integer groupLevel = groupVo.getGroupLevel();
        String password = groupVo.getPassword();
        Integer levelCount = chatGroup.getGroupLevelCount();
        Date levelTime = chatGroup.getGroupLevelTime();
        GroupVo27 priceInfo = chatGroupLevelService.queryPriceInfo(groupLevel, chatGroup);
        // 价格
        BigDecimal amount = priceInfo.getAmount();
        YesOrNoEnum extend = priceInfo.getExtend();
        if (YesOrNoEnum.YES.equals(extend)) {
            levelCount = priceInfo.getCount();
            levelTime = DateUtil.offsetDay(DateUtil.date(), priceInfo.getBetween());
        } else if (YesOrNoEnum.NO.equals(extend)) {
            levelTime = DateUtil.offsetDay(levelTime, priceInfo.getBetween());
        } else {
            levelCount = priceInfo.getCount();
        }
        // 扣减金额
        walletTradeService.shopping(amount, password, chatGroup, StrUtil.format("群组扩容:{}", chatGroup.getGroupNo()));
        // 开通服务
        chatGroupService.updateById(new ChatGroup()
                .setGroupId(groupId)
                .setGroupLevel(groupLevel)
                .setGroupLevelCount(levelCount)
                .setGroupLevelPrice(amount)
                .setGroupLevelTime(levelTime)
        );
        // 添加日志
        String tips = StrUtil.format("金额：{}元，人数：{}人", amount, levelCount);
        chatGroupLogService.addLog(groupId, GroupLogEnum.GROUP_SIZE, tips);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TOTAL, NumberUtil.toStr(levelCount), memberList);
    }

    @Transactional
    @Override
    public void editPrivacyNo(GroupVo34 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum privacyNo = groupVo.getPrivacyNo();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setPrivacyNo(privacyNo));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.PRIVACY_NO, privacyNo);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_PRIVACY_NO, privacyNo.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editPrivacyScan(GroupVo38 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum privacyScan = groupVo.getPrivacyScan();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setPrivacyScan(privacyScan));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.PRIVACY_NO, privacyScan);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_PRIVACY_SCAN, privacyScan.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editPrivacyName(GroupVo45 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum privacyName = groupVo.getPrivacyName();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setPrivacyName(privacyName));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.PRIVACY_NAME, privacyName);
        // 推送setting
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushSetting(groupId, ChatGroup.LABEL_PRIVACY_NAME, privacyName.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigNickname(GroupVo35 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        YesOrNoEnum configNickname = groupVo.getConfigNickname();
        chatGroupService.updateById(new ChatGroup(groupId).setConfigNickname(configNickname));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_TITLE, configNickname);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_NICKNAME, configNickname.getCode(), memberList);
    }

    @Override
    public List<Long> queryPacketWhite(Long groupId) {
        // 验证群组
        this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        return chatGroupMemberService.queryPacketWhite(groupId);
    }

    @Transactional
    @Override
    public void editPacketWhite(GroupVo37 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 查询白名单
        List<Long> whiteList = chatGroupMemberService.queryPacketWhite(groupId);
        // 新白名单
        List<Long> dataList = groupVo.getDataList();
        // 可选成员
        chatGroupMemberService.editPacketWhite(groupId, dataList);
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.PACKET_WHITE);
        // 黑名单
        List<Long> blackList = new ArrayList<>();
        whiteList.forEach(e -> {
            if (!dataList.contains(e)) {
                blackList.add(e);
            }
        });
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_WHITE, YesOrNoEnum.NO.getCode(), blackList);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_WHITE, YesOrNoEnum.YES.getCode(), dataList);
    }

    @Transactional
    @Override
    public void editConfigPacket(GroupVo18 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configPacket = groupVo.getConfigPacket();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigPacket(configPacket));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_PACKET, configPacket);
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_PACKET, configPacket.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigAmount(GroupVo44 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configAmount = groupVo.getConfigAmount();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigAmount(configAmount));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_AMOUNT, configAmount);
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_AMOUNT, configAmount.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigReceive(GroupVo36 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configReceive = groupVo.getConfigReceive();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigReceive(configReceive));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_RECEIVE, configReceive);
        // 推送setting
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_RECEIVE, configReceive.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigScan(GroupVo46 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        YesOrNoEnum configScan = groupVo.getConfigScan();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigScan(configScan));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_SCAN, configScan);
        // 推送setting
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_SCAN, configScan.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigAssign(GroupVo19 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        YesOrNoEnum configAssign = groupVo.getConfigAssign();
        chatGroupService.updateById(new ChatGroup(groupId).setConfigAssign(configAssign));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_ASSIGN, configAssign);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_ASSIGN, configAssign.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigMedia(GroupVo20 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        YesOrNoEnum configMedia = groupVo.getConfigMedia();
        chatGroupService.updateById(new ChatGroup(groupId).setConfigMedia(configMedia));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_MEDIA, configMedia);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_MEDIA, configMedia.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigSpeak(GroupVo13 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        ChatGroupMember chatGroupMember = chatGroupMemberService.verifyGroupManager(groupId, current);
        GroupMemberEnum memberType = chatGroupMember.getMemberType();
        YesOrNoEnum configSpeak = groupVo.getConfigSpeak();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setConfigSpeak(configSpeak));
        // 添加日志
        String tips = YesOrNoEnum.YES.equals(configSpeak) ? "开启" : "关闭";
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_SPEAK, tips);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        String content = StrUtil.format(AppConstants.TIPS_GROUP_FORBID, memberType.getInfo(), tips);
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_SPEAK, configSpeak.getCode(), memberList);
    }

    @Transactional
    @Override
    public void editConfigAudit(GroupVo22 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 查询群组
        this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        YesOrNoEnum configAudit = groupVo.getConfigAudit();
        chatGroupService.updateById(new ChatGroup(groupId).setConfigAudit(configAudit));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.CONFIG_AUDIT, configAudit);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_CONFIG_AUDIT, configAudit.getCode(), memberList);
    }

    @Transactional
    @Override
    public void transfer(GroupVo14 groupVo) {
        Long groupId = groupVo.getGroupId();
        Long current = ShiroUtils.getUserId();
        Long userId = groupVo.getUserId();
        // 校验自己
        if (current.equals(userId)) {
            throw new BaseException("不能转给自己");
        }
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 校验群主
        ChatGroupMember masterMember = chatGroupMemberService.verifyGroupMaster(groupId, current);
        // 校验成员
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, userId);
        if (groupMember == null) {
            throw new BaseException("对方不在群组中，不能转让");
        }
        // 群主
        chatGroupMemberService.updateById(new ChatGroupMember(masterMember.getMemberId()).setMemberType(GroupMemberEnum.NORMAL));
        // 被转让人
        chatGroupMemberService.updateById(new ChatGroupMember(groupMember.getMemberId()).setMemberType(GroupMemberEnum.MASTER));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.TRANSFER, userId);
        // 通知
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        String nickname = groupMember.getNickname();
        String content = StrUtil.format(AppConstants.TIPS_GROUP_TRANSFER, nickname);
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 删除缓存
        chatGroupMemberService.clearCache(groupId);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TYPE, GroupMemberEnum.NORMAL.getCode(), current);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TYPE, GroupMemberEnum.MASTER.getCode(), userId);
    }

    @Transactional
    @Override
    public void setManager(GroupVo15 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验群主
        chatGroupMemberService.verifyGroupMaster(groupId, current);
        // 查询
        List<Long> whiteList = chatGroupMemberService.getGroupManager(groupId);
        // 设置
        List<Long> dataList = chatGroupMemberService.setManager(groupId, groupVo.getMemberList());
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.MANAGER);
        // 通知
        String content = AppConstants.TIPS_GROUP_MANAGER;
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        pushService.pushGroup(pushFrom, pushGroup, dataList, content, PushMsgTypeEnum.TIPS);
        // 黑名单
        List<Long> blackList = new ArrayList<>();
        whiteList.forEach(e -> {
            if (!dataList.contains(e)) {
                blackList.add(e);
            }
        });
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TYPE, GroupMemberEnum.MANAGER.getCode(), dataList);
        // 推送通知
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_TYPE, GroupMemberEnum.NORMAL.getCode(), blackList);
    }

    @Transactional
    @Override
    public void editNoticeTop(GroupVo07 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        Long current = ShiroUtils.getUserId();
        // 校验管理
        chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        YesOrNoEnum noticeTop = groupVo.getNoticeTop();
        chatGroupService.updateById(new ChatGroup(groupId).setNoticeTop(noticeTop));
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.NOTICE_TOP, noticeTop);
        // 推送setting
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushSetting(groupId, ChatGroup.LABEL_GROUP_NOTICE_TOP, noticeTop.getCode(), memberList);
    }

    @Transactional
    @Override
    public void logout(Long groupId) {
        // 查询群组
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        Long current = ShiroUtils.getUserId();
        if (chatGroup == null) {
            // 删除缓存
            chatGroupMemberService.removeMember(groupId, Arrays.asList(current));
            // 清空消息
            pushService.clearMsg(current, groupId);
            // 通知推送
            pushSetting(groupId, ChatGroup.LABEL_DELETE, "", current);
            return;
        }
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (groupMember == null) {
            // 删除缓存
            chatGroupMemberService.removeMember(groupId, Arrays.asList(current));
            // 清空消息
            pushService.clearMsg(current, groupId);
            // 通知推送
            pushSetting(groupId, ChatGroup.LABEL_DELETE, "", current);
            return;
        }
        if (GroupMemberEnum.MASTER.equals(groupMember.getMemberType())) {
            throw new BaseException("群主不能退出群组，请解散群");
        }
        String nickname = groupMember.getDefaultRemark();
        // 通知
        Long master = chatGroupMemberService.getGroupMaster(groupId);
        String content = StrUtil.format(AppConstants.TIPS_GROUP_LOGOUT, nickname);
        // 推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        pushService.pushGroup(pushFrom, pushGroup, Arrays.asList(master), content, PushMsgTypeEnum.TIPS);
        // 删除缓存
        chatGroupMemberService.removeMember(groupId, Arrays.asList(current));
        // 清空消息
        pushService.clearMsg(current, groupId);
        // 通知推送
        pushSetting(groupId, ChatGroup.LABEL_DELETE, "", current);
    }

    @Transactional
    @Override
    public void dissolve(Long groupId) {
        Long current = ShiroUtils.getUserId();
        // 查询群组
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            pushSetting(groupId, ChatGroup.LABEL_DELETE, "", current);
            return;
        }
        // 查询成员
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (groupMember == null) {
            pushSetting(groupId, ChatGroup.LABEL_DELETE, "", current);
            return;
        }
        if (!GroupMemberEnum.MASTER.equals(groupMember.getMemberType())) {
            throw new BaseException("你不是群主，不能操作");
        }
        // 推送
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 删除
        chatGroupService.deleteById(groupId);
        // 删除
        chatGroupMemberService.delGroup(groupId);
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.DISSOLVE);
        // 推送
        String content = AppConstants.TIPS_GROUP_DISSOLVE;
        // 推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_DELETE, "", memberList);
        // 通知推送
        pushService.clearMsg(groupId);
    }

    @Transactional
    @Override
    public void editGroupName(GroupVo02 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 验证管理
        Long current = ShiroUtils.getUserId();
        ChatGroupMember chatGroupMember = chatGroupMemberService.verifyGroupManager(groupId, current);
        String groupName = FilterUtils.filter(groupVo.getGroupName(), PlatformConfig.FILTER);
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setGroupName(groupName));
        // 更新群名
        chatRefreshService.refreshGroupName(groupId, groupName);
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.GROUP_NAME, groupVo.getGroupName());
        // 推送
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        String content = StrUtil.format(AppConstants.TIPS_GROUP_NAME, chatGroupMember.getMemberType().getInfo(), groupName);
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_GROUP_NAME, groupName, memberList);
    }

    @Transactional
    @Override
    public void editPortrait(GroupVo09 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        this.verifyGroup(groupId);
        // 校验管理
        Long current = ShiroUtils.getUserId();
        chatGroupMemberService.verifyGroupManager(groupId, current);
        String portrait = groupVo.getPortrait();
        // 更新
        chatGroupService.updateById(new ChatGroup(groupId).setPortrait(portrait));
        // 删除头像
        chatResourceService.delResource(portrait);
        // 添加日志
        chatGroupLogService.addLog(groupId, GroupLogEnum.PORTRAIT, portrait);
        // 查询
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_GROUP_PORTRAIT, portrait, memberList);
    }

    @Transactional
    @Override
    public void editNotice(GroupVo03 groupVo) {
        Long groupId = groupVo.getGroupId();
        // 验证群组
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 验证管理
        Long current = ShiroUtils.getUserId();
        ChatGroupMember chatGroupMember = chatGroupMemberService.verifyGroupManager(groupId, current);
        String notice = FilterUtils.filter(groupVo.getNotice(), PlatformConfig.FILTER);
        // 更新数据
        this.update(Wrappers.<ChatGroup>lambdaUpdate()
                .set(ChatGroup::getNotice, notice)
                .eq(ChatGroup::getGroupId, groupId));
        // 更新缓存
        this.clearCache(groupId);
        // 添加日志
        String tips = StringUtils.isEmpty(groupVo.getNotice()) ? "-" : groupVo.getNotice();
        chatGroupLogService.addLog(groupId, GroupLogEnum.NOTICE, tips);
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 推送
        if (!StringUtils.isEmpty(notice)) {
            String content = StrUtil.format(AppConstants.TIPS_GROUP_NOTICE, chatGroupMember.getMemberType().getInfo(), notice);
            PushFrom pushFrom = chatGroup.getPushFrom();
            PushGroup pushGroup = chatGroup.getPushGroup();
            pushService.pushGroup(pushFrom, pushGroup, memberList, content, PushMsgTypeEnum.TIPS);
        }
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_GROUP_NOTICE, notice, memberList);
    }

    @Transactional
    @Override
    public void speak(GroupVo26 groupVo) {
        Long current = ShiroUtils.getUserId();
        Long userId = groupVo.getUserId();
        // 校验自己
        if (current.equals(userId)) {
            throw new BaseException("不能对自己禁言");
        }
        // 验证群组
        Long groupId = groupVo.getGroupId();
        ChatGroup chatGroup = this.verifyGroup(groupId);
        // 验证管理
        ChatGroupMember manager = chatGroupMemberService.verifyGroupManager(groupId, current);
        // 更新
        GroupSpeakEnum speakType = groupVo.getSpeakType();
        ChatGroupMember member = chatGroupMemberService.queryGroupMember(groupId, userId);
        if (member == null) {
            throw new BaseException("用户不在当前群中");
        }
        if (GroupMemberEnum.MANAGER.equals(manager.getMemberType())) {
            if (!GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                throw new BaseException("不能对管理员禁言");
            }
        }
        // 更新
        ChatGroupMember groupMember = new ChatGroupMember()
                .setMemberId(member.getMemberId())
                .setSpeak(YesOrNoEnum.transform(!GroupSpeakEnum.CLEAR.equals(speakType)))
                .setSpeakTime(DateUtil.offsetHour(DateUtil.date(), speakType.getValue()));
        chatGroupMemberService.updateById(groupMember);
        // 查询管理员
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 发送通知
        String nickName1 = member.getNickname();
        String nickName2 = manager.getDefaultRemark();
        String content = StrUtil.format("[{}]被[{}]禁言[{}]", nickName1, nickName2, speakType.getName());
        if (GroupSpeakEnum.CLEAR.equals(speakType)) {
            content = StrUtil.format("[{}]被[{}]解除禁言", nickName1, nickName2);
        }
        pushService.pushGroup(chatGroup.getPushFrom(), chatGroup.getPushGroup(), memberList, content, PushMsgTypeEnum.TIPS);
        // 推送setting
        pushSetting(groupId, ChatGroup.LABEL_MEMBER_SPEAK, groupMember.getSpeak().getCode(), userId);
    }

    /**
     * 验证群组
     */
    private ChatGroup verifyGroup(Long groupId) {
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            throw new BaseException("群组不存在");
        }
        return chatGroup;
    }

    /**
     * 移除缓存
     */
    private void clearCache(Long groupId) {
        redisUtils.delete(AppConstants.REDIS_CHAT_GROUP + "::" + groupId);
    }

    // 推送setting
    private void pushSetting(Long groupId, String label, String value, Long userId) {
        pushSetting(groupId, label, value, Arrays.asList(userId));
    }

    // 推送setting
    private void pushSetting(Long groupId, String label, String value, List<Long> receiveList, Long userId) {
        receiveList.add(userId);
        PushSetting setting = new PushSetting(PushSettingEnum.GROUP, groupId, label, value);
        pushService.pushSetting(setting, receiveList);
    }

    // 推送setting
    private void pushSetting(Long groupId, String label, String value, List<Long> receiveList) {
        PushSetting setting = new PushSetting(PushSettingEnum.GROUP, groupId, label, value);
        pushService.pushSetting(setting, receiveList);
    }

}
