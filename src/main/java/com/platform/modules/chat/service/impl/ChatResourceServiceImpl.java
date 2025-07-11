package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatResourceDao;
import com.platform.modules.chat.domain.ChatResource;
import com.platform.modules.chat.service.ChatResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 聊天资源 服务层实现
 * </p>
 */
@Service("chatResourceService")
public class ChatResourceServiceImpl extends BaseServiceImpl<ChatResource> implements ChatResourceService {

    @Resource
    private ChatResourceDao chatResourceDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatResourceDao);
    }

    @Override
    public List<ChatResource> queryList(ChatResource t) {
        List<ChatResource> dataList = chatResourceDao.queryList(t);
        return dataList;
    }

    @Override
    public void addResource(String url) {
        // 存图片
        ChatResource resource = new ChatResource()
                .setUserId(ShiroUtils.getUserId())
                .setUrl(url)
                .setCreateTime(DateUtil.date());
        this.add(resource);
        // 存缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_PORTRAIT, url);
        redisUtils.set(redisKey, NumberUtil.toStr(resource.getResourceId()), 1, TimeUnit.HOURS);
    }

    @Override
    public void delResource(String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        // 取缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_PORTRAIT, url);
        if (!redisUtils.hasKey(redisKey)) {
            return;
        }
        // 获取资源
        String redisValue = redisUtils.get(redisKey);
        if (StringUtils.isEmpty(redisValue)) {
            return;
        }
        // 删除
        this.deleteById(NumberUtil.parseLong(redisValue));
    }

}
