package com.platform.modules.common.service.impl;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.handler.VersionHandlerMapping;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.rtc.RtcConfig;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.common.service.CommonService;
import com.platform.modules.common.vo.CommonVo06;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private RtcConfig rtcConfig;

    @Override
    public void getMapping() {
        VersionHandlerMapping handlerMapping = applicationContext.getBean(VersionHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        Set<String> dataList = new TreeSet<>();
        for (RequestMappingInfo info : map.keySet()) {
            // 获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            patterns.forEach(data -> {
                while (StrUtil.contains(data, "{")) {
                    int len = data.lastIndexOf("{");
                    if (len > 0) {
                        data = StrUtil.sub(data, 0, len);
                    }
                }
                if (StrUtil.endWith(data, "/")) {
                    data = StrUtil.removeSuffix(data, "/");
                }
                if (!StringUtils.isEmpty(data)) {
                    dataList.add(data);
                }
            });
        }
        dataList.forEach(data -> {
            String key = RandomUtil.randomString(16);
            Console.log(StrUtil.format("  \"{}\": \"{}\"", key, data));
        });
    }

    @Override
    public CommonVo06 getConfig() {
        // 查询数据
        Map<ChatConfigEnum, ChatConfig> dataMap = chatConfigService.queryConfig();
        CommonVo06 result = new CommonVo06()
                .setSharePath(dataMap.get(ChatConfigEnum.SYS_SHARE).getStr())
                .setPacket(dataMap.get(ChatConfigEnum.SYS_PACKET).getBigDecimal())
                .setGroupSearch(dataMap.get(ChatConfigEnum.GROUP_NAME_SEARCH).getYesOrNo())
                .setHoldCard(dataMap.get(ChatConfigEnum.USER_HOLD).getYesOrNo())
                .setCallKit(YesOrNoEnum.YES.getCode().equals(rtcConfig.getEnabled()) ? rtcConfig.getAppId() : null)
                .setBeian(dataMap.get(ChatConfigEnum.SYS_BEIAN).getStr())
                .setMessageLimit(AppConstants.MESSAGE_LIMIT - 50);
        if (YesOrNoEnum.YES.getCode().equals(rtcConfig.getEnabled())) {
            result.setCallKit(rtcConfig.getAppId());
        }
        if (!ShiroUtils.getPhone().equals(dataMap.get(ChatConfigEnum.SYS_PHONE).getStr())) {
            result.setWatermark(dataMap.get(ChatConfigEnum.SYS_WATERMARK).getStr())
                    .setScreenshot(dataMap.get(ChatConfigEnum.SYS_SCREENSHOT).getYesOrNo())
                    .setNotice(getNotice(dataMap));
        }
        return result;
    }

    /**
     * 获取公告
     */
    private String getNotice(Map<ChatConfigEnum, ChatConfig> dataMap) {
        String content = null;
        if (redisUtils.hasKey(AppConstants.REDIS_CHAT_NOTICE)) {
            content = redisUtils.get(AppConstants.REDIS_CHAT_NOTICE);
        } else {
            YesOrNoEnum status = dataMap.get(ChatConfigEnum.NOTICE_STATUS).getYesOrNo();
            if (YesOrNoEnum.transform(status)) {
                content = dataMap.get(ChatConfigEnum.NOTICE_CONTENT).getStr();
            }
            if (StringUtils.isEmpty(content)) {
                content = "";
            }
            redisUtils.set(AppConstants.REDIS_CHAT_NOTICE, content, 30, TimeUnit.DAYS);
        }
        return content;
    }

}
