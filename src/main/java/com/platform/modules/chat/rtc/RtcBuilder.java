package com.platform.modules.chat.rtc;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RtcBuilder {

    @Autowired
    private RtcConfig config;

    public String buildToken(Long channel, String userNo) {
        String appId = config.getAppId();
        String secret = config.getSecret();
        int expire = config.getExpired();
        RtcToken rtcToken = new RtcToken(appId, secret, expire);
        RtcToken.Service serviceRtc = new RtcToken.ServiceRtc(NumberUtil.toStr(channel), userNo);

        serviceRtc.addPrivilegeRtc(RtcType.JOIN, expire);
        serviceRtc.addPrivilegeRtc(RtcType.AUDIO, expire);
        serviceRtc.addPrivilegeRtc(RtcType.VIDEO, expire);
        serviceRtc.addPrivilegeRtc(RtcType.DATA, expire);
        rtcToken.addService(serviceRtc);
        try {
            return rtcToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
