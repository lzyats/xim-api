package com.platform.modules.chat.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatBannedService;
import com.platform.modules.chat.dao.ChatBannedDao;
import com.platform.modules.chat.domain.ChatBanned;

/**
 * <p>
 * 封禁状态 服务层实现
 * </p>
 */
@Service("chatBannedService")
public class ChatBannedServiceImpl extends BaseServiceImpl<ChatBanned> implements ChatBannedService {

    @Resource
    private ChatBannedDao chatBannedDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatBannedDao);
    }

    @Override
    public List<ChatBanned> queryList(ChatBanned t) {
        List<ChatBanned> dataList = chatBannedDao.queryList(t);
        return dataList;
    }

}
