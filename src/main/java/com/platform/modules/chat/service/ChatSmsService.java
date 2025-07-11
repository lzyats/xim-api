package com.platform.modules.chat.service;

import com.platform.common.sms.vo.SmsVo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatSms;

/**
 * <p>
 * 短信记录 服务层
 * </p>
 */
public interface ChatSmsService extends BaseService<ChatSms> {

    /**
     * 发送短信
     */
    void addSms(String mobile, String content, SmsVo smsVo);

}
