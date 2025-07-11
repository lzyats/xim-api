package com.platform.modules.wallet.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.wallet.dao.WalletTaskDao;
import com.platform.modules.wallet.domain.WalletTask;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.service.WalletPacketService;
import com.platform.modules.wallet.service.WalletTaskService;
import com.platform.modules.wallet.service.WalletTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包任务 服务层实现
 * </p>
 */
@Slf4j
@Service("walletTaskService")
public class WalletTaskServiceImpl extends BaseServiceImpl<WalletTask> implements WalletTaskService {

    @Resource
    private WalletTaskDao walletTaskDao;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletPacketService walletPacketService;

    @Resource
    private WalletInfoService walletInfoService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WalletTaskService walletTaskService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletTaskDao);
    }

    @Override
    public List<WalletTask> queryList(WalletTask t) {
        List<WalletTask> dataList = walletTaskDao.queryList(t);
        return dataList;
    }

    @Override
    public void addTask(WalletTrade trade) {
        switch (trade.getTradeType()) {
            case PACKET:
            case PACKET_ASSIGN:
            case PACKET_LUCK:
            case PACKET_NORMAL:
                break;
            default:
                return;
        }
        Date now = DateUtil.date();
        WalletTask task = new WalletTask()
                .setTradeId(trade.getTradeId())
                .setTradeType(trade.getTradeType())
                .setStatus(YesOrNoEnum.NO)
                .setCreateTime(now)
                .setTaskTime(DateUtil.offsetDay(now, 1));
        this.add(task);
    }

    @Override
    public void task() {
        // 查询列表
        WalletTask query = new WalletTask()
                .setStatus(YesOrNoEnum.NO)
                .setTaskTime(DateUtil.date());
        List<WalletTask> dataList = this.queryList(query);
        // 执行任务
        dataList.forEach(data -> {
            try {
                redisUtils.delete(StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, data.getTradeId()));
                walletTaskService.doTask(data);
            } catch (Exception e) {
                log.error("定时任务[钱包任务]执行失败", e);
            }
        });
    }

    @Transactional
    @Override
    public void doTask(WalletTask walletTask) {
        Date now = DateUtil.date();
        Long tradeId = walletTask.getTradeId();
        // 更新任务
        WalletTask task = new WalletTask()
                .setTradeId(tradeId)
                .setStatus(YesOrNoEnum.YES)
                .setVersion(walletTask.getVersion())
                .setUpdateTime(now);
        if (this.updateById(task) == 0) {
            return;
        }
        // 查询原账单
        WalletTrade walletTrade = walletTradeService.getById(tradeId);
        if (walletTrade == null) {
            return;
        }
        // 更新原值
        WalletTrade trade = new WalletTrade()
                .setTradeId(tradeId)
                .setTradeStatus(ApproveEnum.REJECT)
                .setUpdateTime(now);
        walletTradeService.updateById(trade);
        // 计算余额
        BigDecimal balance = walletPacketService.calculateBalance(tradeId);
        BigDecimal amount = NumberUtil.sub(walletTrade.getAbsolute(), balance);
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        Long userId = walletTrade.getUserId();
        // 更新钱包
        BigDecimal walletBalance = walletInfoService.addBalance(userId, amount);
        // 写入记录
        WalletTrade newTrade = BeanUtil.toBean(walletTrade, WalletTrade.class)
                .setTradeId(IdWorker.getId())
                .setTradeType(TradeTypeEnum.REFUND)
                .setTradeAmount(amount)
                .setTradeBalance(walletBalance)
                .setTradeStatus(ApproveEnum.REJECT)
                .setSourceId(walletTrade.getTradeId())
                .setSourceType(walletTrade.getTradeType())
                .setCreateTime(now)
                .setUpdateTime(now);
        walletTradeService.add(newTrade);
        // 推送
        walletTradeService.pushTradeMsg(userId, newTrade.getTradeId(), TradeTypeEnum.REFUND, amount, true);
    }

}
