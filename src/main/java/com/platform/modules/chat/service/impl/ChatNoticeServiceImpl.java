package com.platform.modules.chat.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatNoticeDao;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import com.platform.modules.common.vo.CommonVo04;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("chatNoticeService")
public class ChatNoticeServiceImpl extends BaseServiceImpl<ChatNotice> implements ChatNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

    @Resource
    private ChatNoticeDao chatNoticeDao;

    @Autowired
    private RedisUtils redisUtils;

    // 初始化Gson（禁用HTML转义，避免额外字符）
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .disableHtmlEscaping() // 关键：避免特殊字符被转义
            .serializeNulls()
            .create();

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatNoticeDao);
    }

    @Override
    public List<ChatNotice> queryList(ChatNotice t) {
        return chatNoticeDao.queryList(t);
    }

    @Override
    public PageInfo<CommonVo04> queryDataList() {
        String redisKey = AppConstants.REDIS_COMMON_NOTIC + "list";

        try {
            String cachedJson = redisUtils.get(redisKey);
            if (cachedJson != null && !cachedJson.isEmpty()) {
                // 清理可能的特殊字符（如前后空格、引号）
                cachedJson = cleanJsonString(cachedJson);

                // 解析为PageInfo对象
                Type pageInfoType = new TypeToken<PageInfo<CommonVo04>>() {}.getType();
                PageInfo<CommonVo04> cachedPageInfo = gson.fromJson(cachedJson, pageInfoType);

                logger.info("从Redis缓存获取数据，key: {}", redisKey);
                return cachedPageInfo;
            }
        } catch (Exception e) {
            logger.error("Redis缓存解析失败，清除缓存，key: {}", redisKey, e);
            redisUtils.delete(redisKey); // 自动清除异常缓存
        }

        // 数据库查询
        ChatNotice query = new ChatNotice().setStatus(YesOrNoEnum.YES);
        List<ChatNotice> dataList = queryList(query);

        // 转换为VO
        List<CommonVo04> resultList = dataList.stream()
                .filter(Objects::nonNull)
                .map(CommonVo04::new)
                .collect(Collectors.toList());

        // 构建分页信息
        PageInfo<CommonVo04> pageInfo = new PageInfo<>(resultList);
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(resultList.size());
        pageInfo.setTotal(resultList.size());
        pageInfo.setPages(1);

        // 序列化并缓存（确保格式正确）
        try {
            // 生成纯净的JSON对象字符串（无额外引号）
            String jsonStr = gson.toJson(pageInfo);
            logger.debug("写入Redis的JSON: {}", jsonStr); // 调试：确认格式正确
            redisUtils.set(redisKey, jsonStr, 120);
        } catch (Exception e) {
            logger.error("写入Redis缓存失败，key: {}", redisKey, e);
        }

        return pageInfo;
    }

    /**
     * 清理JSON字符串中的非法字符（解决核心错误）
     */
    private String cleanJsonString(String json) {
        if (json == null) {
            return null;
        }
        // 1. 去除前后空格
        json = json.trim();
        // 2. 去除首尾可能的引号（例如："{...}" → {...}）
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);
        }
        // 3. 去除可能的转义字符（例如：\\" → "）
        json = json.replace("\\\"", "\"");
        return json;
    }
}