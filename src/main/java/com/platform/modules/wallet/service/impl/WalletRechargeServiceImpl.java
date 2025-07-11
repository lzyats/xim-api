package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.constant.HeadConstant;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.DeviceEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.EncryptUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.common.service.HookService;
import com.platform.modules.pay.service.PayAliService;
import com.platform.modules.pay.service.PayWxService;
import com.platform.modules.pay.vo.NotifyVo;
import com.platform.modules.pay.vo.PayVo;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.dao.WalletRechargeDao;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletConfigService;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.service.WalletTradeService;
import com.platform.modules.wallet.vo.WalletVo01;
import com.platform.modules.wallet.vo.WalletVo03;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 钱包充值 服务层实现
 * </p>
 */
@Service("walletRechargeService")
public class WalletRechargeServiceImpl extends BaseServiceImpl<WalletRecharge> implements WalletRechargeService {

    @Resource
    private WalletRechargeDao walletRechargeDao;

    @Resource
    private WalletConfigService walletConfigService;

    @Resource
    private PushService pushService;

    @Resource
    private HookService hookService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private PayAliService payAliService;

    @Resource
    private PayWxService payWxService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletRechargeDao);
    }

    @Override
    public List<WalletRecharge> queryList(WalletRecharge t) {
        List<WalletRecharge> dataList = walletRechargeDao.queryList(t);
        return dataList;
    }

    @Override
    public WalletVo03 getConfig() {
        // 当前时间
        Date now = DateUtil.date();
        // 单笔数验证
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        Long total = configMap.get(ChatConfigEnum.WALLET_RECHARGE_COUNT).getLong();
        QueryWrapper<WalletRecharge> wrapper = new QueryWrapper();
        wrapper.eq(WalletRecharge.COLUMN_USER_ID, ShiroUtils.getUserId());
        wrapper.between(WalletCash.COLUMN_CREATE_TIME, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        Long count = total - this.queryCount(wrapper);
        // 充值时间
        String startTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_START).getStr();
        String endTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_END).getStr();
        return new WalletVo03()
                .setCount(count.intValue() < 0 ? 0 : count.intValue())
                .setRemark(StrUtil.format("充值时间：{}~{}", startTime, endTime))
                ;
    }

    @Override
    public List<BigDecimal> getPayAmount() {
        // 查询
        List<BigDecimal> dataList = walletConfigService.queryList();
        // 顺序
        Collections.sort(dataList);
        return dataList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getPayType() {
        // 请求
        HttpServletRequest request = ServletUtils.getRequest();
        // 设备
        String device = request.getHeader(HeadConstant.DEVICE);
        DeviceEnum deviceEnum = EnumUtils.toEnum(DeviceEnum.class, device);
        // 数据
        List<TradePayEnum> payTypeList;
        switch (deviceEnum) {
            case ANDROID:
                payTypeList = this.getPayType(ChatConfigEnum.WALLET_RECHARGE_ANDROID);
                break;
            case IOS:
                payTypeList = this.getPayType(ChatConfigEnum.WALLET_RECHARGE_IOS);
                break;
            default:
                payTypeList = new ArrayList<>();
        }
        List<String> dataList = new ArrayList<>();
        // 验证
        if (CollectionUtils.isEmpty(payTypeList)) {
            return dataList;
        }
        payTypeList.forEach(data -> {
            dataList.add(data.getCode());
        });
        return dataList;
    }

    private List<TradePayEnum> getPayType(ChatConfigEnum configKey) {
        // 查询
        ChatConfig chatConfig = chatConfigService.queryConfig(configKey);
        List<TradePayEnum> dataList = new ArrayList<>();
        if (StringUtils.isEmpty(chatConfig.getStr())) {
            return dataList;
        }
        List<String> splitList = StrUtil.split(chatConfig.getStr(), ',');
        for (TradePayEnum payType : TradePayEnum.values()) {
            if (splitList.contains(payType.getCode())) {
                dataList.add(payType);
            }
        }
        return dataList;
    }

    @Transactional
    @Override
    public String submit(WalletVo01 walletVo) {
        // 当前时间
        Date now = DateUtil.date();
        // 获取配置
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 时间判断
        String today = DateUtil.format(DateUtil.date(), DatePattern.NORM_TIME_PATTERN);
        String startTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_START).getStr();
        String endTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_END).getStr();
        if (today.compareTo(startTime) < 0 || today.compareTo(endTime) > 0) {
            throw new BaseException(StrUtil.format("充值时间：{}~{}", startTime, endTime));
        }
        // 单笔数验证
        int count = configMap.get(ChatConfigEnum.WALLET_RECHARGE_COUNT).getInt();
        QueryWrapper<WalletRecharge> wrapper = new QueryWrapper();
        wrapper.eq(WalletRecharge.COLUMN_USER_ID, ShiroUtils.getUserId());
        wrapper.between(WalletCash.COLUMN_CREATE_TIME, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        if (this.queryCount(wrapper) + 1 > count) {
            throw new BaseException("充值失败：您当日充值次数已达最高限制");
        }
        // 总金额验证
        BigDecimal decimal = configMap.get(ChatConfigEnum.WALLET_RECHARGE_AMOUNT).getBigDecimal();
        wrapper = new QueryWrapper();
        wrapper.select("SUM(amount) AS 'amount'");
        wrapper.between(WalletCash.COLUMN_CREATE_TIME, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        WalletRecharge recharge = this.queryOne(wrapper);
        if (recharge != null && recharge.getAmount().compareTo(decimal) > 0) {
            throw new BaseException("充值失败：当日总充值金额已达最高限制");
        }
        // 生成订单
        BigDecimal amount = walletVo.getAmount();
        TradePayEnum payType = walletVo.getPayType();
        String tradeNo = IdWorker.get32UUID();
        TradeTypeEnum tradeType = TradeTypeEnum.RECHARGE;
        WalletTrade trade = new WalletTrade()
                .setTradeId(IdWorker.getId())
                .setTradeType(tradeType)
                .setTradeAmount(amount)
                .setTradeRemark(tradeType.getInfo())
                .setUserId(ShiroUtils.getUserId())
                .setUserNo(ShiroUtils.getUserNo())
                .setNickname(ShiroUtils.getNickname())
                .setPhone(ShiroUtils.getPhone())
                .setPortrait(ShiroUtils.getPortrait())
                .setTradeStatus(ApproveEnum.PASS)
                .setCreateTime(now);
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_RECHARGE, tradeNo);
        redisUtils.set(redisKey, JSONUtil.toJsonStr(trade), 30, TimeUnit.DAYS);
        // 生成订单
        String result;
        PayVo payVo = new PayVo()
                .setBody(StrUtil.format("余额充值({})", ShiroUtils.getPhone()))
                .setTradeNo(tradeNo)
                .setAmount(amount);
        switch (payType) {
            case ALI_PAY:
                result = payAliService.createOrder(payVo, "/wallet/recharge/notifyAli");
                break;
            case WX_PAY:
                result = payWxService.createOrder(payVo, "/wallet/recharge/notifyWx");
                break;
            default:
                throw new BaseException("暂不支持其他支付方式");
        }
        return EncryptUtils.encrypt(result, PlatformConfig.SECRET);
    }

    @Transactional
    @Override
    public String notify(HttpServletRequest request, TradePayEnum payType) {
        NotifyVo notifyVo;
        switch (payType) {
            case ALI_PAY:
                notifyVo = payAliService.notify(request);
                break;
            case WX_PAY:
                notifyVo = payWxService.notify(request);
                break;
            default:
                throw new BaseException("暂不支持其他支付方式");
        }
        if (!notifyVo.isResult()) {
            return notifyVo.getResultLabel();
        }
        String tradeNo = notifyVo.getTradeNo();
        String orderNo = notifyVo.getOrderNo();
        // 校验订单
        this.addRecharge(tradeNo, orderNo, payType);
        return notifyVo.getResultLabel();
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<WalletRecharge>lambdaUpdate()
                .set(WalletRecharge::getNickname, nickname)
                .eq(WalletRecharge::getUserId, current));
    }

    /**
     * 支付数据
     */
    private void addRecharge(String tradeNo, String orderNo, TradePayEnum payType) {
        // 校验订单
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_RECHARGE, tradeNo);
        if (!redisUtils.hasKey(redisKey)) {
            return;
        }
        WalletTrade walletTrade = JSONUtil.toBean(redisUtils.get(redisKey), WalletTrade.class);
        Long userId = walletTrade.getUserId();
        BigDecimal amount = walletTrade.getTradeAmount();
        // 更新余额
        BigDecimal walletBalance = walletInfoService.addBalance(userId, amount);
        // 写入账单
        walletTrade.setUpdateTime(DateUtil.date());
        walletTrade.setTradeBalance(walletBalance);
        walletTradeService.add(walletTrade);
        // 写入充值
        WalletRecharge recharge = new WalletRecharge()
                .setTradeId(walletTrade.getTradeId())
                .setUserId(userId)
                .setUserNo(walletTrade.getUserNo())
                .setPhone(walletTrade.getPhone())
                .setNickname(walletTrade.getNickname())
                .setPayType(payType)
                .setAmount(amount)
                .setTradeNo(tradeNo)
                .setOrderNo(orderNo)
                .setCreateTime(walletTrade.getCreateTime())
                .setUpdateTime(DateUtil.date());
        this.add(recharge);
        // 移除缓存
        redisUtils.delete(redisKey);
        // 推送
        ThreadUtil.execAsync(() -> {
            // 推送
            PushSetting setting = new PushSetting(PushSettingEnum.SYS, "balance", walletBalance.setScale(2).toString());
            pushService.pushSetting(setting, Arrays.asList(userId));
            // 通知
            hookService.handle(recharge);
        });
    }

}
