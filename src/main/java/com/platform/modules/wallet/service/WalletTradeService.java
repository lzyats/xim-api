package com.platform.modules.wallet.service;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.vo.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 钱包交易 服务层
 * </p>
 */
public interface WalletTradeService extends BaseService<WalletTrade> {

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 修改群名称
     */
    void editGroupName(Long groupId, String groupName);

    /**
     * 账单列表
     */
    PageInfo getTradeList(TradeTypeEnum tradeType);

    /**
     * 账单详情
     */
    TradeVo10 getTradeInfo(Long tradeId);

    /**
     * 删除账单
     */
    void removeTrade(Long tradeId);

    /**
     * 发送转账
     */
    void sendTransfer(Long tradeId, Long receiveId, JSONObject jsonObject);

    /**
     * 发送红包
     */
    void sendPacket(Long tradeId, Long receiveId, JSONObject jsonObject);

    /**
     * 发送专属红包
     */
    void sendPacketAssign(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject);

    /**
     * 发送群组红包
     */
    void sendPacketGroup(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject, YesOrNoEnum normal);

    /**
     * 发送群组转账
     */
    void sendGroupTransfer(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject);

    /**
     * 扫码支付
     */
    TradeVo05 sendScan(TradeVo06 tradeVo);

    /**
     * 消费
     */
    void shopping(BigDecimal amount, String password, ChatGroup chatGroup, String remark);

    /**
     * 消费
     */
    void payment(String appId, String goodsName, BigDecimal goodsPrice, String orderNo, String password);

    /**
     * 增加交易
     */
    WalletTrade addTrade(Long tradeId, TradeTypeEnum tradeType, BigDecimal amount, String password, String remark);

    /**
     * 接收
     */
    BigDecimal doReceive(Long tradeId);

    /**
     * 接收详情
     */
    TradeVo11 getSender(Long tradeId);

    /**
     * 接收详情
     */
    List<TradeVo12> getReceiver(Long tradeId);

    /**
     * 群组红包
     */
    PageInfo getGroupPacket(Long groupId);

    /**
     * 推送消息
     */
    void pushTradeMsg(Long userId, Long tradeId, TradeTypeEnum tradeType, BigDecimal amount, boolean payment);

}
