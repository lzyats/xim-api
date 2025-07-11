package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVoiceDao;
import com.platform.modules.chat.domain.ChatVoice;
import com.platform.modules.chat.service.ChatVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 声音表 服务层实现
 * </p>
 */
@Service("chatVoiceService")
public class ChatVoiceServiceImpl extends BaseServiceImpl<ChatVoice> implements ChatVoiceService {

    @Resource
    private ChatVoiceDao chatVoiceDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatVoiceDao);
    }

    @Override
    public List<ChatVoice> queryList(ChatVoice t) {
        List<ChatVoice> dataList = chatVoiceDao.queryList(t);
        return dataList;
    }

    @Override
    public void addVoice(Long msgId, String voicePath) {
        ChatVoice chatVoice = new ChatVoice()
                .setMsgId(msgId)
                .setVoiceUrl(voicePath)
                .setUserId(ShiroUtils.getUserId())
                .setCreateTime(DateUtil.date());
        this.add(chatVoice);
    }
}
