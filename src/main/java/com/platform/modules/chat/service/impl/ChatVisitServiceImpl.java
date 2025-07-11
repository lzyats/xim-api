package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVisitDao;
import com.platform.modules.chat.domain.ChatVisit;
import com.platform.modules.chat.service.ChatVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户访问 服务层实现
 * </p>
 */
@Service("chatVisitService")
public class ChatVisitServiceImpl extends BaseServiceImpl<ChatVisit> implements ChatVisitService {

    @Resource
    private ChatVisitDao chatVisitDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatVisitDao);
    }

    @Override
    public List<ChatVisit> queryList(ChatVisit t) {
        List<ChatVisit> dataList = chatVisitDao.queryList(t);
        return dataList;
    }

}
