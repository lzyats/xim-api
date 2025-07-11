package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFriendInformDao;
import com.platform.modules.chat.domain.ChatFriendInform;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.ChatFriendInformService;
import com.platform.modules.chat.service.ChatUserLogService;
import com.platform.modules.chat.vo.FriendVo07;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.enums.PushAuditEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 骚扰举报 服务层实现
 * </p>
 */
@Service("chatFriendInformService")
public class ChatFriendInformServiceImpl extends BaseServiceImpl<ChatFriendInform> implements ChatFriendInformService {

    @Resource
    private ChatFriendInformDao chatFriendInformDao;

    @Resource
    private HookService hookService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFriendInformDao);
    }

    @Override
    public List<ChatFriendInform> queryList(ChatFriendInform t) {
        List<ChatFriendInform> dataList = chatFriendInformDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public void inform(FriendVo07 friendVo) {
        Long current = ShiroUtils.getUserId();
        Long groupId = friendVo.getUserId();
        ChatFriendInform inform = new ChatFriendInform()
                .setInformType(friendVo.getInformType())
                .setUserId(current)
                .setImages(JSONUtil.toJsonStr(friendVo.getImages()))
                .setContent(friendVo.getContent())
                .setGroupId(groupId)
                .setStatus(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        this.add(inform);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.INFORM_FROM, groupId);
        chatUserLogService.addLog(groupId, UserLogEnum.INFORM_TO, current);
        // 通知
        hookService.handle(PushAuditEnum.INFORM_USER);
    }

}
