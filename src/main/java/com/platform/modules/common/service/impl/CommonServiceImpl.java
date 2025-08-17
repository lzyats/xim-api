package com.platform.modules.common.service.impl;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.handler.VersionHandlerMapping;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.rtc.RtcConfig;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.impl.ChatNoticeServiceImpl;
import com.platform.modules.common.service.CommonService;
import com.platform.modules.common.vo.CommonVo06;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private RedisJsonUtil redisJsonUtil;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private RtcConfig rtcConfig;


    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);


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
        logger.info("获取配置信息");
        String redisKey = AppConstants.REDIS_COMMON_CONFIG; // 假设配置缓存KEY

        try {
            // 1. 先查询Redis缓存
            CommonVo06 cachedResult = redisJsonUtil.get(redisKey, CommonVo06.class);
            if (cachedResult != null) {
                logger.info("从Redis缓存获取配置成功，key: {}", redisKey);
                return cachedResult;
            }
        } catch (Exception e) {
            // 缓存查询异常不中断流程，仅记录日志
            logger.error("Redis缓存查询异常，key: {}", redisKey, e);
        }

        // 2. 缓存不存在或异常，查询数据库并构建结果
        Map<ChatConfigEnum, ChatConfig> dataMap = chatConfigService.queryConfig();
        //logger.info("从数据库获取配置成功，Value: {}", dataMap);
        CommonVo06 result = buildConfigResult(dataMap);
        //logger.info("从数据库获取配置成功，Value: {}", result);
        // 3. 将结果存入Redis缓存（设置过期时间，如5分钟）
        try {
            redisJsonUtil.set(redisKey, result, 600L, TimeUnit.SECONDS); // 300秒=5分钟，可按业务调整
            logger.info("配置信息写入Redis缓存，key: {}", redisKey);
        } catch (Exception e) {
            // 缓存写入失败不影响返回结果
            logger.error("Redis缓存写入异常，key: {}", redisKey, e);
        }

        return result;
    }

    /**
     * 抽取构建结果的逻辑为独立方法，使代码更清晰
     */
    private CommonVo06 buildConfigResult(Map<ChatConfigEnum, ChatConfig> dataMap) {
        CommonVo06 result = new CommonVo06()
                .setSharePath(dataMap.get(ChatConfigEnum.SYS_SHARE).getStr())
                .setPacket(dataMap.get(ChatConfigEnum.SYS_PACKET).getBigDecimal())
                .setGroupSearch(dataMap.get(ChatConfigEnum.GROUP_NAME_SEARCH).getYesOrNo())
                .setHoldCard(dataMap.get(ChatConfigEnum.USER_HOLD).getYesOrNo())
                .setBeian(dataMap.get(ChatConfigEnum.SYS_BEIAN).getStr())
                .setMessageLimit(AppConstants.MESSAGE_LIMIT - 50)
                .setInvo(dataMap.get(ChatConfigEnum.SYS_INVO).getBigDecimal().doubleValue())
                .setSign(dataMap.get(ChatConfigEnum.SYS_SIGN).getBigDecimal().doubleValue())
                .setSigntoal(dataMap.get(ChatConfigEnum.SYS_SIGNTOAL).getYesOrNo())
                .setCashname(dataMap.get(ChatConfigEnum.SYS_CASHNAME).getStr())
                .setCashstr(dataMap.get(ChatConfigEnum.SYS_CASHSTR).getStr())
                ;


        // 处理音视频配置
        if (YesOrNoEnum.YES.getCode().equals(rtcConfig.getEnabled())) {
            result.setCallKit(rtcConfig.getAppId());
        } else {
            result.setCallKit(null); // 显式设置为null
        }

        // 处理水印、截屏等条件字段
        if (!ShiroUtils.getPhone().equals(dataMap.get(ChatConfigEnum.SYS_PHONE).getStr())) {
            result.setWatermark(dataMap.get(ChatConfigEnum.SYS_WATERMARK).getStr())
                    .setScreenshot(dataMap.get(ChatConfigEnum.SYS_SCREENSHOT).getYesOrNo())
                    .setNotice(getNotice(dataMap));
        } else {
            // 确保这些字段总是被设置
            result.setWatermark(null);
            result.setScreenshot(null);
            result.setNotice(null);
        }

        return result;
    }

    /**
     * 获取公告
     */
    private String getNotice(Map<ChatConfigEnum, ChatConfig> dataMap) {
        String content = null;
        if (redisUtils.hasKey(AppConstants.REDIS_CHAT_NOTICE)) {
            logger.info("获取缓存浮动公告，key: {}", AppConstants.REDIS_CHAT_NOTICE);
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
