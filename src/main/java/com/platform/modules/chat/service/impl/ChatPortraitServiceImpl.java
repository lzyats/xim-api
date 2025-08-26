package com.platform.modules.chat.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatPortraitDao;
import com.platform.modules.chat.domain.ChatPortrait;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatPortraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 聊天头像 服务层实现
 * </p>
 */
@Service("chatPortraitService")
public class ChatPortraitServiceImpl extends BaseServiceImpl<ChatPortrait> implements ChatPortraitService {

    @Resource
    private ChatPortraitDao chatPortraitDao;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    String redisKey = AppConstants.REDIS_COMMON_PORTRAIT + ":list";
    Long CACHE_TIMEOUT=300L;
    TimeUnit CACHE_TIME_UNIT=TimeUnit.SECONDS;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatPortraitDao);
    }

    @Override
    public List<ChatPortrait> queryList(ChatPortrait t) {
        List<ChatPortrait> dataList = chatPortraitDao.queryList(t);
        return dataList;
    }

    @Override
    public String queryGroupPortrait() {
        return this. queryPortrait(ChatTalkEnum.GROUP);
    }

    @Override
    public String queryUserPortrait() {
        return this. queryPortrait(ChatTalkEnum.FRIEND);
    }

    private String queryPortrait(ChatTalkEnum chatType) {
        ChatPortrait query = new ChatPortrait(chatType);
        List<ChatPortrait> dataList = this.queryList(query);
        // 随机排序
        Collections.shuffle(dataList);
        // 取第一条
        return dataList.get(0).getPortrait();
    }

    @Override
    public List<String> queryPortraitList(ChatTalkEnum chatType) {
        redisKey=redisKey+":"+chatType.getType();
        // 1. 先检查缓存是否存在
        if (redisJsonUtil.hasKey(redisKey)) {
            // 2. 缓存存在：调用RedisJsonUtil的range方法获取整个List（start=0，end=-1表示全部元素）
            List<String> portraitList = redisJsonUtil.range(redisKey, 0, -1,String.class);
            // 防止缓存中存储空列表，返回空集合而非null
            return portraitList != null ? portraitList : Collections.emptyList();
        }

        // 3. 缓存不存在：从数据库查询数据
        List<String> portraitList = chatPortraitDao.queryPortraits(chatType.getCode());

        // 4. 数据库查询结果非空：写入Redis缓存（使用rightPushAll批量追加到List尾部）
        if (!CollectionUtils.isEmpty(portraitList)) {
            // 循环插入每个元素
            for (String robot : portraitList) {
                // 此处不设置过期时间，避免重复设置，最后统一设置
                redisJsonUtil.leftPush(redisKey, robot, null, null);
            }
            // 统一设置过期时间（5分钟）
            redisJsonUtil.expire(redisKey, CACHE_TIMEOUT, CACHE_TIME_UNIT);
        }

        // 5. 返回结果（空结果返回空集合，避免后续调用报错）
        return CollectionUtils.isEmpty(portraitList) ? Collections.emptyList() : portraitList;
    }
}
