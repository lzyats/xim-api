package com.platform.modules.chat.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatNumberDao;
import com.platform.modules.chat.domain.ChatNumber;
import com.platform.modules.chat.service.ChatNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 微聊号码 服务层实现
 * </p>
 */
@Service("chatNumberService")
public class ChatNumberServiceImpl extends BaseServiceImpl<ChatNumber> implements ChatNumberService {

    @Resource
    private ChatNumberDao chatNumberDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatNumberDao);
    }

    @Override
    public List<ChatNumber> queryList(ChatNumber t) {
        List<ChatNumber> dataList = chatNumberDao.queryList(t);
        return dataList;
    }

    @Override
    public String queryNextNo() {
        if (redisUtils.lSize(AppConstants.REDIS_CHAT_NO) < 10) {
            initChatNo();
        }
        String chatNo = redisUtils.lLeftPop(AppConstants.REDIS_CHAT_NO);
        if (StringUtils.isEmpty(chatNo)) {
            return queryNextNo();
        }
        Integer result = this.updateById(new ChatNumber(chatNo));
        if (result == 0) {
            return queryNextNo();
        }
        return chatNo;
    }

    /**
     * 初始化ChatNo
     */
    private void initChatNo() {
        // 查询数据库数量
        if (queryCount(new ChatNumber()) < 50) {
            // 生成数据
            calculateData();
        }
        // 执行分页
        PageHelper.startPage(1, 100);
        // 查询数据
        List<ChatNumber> dataList = queryList(new ChatNumber());
        // 集合取属性
        List<String> valueList = dataList.stream().map(ChatNumber::getChatNo).collect(Collectors.toList());
        redisUtils.lRightPushAll(AppConstants.REDIS_CHAT_NO, valueList, 30, TimeUnit.DAYS);
    }

    /**
     * 计算数据
     */
    private void calculateData() {
        try {
            List<ChatNumber> dataList = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                Integer chatId = RandomUtil.randomInt(10000000, 100000000);
                dataList.add(new ChatNumber().setChatNo(NumberUtil.toStr(chatId)));
            }
            this.batchAdd(dataList);
        } catch (Exception e) {
            calculateData();
        }
    }

}
