package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.sms.vo.SmsVo;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatSmsDao;
import com.platform.modules.chat.domain.ChatSms;
import com.platform.modules.chat.service.ChatSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 短信记录 服务层实现
 * </p>
 */
@Service("chatSmsService")
public class ChatSmsServiceImpl extends BaseServiceImpl<ChatSms> implements ChatSmsService {

    @Resource
    private ChatSmsDao chatSmsDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatSmsDao);
    }

    @Override
    public List<ChatSms> queryList(ChatSms t) {
        List<ChatSms> dataList = chatSmsDao.queryList(t);
        return dataList;
    }

    @Override
    public void addSms(String mobile, String content, SmsVo smsVo) {
        ThreadUtil.execAsync(() -> {
            ChatSms sms = new ChatSms()
                    .setMobile(mobile)
                    .setContent(content)
                    .setStatus(YesOrNoEnum.transform(smsVo.isResult()))
                    .setBody(smsVo.getBody())
                    .setCreateTime(DateUtil.date());
            this.add(sms);
        });
    }
}
