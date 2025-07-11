package com.platform.common.config;

import com.platform.common.enums.YesOrNoEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformConfig {

    /**
     * 上传路径
     */
    public static String ROOT_PATH;

    /**
     * 超时时间（天）
     */
    public static Integer TIMEOUT;

    /**
     * 消息秘钥
     */
    public static String SECRET;

    /**
     * 敏感词过滤
     */
    public static boolean FILTER;

    /**
     * email短信
     */
    public static boolean EMAIL;

    public void setTimeout(Integer timeout) {
        PlatformConfig.TIMEOUT = timeout;
    }

    public void setRootPath(String rootPath) {
        PlatformConfig.ROOT_PATH = rootPath;
    }

    public void setSecret(String secret) {
        PlatformConfig.SECRET = secret;
    }

    public void setFilter(String filter) {
        PlatformConfig.FILTER = YesOrNoEnum.YES.getCode().equalsIgnoreCase(filter);
    }

    public void setEmail(String email) {
        PlatformConfig.EMAIL = YesOrNoEnum.YES.getCode().equalsIgnoreCase(email);
    }

}