package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.wallet.dao.WalletPacketDao;
import com.platform.modules.wallet.domain.WalletPacket;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletPacketService;
import com.platform.modules.wallet.vo.TradeVo12;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 钱包红包 服务层实现
 * </p>
 */
@Service("walletPacketService")
public class WalletPacketServiceImpl extends BaseServiceImpl<WalletPacket> implements WalletPacketService {

    @Resource
    private WalletPacketDao walletPacketDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletPacketDao);
    }

    @Override
    public List<WalletPacket> queryList(WalletPacket t) {
        List<WalletPacket> dataList = walletPacketDao.queryList(t);
        return dataList;
    }

    @Override
    public void addPacket(Long tradeId, BigDecimal amount) {
        Date now = DateUtil.date();
        WalletPacket walletPacket = new WalletPacket()
                .setTradeId(tradeId)
                .setUserId(ShiroUtils.getUserId())
                .setUserNo(ShiroUtils.getUserNo())
                .setNickname(ShiroUtils.getNickname())
                .setPortrait(ShiroUtils.getPortrait())
                .setAmount(amount)
                .setCreateTime(now);
        this.add(walletPacket);
    }

    @Override
    public BigDecimal getAmount(Long tradeId) {
        Long current = ShiroUtils.getUserId();
        WalletPacket packet = this.queryOne(new WalletPacket().setTradeId(tradeId).setUserId(current));
        BigDecimal amount = BigDecimal.ZERO;
        if (packet != null) {
            amount = packet.getAmount();
        }
        return amount;
    }

    @Override
    public List<TradeVo12> queryDataList(WalletTrade trade) {
        // 查询参数
        WalletPacket query = new WalletPacket().setTradeId(trade.getTradeId());
        // 执行查询
        List<WalletPacket> dataList = this.queryList(query);
        // 查询手气最差
        Long worst = calculatePacket(trade, dataList, YesOrNoEnum.NO);
        // 查询手气最佳
        Long best = calculatePacket(trade, dataList, YesOrNoEnum.YES);
        // 重新排序
        Collections.sort(dataList, Comparator.comparing(WalletPacket::getPacketId));
        // 转换数据
        List<TradeVo12> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            TradeVo12 tradeVo = new TradeVo12(y);
            if (y.getUserId().equals(worst)) {
                tradeVo.setBest(YesOrNoEnum.NO);
            }
            if (y.getUserId().equals(best)) {
                tradeVo.setBest(YesOrNoEnum.YES);
            }
            x.add(tradeVo);
        }, ArrayList::addAll);
        return dictList;
    }

    /**
     * 查询最佳/最差红包
     */
    private static Long calculatePacket(WalletTrade trade, List<WalletPacket> dataList, YesOrNoEnum best) {
        Long packetId = 0L;
        // 手气红包才有最佳
        if (!TradeTypeEnum.PACKET_LUCK.equals(trade.getTradeType())) {
            return packetId;
        }
        if (CollectionUtils.isEmpty(dataList)) {
            return packetId;
        }
        // 抽完的红包才有最佳
        if (trade.getTradeCount().intValue() != dataList.size()) {
            return packetId;
        }
        Collections.sort(dataList, (o1, o2) -> {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            int index = o1.getAmount().compareTo(o2.getAmount());
            if (index == 0) {
                if (o1.getPacketId() > o2.getPacketId()) {
                    index = 1;
                } else {
                    index = -1;
                }
            }
            // 最佳
            if (YesOrNoEnum.YES.equals(best)) {
                return 0 - index;
            }
            // 最差
            return index;
        });
        return dataList.get(0).getUserId();

    }

    @Override
    public WalletPacket queryPacket(Long tradeId, Long userId) {
        return this.queryOne(new WalletPacket().setTradeId(tradeId).setUserId(userId));
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<WalletPacket>lambdaUpdate()
                .set(WalletPacket::getNickname, nickname)
                .eq(WalletPacket::getUserId, current));
    }

    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<WalletPacket>lambdaUpdate()
                .set(WalletPacket::getPortrait, portrait)
                .eq(WalletPacket::getUserId, current));
    }

    @Override
    public BigDecimal calculateBalance(Long tradeId) {
        // 查询抢过的红包
        List<WalletPacket> dataList = queryList(new WalletPacket().setTradeId(tradeId));
        BigDecimal amount = BigDecimal.ZERO;
        for (WalletPacket packet : dataList) {
            amount = NumberUtil.add(amount, packet.getAmount());
        }
        return amount;
    }

}
