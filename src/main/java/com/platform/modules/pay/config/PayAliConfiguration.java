package com.platform.modules.pay.config;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.codec.Base64;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class PayAliConfiguration {

    @Autowired
    private PayAliConfig payAliConfig;

    @Bean
    public AliPayApiConfig aliPayApiConfig() throws AlipayApiException {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                // appId
                .setAppId(payAliConfig.getAppId())
                // 应用私钥
                .setPrivateKey(payAliConfig.getAppPrivateKey())
                // 应用公钥
                .setAppCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAppPublicPath()))
                // 支付宝公钥
                .setAliPayCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAliPayPublicPath()))
                // 支付宝根证书
                .setAliPayRootCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAliPayRootPath()))
                // 支付网关
                .setServiceUrl(payAliConfig.getServiceUrl())
                .buildByCertContent();
        this.getAliPayPublicKey();
        return AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
    }

    private void getAliPayPublicKey() throws AlipayApiException {
        InputStream inputStream = null;
        String aliPayPublicKey = null;
        try {
            inputStream = ResourceUtil.getStream(payAliConfig.getAliPayPublicPath());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            PublicKey publicKey = cert.getPublicKey();
            aliPayPublicKey = Base64.encodeBase64String(publicKey.getEncoded());
        } catch (Exception e) {
            Console.error("支付宝配置不正确", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var18) {
                throw new AlipayApiException(var18);
            }
        }
        payAliConfig.setAliPayPublicKey(aliPayPublicKey);
    }

}
