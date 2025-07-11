package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupInformDao;
import com.platform.modules.chat.domain.ChatGroupInform;
import com.platform.modules.chat.service.ChatGroupInformService;
import com.platform.modules.chat.vo.GroupVo40;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.enums.PushAuditEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 骚扰举报 服务层实现
 * </p>
 */
@Service("chatGroupInformService")
public class ChatGroupInformServiceImpl extends BaseServiceImpl<ChatGroupInform> implements ChatGroupInformService {

    @Resource
    private ChatGroupInformDao chatGroupInformDao;

    @Resource
    private HookService hookService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupInformDao);
    }

    @Override
    public List<ChatGroupInform> queryList(ChatGroupInform t) {
        List<ChatGroupInform> dataList = chatGroupInformDao.queryList(t);
        return dataList;
    }

    @Override
    public void inform(GroupVo40 groupVo) {
        ChatGroupInform inform = new ChatGroupInform()
                .setInformType(groupVo.getInformType())
                .setUserId(ShiroUtils.getUserId())
                .setImages(JSONUtil.toJsonStr(groupVo.getImages()))
                .setContent(groupVo.getContent())
                .setGroupId(groupVo.getGroupId())
                .setStatus(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        this.add(inform);
        // 通知
        hookService.handle(PushAuditEnum.INFORM_GROUP);
    }

}
