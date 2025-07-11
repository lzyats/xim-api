package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatPortraitDao;
import com.platform.modules.chat.domain.ChatPortrait;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatPortraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 聊天头像 服务层实现
 * </p>
 */
@Service("chatPortraitService")
public class ChatPortraitServiceImpl extends BaseServiceImpl<ChatPortrait> implements ChatPortraitService {

    @Resource
    private ChatPortraitDao chatPortraitDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatPortraitDao);
    }

    @Override
    public List<ChatPortrait> queryList(ChatPortrait t) {
        List<ChatPortrait> dataList = chatPortraitDao.queryList(t);
        return dataList;
    }

    @Override
    public String queryGroupPortrait() {
        return this. queryPortrait(ChatTalkEnum.GROUP);
    }

    @Override
    public String queryUserPortrait() {
        return this. queryPortrait(ChatTalkEnum.FRIEND);
    }

    private String queryPortrait(ChatTalkEnum chatType) {
        ChatPortrait query = new ChatPortrait(chatType);
        List<ChatPortrait> dataList = this.queryList(query);
        // 随机排序
        Collections.shuffle(dataList);
        // 取第一条
        return dataList.get(0).getPortrait();
    }
}
