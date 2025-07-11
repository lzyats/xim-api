package com.platform.modules.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "pay.ali")
@Component
public class PayAliConfig {

    /**
     * 应用编号
     */
    private String appId;
    /**
     * 应用私钥
     */
    private String appPrivateKey;
    /**
     * 应用公钥
     */
    private String appPublicPath;
    /**
     * 支付宝公钥
     */
    private String aliPayPublicPath;
    /**
     * 支付宝公钥
     */
    private String aliPayPublicKey;
    /**
     * 支付宝根证书
     */
    private String aliPayRootPath;
    /**
     * 支付网关
     */
    private String serviceUrl;
    /**
     * 回调地址
     */
    private String notifyUrl;

}
