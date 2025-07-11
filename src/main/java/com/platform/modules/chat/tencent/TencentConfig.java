package com.platform.modules.chat.tencent;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 腾讯语音识别
 */
@Component
@Data
public class TencentConfig {

    @Value("${tencent.enabled}")
    private String enabled;

    @Value("${tencent.appId}")
    private String appId;

    @Value("${tencent.accessKey}")
    private String accessKey;

    @Value("${tencent.secretKey}")
    private String secretKey;

}