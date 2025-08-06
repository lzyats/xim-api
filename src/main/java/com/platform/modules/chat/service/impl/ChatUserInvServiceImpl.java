package com.platform.modules.chat.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatUserInvService;
import com.platform.modules.chat.dao.ChatUserInvDao;
import com.platform.modules.chat.domain.ChatUserInv;
import com.platform.modules.chat.service.ChatUserInvService;

/**
 * <p>
 * 会员注册邀请表 服务层实现
 * </p>
 */
@Service("chatUserInvService")
public class ChatUserInvServiceImpl extends BaseServiceImpl<ChatUserInv> implements ChatUserInvService {

    @Resource
    private ChatUserInvDao chatUserInvDao;

    @Resource
    private ChatUserInvService chatUserInvService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserInvDao);
    }

    @Override
    public List<ChatUserInv> queryList(ChatUserInv t) {
        List<ChatUserInv> dataList = chatUserInvDao.queryList(t);
        return dataList;
    }

    /**
     * 执行邀请
     */
    @Override
    public void invode(ChatUserInv chatUserInv){
        chatUserInvService.add(chatUserInv);
    }

}
