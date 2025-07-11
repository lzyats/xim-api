package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupSolitaireDao;
import com.platform.modules.chat.domain.ChatGroupSolitaire;
import com.platform.modules.chat.service.ChatGroupMemberService;
import com.platform.modules.chat.service.ChatGroupSolitaireService;
import com.platform.modules.chat.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 成语接龙 服务层实现
 * </p>
 */
@Service("chatGroupSolitaireService")
public class ChatSolitaireServiceImpl extends BaseServiceImpl<ChatGroupSolitaire> implements ChatGroupSolitaireService {

    @Resource
    private ChatGroupSolitaireDao chatGroupSolitaireDao;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupSolitaireDao);
    }

    @Override
    public List<ChatGroupSolitaire> queryList(ChatGroupSolitaire t) {
        List<ChatGroupSolitaire> dataList = chatGroupSolitaireDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public GroupVo41 createSolitaire(GroupVo29 groupVo) {
        Long groupId = groupVo.getGroupId();
        String subject = groupVo.getSubject();
        String example = groupVo.getExample();
        List<GroupVo30> dataList = groupVo.getDataList();
        Long current = ShiroUtils.getUserId();
        // 验证群员
        chatGroupMemberService.verifyGroupMember(groupId, current);
        // 写入数据
        ChatGroupSolitaire solitaire = new ChatGroupSolitaire()
                .setUserId(current)
                .setGroupId(groupId)
                .setSubject(subject)
                .setExample(example)
                .setCreateTime(DateUtil.date());
        this.add(solitaire);
        Long solitaireId = solitaire.getSolitaireId();
        // 存入缓存2
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.forEach(data -> {
                if (!StringUtils.isEmpty(StrUtil.trimToNull(data.getContent()))) {
                }
            });
        }
        return new GroupVo41().setSolitaireId(solitaireId);
    }

    @Override
    public GroupVo43 querySolitaire(GroupVo31 groupVo) {
        Long groupId = groupVo.getGroupId();
        Long solitaireId = groupVo.getSolitaireId();
        Long current = ShiroUtils.getUserId();
        // 验证群员
        chatGroupMemberService.verifyGroupMember(groupId, current);
        ChatGroupSolitaire solitaire;
        // 缓存
        // 查询redis2
        List<GroupVo33> arrayList = new ArrayList<>();
//                    String content = dataList.get(index);
//                    arrayList.add(new GroupVo33(splitArray.get(4), splitArray.get(5), content));
        // 查询数据库
        solitaire = this.findById(solitaireId, "当前接龙已过期");
        String content = solitaire.getContent();
        return new GroupVo43()
                .setSolitaireId(solitaireId)
                .setGroupId(groupId)
                .setSubject(solitaire.getSubject())
                .setExample(solitaire.getExample())
                .setCreateTime(solitaire.getCreateTime())
                .setExpired(YesOrNoEnum.transform(DateUtil.between(DateUtil.date(), solitaire.getCreateTime(), DateUnit.HOUR) > 23))
                .setDataList(JSONUtil.toList(content, GroupVo33.class));
    }

    @Override
    public GroupVo42 submitSolitaire(GroupVo32 groupVo) {
        List<GroupVo30> dataList = groupVo.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return new GroupVo42();
        }
        Long groupId = groupVo.getGroupId();
        Long current = ShiroUtils.getUserId();
        // 验证群员
        chatGroupMemberService.verifyGroupMember(groupId, current);
        Long solitaireId = groupVo.getSolitaireId();
        dataList.forEach(data -> {
            Long line = data.getLine();
            String content = data.getContent();
            // 删除
            if (StringUtils.isEmpty(content)) {
                if (line != null) {
                }
            }
            // 新增
            else if (line == null) {
            }
            // 修改
            else {
            }
        });
        return new GroupVo42().setSolitaireId(solitaireId);
    }

}
