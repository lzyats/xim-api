package com.platform.modules.chat.rtc;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 音视频配置
 */
@Component
@Data
public class RtcConfig {

    @Value("${rtc.enabled:N}")
    private String enabled;

    @Value("${rtc.appId}")
    private String appId;

    @Value("${rtc.secret}")
    private String secret;

    @Value("${rtc.expired}")
    private Integer expired;

}