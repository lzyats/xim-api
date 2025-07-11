/**
 * Licensed to the Apache Software Foundation （ASF） under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * （the "License"）； you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.platform.modules.common.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import com.platform.common.utils.CodeUtils;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatSmsService;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.common.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 短信 服务层
 */
@Service("messageService")
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatSmsService chatSmsService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private SmsService smsService;

    @Override
    public Dict sendSms(String phone, String email, MessageTypeEnum messageType, YesOrNoEnum special) {
        // 验证
        if (!Validator.isMobile(phone)) {
            throw new BaseException("请输入正确的账号");
        }
        String redisKey = messageType.getPrefix().concat(phone);
        // 生成验证码
        String code = CodeUtils.smsCode();
        // 正常账号
        if (YesOrNoEnum.NO.equals(special)) {
            // 获取配置
            YesOrNoEnum config = chatConfigService.queryConfig(ChatConfigEnum.USER_SMS).getYesOrNo();
            // 测试短信
            if (YesOrNoEnum.NO.equals(config)) {
                special = YesOrNoEnum.YES;
            }
        }
        SmsVo smsVo;
        String msg = "验证码已发送";
        // 测试
        if (YesOrNoEnum.transform(special) || StringUtils.isEmpty(email)) {
            smsVo = new SmsVo().setResult(true).setCode(code).setBody("local");
        }
        // 邮箱
        else if (PlatformConfig.EMAIL) {
            smsVo = smsService.sendMessage(email, code);
            msg = StrUtil.format("验证码已发送至{}", DesensitizedUtil.email(email));
        }
        // 正常
        else {
            smsVo = smsService.sendMessage(phone, code);
        }
        chatSmsService.addSms(phone, code, smsVo);
        // 存入缓存
        redisUtils.set(redisKey, code, messageType.getTimeout(), TimeUnit.MINUTES);
        return Dict.create()
                .set("code", smsVo.getCode())
                .set("msg", msg);
    }

    @Override
    public void verifySms(String phone, String code, MessageTypeEnum messageType) {
        // 获取配置
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        String phoneConfig = configMap.get(ChatConfigEnum.SYS_PHONE).getStr();
        YesOrNoEnum auditConfig = configMap.get(ChatConfigEnum.SYS_AUDIT).getYesOrNo();
        if (phone.equals(phoneConfig) && YesOrNoEnum.YES.equals(auditConfig)) {
            return;
        }
        String redisKey = messageType.getPrefix().concat(phone);
        if (!redisUtils.hasKey(redisKey)) {
            throw new BaseException("验证码已过期，请重新获取");
        }
        String value = redisUtils.get(redisKey);
        if (value.equalsIgnoreCase(code)) {
            redisUtils.delete(redisKey);
        } else {
            throw new BaseException("验证码不正确，请重新获取");
        }
    }

}
