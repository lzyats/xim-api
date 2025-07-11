package com.platform.modules.pay.config;

import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayWxConfiguration {

    @Autowired
    private PayWxConfig payWxConfig;

    @Bean
    public WxPayApiConfig wxPayApiConfig() {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder()
                .appId(payWxConfig.getAppId())
                .mchId(payWxConfig.getMchId())
                .partnerKey(payWxConfig.getPartnerKey())
                .certPath(payWxConfig.getCertPath())
                .domain(payWxConfig.getNotifyUrl())
                .build();
        return WxPayApiConfigKit.putApiConfig(wxPayApiConfig);
    }

}
