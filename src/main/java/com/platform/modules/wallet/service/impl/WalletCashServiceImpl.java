package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.wallet.dao.WalletCashDao;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletCashService;
import com.platform.modules.wallet.service.WalletTradeService;
import com.platform.modules.wallet.vo.WalletVo02;
import com.platform.modules.wallet.vo.WalletVo06;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.common.constant.AppConstants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.HashMap;  // 引入 HashMap 类
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 钱包提现 服务层实现
 * </p>
 */
@Service("walletCashService")
public class WalletCashServiceImpl extends BaseServiceImpl<WalletCash> implements WalletCashService {

    @Resource
    private WalletCashDao walletCashDao;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private HookService hookService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletCashDao);
    }

    @Override
    public List<WalletCash> queryList(WalletCash t) {
        List<WalletCash> dataList = walletCashDao.queryList(t);
        return dataList;
    }

    @Override
    public WalletVo02 getConfig() {
        // 查询数据
        Map<ChatConfigEnum, ChatConfig> dataMap = chatConfigService.queryConfig();
        // 写缓存
        Integer expired = 60;
        String redisKey = AppConstants.REDIS_WALLET_ROBOT+"config";
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("wallet_cash_cost", dataMap.get(ChatConfigEnum.WALLET_CASH_COST).getBigDecimal().toString());
        hashMap.put("wallet_cash_max", dataMap.get(ChatConfigEnum.WALLET_CASH_MAX).getBigDecimal().toString());
        hashMap.put("wallet_cash_min", dataMap.get(ChatConfigEnum.WALLET_CASH_MIN).getStr());
        hashMap.put("wallet_cash_remark", dataMap.get(ChatConfigEnum.WALLET_CASH_REMARK).getStr());
        hashMap.put("wallet_cash_rate", dataMap.get(ChatConfigEnum.WALLET_CASH_RATE).getBigDecimal().toString());
        hashMap.put("wallet_cash_rates", dataMap.get(ChatConfigEnum.WALLET_CASH_RATES).getBigDecimal().toString());
        hashMap.put("wallet_cash_count", dataMap.get(ChatConfigEnum.WALLET_CASH_COUNT).getInt().toString());
        hashMap.put("wallet_cash_auth", dataMap.get(ChatConfigEnum.WALLET_CASH_AUTH).getStr());
        redisUtils.hPutAll(redisKey,hashMap,expired, TimeUnit.SECONDS);
        return new WalletVo02()
                .setCost(dataMap.get(ChatConfigEnum.WALLET_CASH_COST).getBigDecimal())
                .setMax(dataMap.get(ChatConfigEnum.WALLET_CASH_MAX).getBigDecimal())
                .setMin(dataMap.get(ChatConfigEnum.WALLET_CASH_MIN).getBigDecimal())
                .setRemark(dataMap.get(ChatConfigEnum.WALLET_CASH_REMARK).getStr())
                .setRate(dataMap.get(ChatConfigEnum.WALLET_CASH_RATE).getBigDecimal())
                .setRates(dataMap.get(ChatConfigEnum.WALLET_CASH_RATES).getBigDecimal())
                .setCount(dataMap.get(ChatConfigEnum.WALLET_CASH_COUNT).getInt())
                .setAuth(dataMap.get(ChatConfigEnum.WALLET_CASH_AUTH).getStr())
                ;
    }

    @Transactional
    @Override
    public void apply(WalletVo06 walletVo) {
        BigDecimal amount = walletVo.getAmount();
        // 查询配置
        WalletVo02 config = getConfig();
        // 验证最小金额
        if (amount.compareTo(config.getMin()) == -1) {
            throw new BaseException(StrUtil.format("提现失败：提现金额不能小于{}元", config.getMin()));
        }
        // 验证最大金额
        if (amount.compareTo(config.getMax()) == 1) {
            throw new BaseException(StrUtil.format("提现失败：提现金额不能大于{}元", config.getMax()));
        }
        // 计算手续费
        BigDecimal charge = NumberUtil.add(NumberUtil.mul(amount, config.getRate(), 0.01), config.getCost());
        if (charge.compareTo(amount) > -1) {
            throw new BaseException("提现失败：提现金额不正确");
        }
        // 验证提现次数
        Long current = ShiroUtils.getUserId();
        QueryWrapper<WalletCash> wrapper = new QueryWrapper();
        wrapper.eq(WalletCash.COLUMN_USER_ID, current);
        // 当前时间
        Date now = DateUtil.date();
        wrapper.between(WalletCash.COLUMN_CREATE_TIME, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        if (this.queryCount(wrapper) + 1 > config.getCount()) {
            throw new BaseException("提现失败：当日提现次数已超过最高限制");
        }
        // 验证实名
        if (YesOrNoEnum.YES.equals(config.getAuth())) {
            ChatUser chatUser = chatUserService.getById(current);
            if (!ApproveEnum.PASS.equals(chatUser.getAuth())) {
                throw new BaseException("提现失败：请先实名认证");
            }
        }
        // 扣减余额
        Long tradeId = IdWorker.getId();
        WalletTrade trade = walletTradeService.addTrade(tradeId, TradeTypeEnum.CASH, amount, walletVo.getPassword(), null);
        walletTradeService.add(trade);
        // 写入提现
        WalletCash cash = new WalletCash()
                .setTradeId(tradeId)
                .setUserId(current)
                .setName(walletVo.getName())
                .setWallet(walletVo.getWallet())
                .setAmount(amount)
                .setCharge(charge)
                .setRate(config.getRate())
                .setCost(config.getCost())
                .setCreateTime(now);
        this.add(cash);
        // 发送通知
        hookService.handle(PushAuditEnum.APPLY_CASH);
    }

}
