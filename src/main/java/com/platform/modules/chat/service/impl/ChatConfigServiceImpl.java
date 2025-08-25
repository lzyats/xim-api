package com.platform.modules.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatConfigDao;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.platform.common.constant.AppConstants;


/**
 * <p>
 * 设置表 服务层实现
 * </p>
 */
@Slf4j
@Service("chatConfigService")
public class ChatConfigServiceImpl extends BaseServiceImpl<ChatConfig> implements ChatConfigService {

    @Resource
    private ChatConfigDao chatConfigDao;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    // 缓存键（固定为AppConstants.REDIS_COMMON_CONFIG）
    private static final String REDIS_KEY = AppConstants.REDIS_COMMON_CONFIG+"all";
    // 缓存过期时间（24小时，可根据需求调整）
    private static final long CACHE_EXPIRE = 300;
    private static final TimeUnit CACHE_TIME_UNIT = TimeUnit.SECONDS;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatConfigDao);
    }

    @Override
    public List<ChatConfig> queryList(ChatConfig t) {
        List<ChatConfig> dataList = chatConfigDao.queryList(t);
        return dataList;
    }

    @Override
    public Map<ChatConfigEnum, ChatConfig> queryConfig() {
        // 1. 从缓存获取Hash表（此时value是Map<String, Object>，而非直接的ChatConfig）
        Map<String, Map<String, Object>> cacheMap = redisJsonUtil.hgetAllMap(REDIS_KEY);
        if (cacheMap != null && !cacheMap.isEmpty()) {
            Map<ChatConfigEnum, ChatConfig> resultMap = new HashMap<>(cacheMap.size());
            cacheMap.forEach((key, configMap) -> {
                try {
                    // 转换key为ChatConfigEnum
                    ChatConfigEnum configEnum = ChatConfigEnum.valueOf(key);
                    // 将Map<String, Object>转换为ChatConfig对象
                    ChatConfig chatConfig = new ChatConfig();
                    // 设置核心字段：configKey（枚举）和configValue（原始值）
                    chatConfig.setConfigKey(configEnum);
                    // 从map中获取configValue（存入时应包含该字段，对应ChatConfig的configValue）
                    if (configMap.containsKey("configValue")) {
                        chatConfig.setConfigValue(configMap.get("configValue").toString());
                    }
                    resultMap.put(configEnum, chatConfig);
                } catch (IllegalArgumentException e) {
                    // 忽略无效枚举值
                    log.warn("无效的ChatConfigEnum枚举值：{}", key, e);
                } catch (Exception e) {
                    // 忽略转换异常
                    log.warn("缓存转换ChatConfig失败，key:{}", key, e);
                }
            });
            //log.info("<UNK>ChatConfig<UNK>{}", resultMap);
            return resultMap;
        }

        // 2. 缓存不存在，查询数据库
        List<ChatConfig> dataList = chatConfigDao.queryList(new ChatConfig());
        Map<ChatConfigEnum, ChatConfig> dbMap = dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getConfigKey(), y);
        }, HashMap::putAll);

        // 3. 写入缓存（Hash表，字段为ChatConfigEnum的name，值为ChatConfig对象）
        Map<String,Map<String, Object> > redisDataMap = new HashMap<>(dbMap.size());
        //log.info("原始值：{}",dbMap);
        dbMap.forEach((enumKey, config) -> {
            redisDataMap.put(enumKey.name(), config.toMap());
        });
        redisJsonUtil.hmset(REDIS_KEY, redisDataMap, CACHE_EXPIRE, CACHE_TIME_UNIT);

        return dbMap;
    }

    @Override
    public ChatConfig queryConfig(ChatConfigEnum configKey) {
        // 1. 先从缓存Hash表中查询指定字段
        if (redisJsonUtil.hexists(REDIS_KEY, configKey.name())) {
            // 读取缓存中的Map<String, Object>
            Map<String, Object> configMap = redisJsonUtil.hget(REDIS_KEY, configKey.name(), Map.class);
            if (configMap != null) {
                try {
                    // 将Map转换为ChatConfig对象
                    ChatConfig chatConfig = new ChatConfig();
                    chatConfig.setConfigKey(configKey);
                    if (configMap.containsKey("configValue")) {
                        chatConfig.setConfigValue(configMap.get("configValue").toString());
                    }
                    return chatConfig;
                } catch (Exception e) {
                    log.error("缓存转换ChatConfig失败，key:{}", configKey.name(), e);
                    redisJsonUtil.hdel(REDIS_KEY, configKey.name());
                }
            }
        }

        // 2. 缓存不存在时，调用全量查询方法（该方法会加载所有配置到缓存）
        Map<ChatConfigEnum, ChatConfig> allConfigMap = queryConfig();
        // 3. 从全量结果中获取当前需要的配置
        ChatConfig chatConfig = allConfigMap.get(configKey);
        if (chatConfig == null) {
            throw new BaseException("配置不存在，请检查配置");
        }
        return chatConfig;
    }


}
