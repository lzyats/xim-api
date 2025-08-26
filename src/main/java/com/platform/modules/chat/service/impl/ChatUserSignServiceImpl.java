package com.platform.modules.chat.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.chat.vo.ChatSignVo01;
import com.platform.modules.chat.vo.MineSignVo01;
import com.platform.modules.common.service.CommonService;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletTradeService;
import com.platform.modules.common.vo.CommonVo06;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatUserSignService;
import com.platform.modules.chat.dao.ChatUserSignDao;
import com.platform.modules.chat.domain.ChatUserSign;
import org.springframework.util.CollectionUtils;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Date;
import java.util.HashMap;  // 导入HashMap


import java.util.Locale;
import java.math.BigDecimal;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.ZonedDateTime;

import java.util.concurrent.TimeUnit;
import com.alibaba.fastjson.TypeReference;

/**
 * <p>
 * 用户按天签到记录 服务层实现
 * </p>
 */
@Service("chatUserSignService")
public class ChatUserSignServiceImpl extends BaseServiceImpl<ChatUserSign> implements ChatUserSignService {

    @Resource
    private ChatUserSignDao chatUserSignDao;

    @Resource
    private CommonService commonService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    @Resource
    private WalletTradeService  walletTradeService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserSignDao);
    }

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

    @Override
    public List<ChatUserSign> queryList(ChatUserSign t) {
        List<ChatUserSign> dataList = chatUserSignDao.queryList(t);
        return dataList;
    }


    public List<ChatSignVo01> queryList1(QueryWrapper<ChatSignVo01> t) {
        List<ChatSignVo01> dataList = chatUserSignDao.queryList1(t);
        return dataList;
    }

    @Override
    public PageInfo getSignList() {
        // 查询数据
        QueryWrapper<ChatUserSign> wrapper = new QueryWrapper();
        wrapper.eq(ChatUserSign.COLUMN_USER_ID, ShiroUtils.getUserId());
        wrapper.eq(ChatUserSign.COLUMN_SIGN_TYPE, 1);
        wrapper.eq(ChatUserSign.COLUMN_IS_VALID, 1);
        wrapper.ge(ChatUserSign.COLUMN_CREATE_TIME, DateUtil.offsetMonth(DateUtil.date(), -3));

        List<ChatUserSign> dataList = this.queryList(wrapper);
        logger.error("查询签到数据: {}", dataList);
        List<MineSignVo01> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new MineSignVo01(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    /**
     * * 查询指定条件下的reward_amount总和和sign_date集合
     * @return 包含总和和日期集合的
     */
    @Override
    public Map<String, Object> getSignStats() {
        Long userId = ShiroUtils.getUserId();
        String redisKey = AppConstants.REDIS_MINE_SIGN +"li"+ userId;

        // 1. 尝试从缓存获取数据
        Map<String, Object> cacheResult = redisJsonUtil.get(redisKey, new TypeReference<Map<String, Object>>() {});
        if (cacheResult != null) {
            //logger.info("从缓存获取用户[{}]的签到统计数据 {}", userId,cacheResult);
            return cacheResult;
        }

        // 2. 缓存未命中，查询数据库
        QueryWrapper<ChatSignVo01> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatUserSign.COLUMN_USER_ID, userId);
        wrapper.eq(ChatUserSign.COLUMN_SIGN_TYPE, 1);
        wrapper.eq(ChatUserSign.COLUMN_IS_VALID, 1);

        double totalReward = 0.00;
        List<ChatSignVo01> signList = this.queryList1(wrapper);
        //logger.error("查询签到数据: {}", signList);

        if (!signList.isEmpty()) {
            // 计算奖励总和（使用Java 8 Stream API）
            totalReward = signList.stream()
                    .map(ChatSignVo01::getRewardAmount)
                    .filter(amount -> amount != null)
                    .mapToDouble(Double::doubleValue)
                    .sum();
        }
        // 计算连续签到天数
        int continuousDays = getContinuousSignDays(userId);

        // 计算签到奖励
        double reward = calculateReward(continuousDays + 1);
        Map<String, Object> result = new HashMap<>();
        result.put("totalReward", totalReward);
        result.put("reward", reward);
        result.put("signDates", signList);

        // 3. 将查询结果存入缓存，设置过期时间为24小时（可根据业务需求调整）
        redisJsonUtil.set(redisKey, result, 300L, TimeUnit.SECONDS);
        //logger.info("将用户[{}]的签到统计数据 {} 存入缓存，过期时间300秒", userId,result);
        return result;
    }


    /**
     * 签到
     * @return 包含总和和日期集合的Map
     */
    @Override
    public Map<String, Object> sign() {
        Long userId = ShiroUtils.getUserId();
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        // 获取当前东8区时间的当天0点
        ZonedDateTime todayStart = ZonedDateTime.now(zoneId)
                .toLocalDate()
                .atStartOfDay(zoneId);
        // 转换为Date
        Date today = Date.from(todayStart.toInstant());
        //Date today = DateUtil.beginOfDay(new Date()); // 获取今天0点时间
        logger.error("开始签到: userId{},today{}", userId,today);
        // 1. 检查用户今天是否已签到
        String redisKey = AppConstants.REDIS_MINE_SIGN + userId;

        try {
            String cachedJson = redisUtils.get(redisKey);
            if (cachedJson != null && !cachedJson.isEmpty()) {
                throw new BaseException("你今天已经签过到了");
            }
        } catch (Exception e) {
            logger.error("Redis缓存解析失败，清除缓存，key: {}", redisKey, e);
            redisUtils.delete(redisKey); // 自动清除异常缓存
        }
        QueryWrapper<ChatUserSign> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq(ChatUserSign.COLUMN_USER_ID, userId)
                .ge("sign_date", today)
                .last("limit 1");
        ChatUserSign existingSign = this.queryOne(checkWrapper);
        if (existingSign != null) {
            // 序列化并缓存（确保格式正确）
            try {
                // 生成纯净的JSON对象字符串（无额外引号）
                String jsonStr = "1";
                logger.debug("写入Redis的JSON: {}", jsonStr); // 调试：确认格式正确
                redisUtils.set(redisKey, jsonStr, 12,TimeUnit.HOURS);
            } catch (Exception e) {
                logger.error("写入Redis缓存失败，key: {}", redisKey, e);
            }
            throw new BaseException("你今天已经签过到了");
        }
        //清除查询缓存
        String redisKeylist = AppConstants.REDIS_MINE_SIGN +"li"+ userId;
        redisJsonUtil.delete(redisKeylist);
        // 2. 计算连续签到天数
        int continuousDays = getContinuousSignDays(userId);

        // 3. 计算签到奖励
        double reward = calculateReward(continuousDays + 1);

        // 实际签到奖励
        CommonVo06 vo06= commonService.getConfig();
        String dotal=vo06.getSigntoal().getCode();
        reward=reward+vo06.getSign();
        Long tradeId=null;
        Long Signid=IdWorker.getId();
        // 将签到记录奖励记入总账
        if(dotal=="Y"){
            tradeId = IdWorker.getId();
            WalletTrade trade = walletTradeService.addTrade(tradeId, TradeTypeEnum.SIGN, BigDecimal.valueOf(reward), "lz88888888", "每日签到");
            trade.setTradeAmount(NumberUtil.add(BigDecimal.ZERO, reward));
            trade.setSourceId(Signid);
            trade.setSourceType(TradeTypeEnum.SIGN);
            walletTradeService.add(trade);
        }
        // 4. 创建新签到记录

        ChatUserSign newSign = new ChatUserSign()
                .setSignid(Signid)
                .setUserId(userId)
                .setSignDate(today)
                .setTradeId(tradeId)
                .setSignType(true)  // 1-正常签到
                .setIsValid(true)   // 1-有效
                .setCreateTime(new Date())
                .setUpdateTime(new Date())
                .setRewardAmount(reward);

        // 5. 保存签到记录
        this.add(newSign);

        // 6. 构建返回结果
        Map<String, Object> result = this.getSignStats();
        //result.put("currentReward", reward);
        //result.put("continuousDays", continuousDays + 1);
        //result.put("todaySigned", true);

        return result;
    }

    /**
     * 获取用户连续签到天数
     */
    private int getContinuousSignDays(Long userId) {
        // 查询用户有效签到记录，按日期倒序排列
        QueryWrapper<ChatUserSign> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatUserSign.COLUMN_USER_ID, userId)
                .eq("is_valid", true)
                .orderByDesc("sign_date");

        List<ChatUserSign> signList = this.queryList(wrapper);
        if (CollectionUtils.isEmpty(signList)) {
            return 0;
        }

        int continuousDays = 0;
        DateTime yesterday = new DateTime().minusDays(1).toDateMidnight().toDateTime();

        // 检查最近的签到记录是否是昨天
        DateTime lastSignDate = new DateTime(signList.get(0).getSignDate()).toDateMidnight().toDateTime();
        if (lastSignDate.equals(yesterday)) {
            continuousDays++;

            // 继续检查更早的记录是否连续
            for (int i = 1; i < signList.size(); i++) {
                DateTime currentSignDate = new DateTime(signList.get(i).getSignDate()).toDateMidnight().toDateTime();
                yesterday = yesterday.minusDays(1);

                if (currentSignDate.equals(yesterday)) {
                    continuousDays++;
                } else {
                    break;
                }
            }
        }

        return continuousDays;
    }

    /**
     * 计算签到奖励
     */
    private double calculateReward(int continuousDays) {
        // 奖励规则可根据实际业务调整
        if (continuousDays >= 7) {
            return 0.35;
        } else if (continuousDays >= 6) {
            return 0.25;
        } else if (continuousDays >= 5) {
            return 0.2;
        } else if (continuousDays >= 4) {
            return 0.15;
        } else if (continuousDays >= 3) {
            return 0.1;
        } else if (continuousDays >= 2) {
            return 0.05;
        } else {
            return 0.00;
        }
    }

}
