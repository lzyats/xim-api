package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.quartz.service.QuartzJobService;
import com.platform.modules.wallet.dao.WalletReceiveDao;
import com.platform.modules.wallet.domain.WalletReceive;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.service.WalletReceiveService;
import com.platform.modules.wallet.service.WalletTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 钱包余额 服务层实现
 * </p>
 */
@Slf4j
@Service("walletReceiveService")
public class WalletReceiveServiceImpl extends BaseServiceImpl<WalletReceive> implements WalletReceiveService {

    @Resource
    private WalletReceiveDao walletReceiveDao;

    @Lazy
    @Resource
    private WalletTradeService walletTradeService;

    @Lazy
    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private QuartzJobService quartzJobService;

    @Resource
    private WalletReceiveService walletReceiveService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletReceiveDao);
    }

    @Override
    public List<WalletReceive> queryList(WalletReceive t) {
        List<WalletReceive> dataList = walletReceiveDao.queryList(t);
        return dataList;
    }

    @Override
    public void addReceive(Long tradeId, Long userId, BigDecimal amount) {
        WalletReceive receive = new WalletReceive()
                .setTradeId(tradeId)
                .setUserId(userId)
                .setAmount(amount)
                .setStatus(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        this.add(receive);
        // 定时任务
        quartzJobService.once("钱包补偿", StrUtil.format("walletReceiveService.once('{}')", receive.getTradeId()));
    }

    @Override
    public void task() {
        // 查询列表
        WalletReceive query = new WalletReceive()
                .setStatus(YesOrNoEnum.NO);
        List<WalletReceive> dataList = this.queryList(query);
        // 执行任务
        dataList.forEach(data -> {
            try {
                walletReceiveService.doTask(data);
            } catch (Exception e) {
                log.error("定时任务[钱包补偿]执行失败", e);
            }
        });
    }

    @Transactional
    @Override
    public void doTask(WalletReceive walletReceive) {
        // 更新任务
        WalletReceive receive = new WalletReceive()
                .setTradeId(walletReceive.getTradeId())
                .setStatus(YesOrNoEnum.YES)
                .setVersion(walletReceive.getVersion())
                .setUpdateTime(DateUtil.date());
        if (this.updateById(receive) == 0) {
            return;
        }
        // 增加余额
        BigDecimal walletBalance = walletInfoService.addBalance(walletReceive.getUserId(), walletReceive.getAmount());
        // 更新交易
        WalletTrade trade = new WalletTrade()
                .setTradeId(walletReceive.getTradeId())
                .setTradeBalance(walletBalance);
        walletTradeService.updateById(trade);
    }

    @Override
    public void once(String param) {
        WalletReceive walletReceive = this.getById(NumberUtil.parseLong(param));
        if (walletReceive == null) {
            return;
        }
        if (YesOrNoEnum.YES.equals(walletReceive.getStatus())) {
            return;
        }
        ThreadUtil.execAsync(() -> {
            this.doTask(walletReceive);
        });
    }

}
