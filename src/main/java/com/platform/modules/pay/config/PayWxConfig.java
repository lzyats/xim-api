package com.platform.modules.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "pay.wx")
@Component
public class PayWxConfig {

    /**
     * APP应用的id
     */
    private String appId;
    /**
     * 直连商户号
     */
    private String mchId;
    /**
     * 商户密钥
     */
    private String partnerKey;
    /**
     * 证书地址
     */
    private String certPath;
    /**
     * 通知回调地址
     */
    private String notifyUrl;
    /**
     * universalLink
     */
    private String universalLink;

}
