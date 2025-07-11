package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatRobotSubDao;
import com.platform.modules.chat.domain.ChatRobotSub;
import com.platform.modules.chat.service.ChatRobotSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务号 服务层实现
 * </p>
 */
@Service("chatRobotSubService")
public class ChatRobotSubServiceImpl extends BaseServiceImpl<ChatRobotSub> implements ChatRobotSubService {

    @Resource
    private ChatRobotSubDao chatRobotSubDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatRobotSubDao);
    }

    @Override
    public List<ChatRobotSub> queryList(ChatRobotSub t) {
        List<ChatRobotSub> dataList = chatRobotSubDao.queryList(t);
        return dataList;
    }

}
