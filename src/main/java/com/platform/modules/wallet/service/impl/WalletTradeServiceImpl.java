package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.TradeUtils;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.GroupMemberEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.push.dto.PushBox;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushGroup;
import com.platform.modules.push.enums.PushBoxEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.dao.WalletTradeDao;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.*;
import com.platform.modules.wallet.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 钱包交易 服务层实现
 * </p>
 */
@Slf4j
@Service("walletTradeService")
public class WalletTradeServiceImpl extends BaseServiceImpl<WalletTrade> implements WalletTradeService {

    @Resource
    private WalletTradeDao walletTradeDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private WalletRechargeService walletRechargeService;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private WalletCashService walletCashService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private WalletPacketService walletPacketService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private WalletTaskService walletTaskService;

    @Resource
    private WalletReceiveService walletReceiveService;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private WalletShoppingService walletShoppingService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletTradeDao);
    }

    @Override
    public List<WalletTrade> queryList(WalletTrade t) {
        List<WalletTrade> dataList = walletTradeDao.queryList(t);
        return dataList;
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getNickname, nickname)
                .eq(WalletTrade::getUserId, current));
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getReceiveName, nickname)
                .eq(WalletTrade::getReceiveId, current));
    }

    @Override
    public void editPortrait(String portrait) {
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getPortrait, portrait)
                .eq(WalletTrade::getUserId, ShiroUtils.getUserId()));
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getReceivePortrait, portrait)
                .eq(WalletTrade::getReceiveId, ShiroUtils.getUserId()));
    }

    @Override
    public void editGroupName(Long groupId, String groupName) {
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getGroupName, groupName)
                .eq(WalletTrade::getGroupId, groupId));
    }

    @Override
    public PageInfo getTradeList(TradeTypeEnum tradeType) {
        // 查询数据
        QueryWrapper<WalletTrade> wrapper = new QueryWrapper();
        wrapper.eq(WalletTrade.COLUMN_USER_ID, ShiroUtils.getUserId());
        wrapper.eq(WalletTrade.COLUMN_DELETED, 0);
        wrapper.ge(WalletTrade.COLUMN_CREATE_TIME, DateUtil.offsetMonth(DateUtil.date(), -3));
        if (TradeTypeEnum.PACKET.equals(tradeType)) {
            wrapper.in(WalletTrade.COLUMN_TRADE_TYPE, Arrays.asList(
                    TradeTypeEnum.PACKET,
                    TradeTypeEnum.PACKET_ASSIGN,
                    TradeTypeEnum.PACKET_LUCK,
                    TradeTypeEnum.PACKET_NORMAL
            ));
        } else if (tradeType != null) {
            wrapper.eq(WalletTrade.COLUMN_TRADE_TYPE, tradeType);
        }
        List<WalletTrade> dataList = this.queryList(wrapper);
        List<TradeVo09> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new TradeVo09(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public TradeVo10 getTradeInfo(Long tradeId) {
        Long current = ShiroUtils.getUserId();
        WalletTrade trade = findById(tradeId);
        if (!trade.getUserId().equals(current)) {
            throw new BaseException(EXIST_MSG);
        }
        // 结果
        TradeVo10 result = new TradeVo10();
        switch (trade.getTradeType()) {
            // 充值
            case RECHARGE:
                WalletRecharge recharge = walletRechargeService.getById(tradeId);
                result = result.setRecharge(trade, recharge);
                break;
            // 提现
            case CASH:
                WalletCash cash = walletCashService.getById(tradeId);
                result = result.setCash(trade, cash);
                break;
            case TRANSFER:
            case PACKET:
            case SCAN:
                result = result.setFriend(trade);
                break;
            case PACKET_NORMAL:
            case PACKET_ASSIGN:
            case PACKET_LUCK:
            case GROUP_TRANSFER:
                result = result.setGroup(trade);
                break;
            case REFUND:
                result = result.setRefund(trade);
                break;
            default:
                result = new TradeVo10(trade);
                break;
        }
        return result;
    }

    @Override
    public void removeTrade(Long tradeId) {
        Long current = ShiroUtils.getUserId();
        WalletTrade trade = findById(tradeId);
        if (!trade.getUserId().equals(current)) {
            throw new BaseException(EXIST_MSG);
        }
        // 执行
        this.update(Wrappers.<WalletTrade>lambdaUpdate()
                .set(WalletTrade::getDeleted, null)
                .eq(WalletTrade::getTradeId, tradeId));
    }

    @Transactional
    @Override
    public void sendTransfer(Long tradeId, Long receiveId, JSONObject jsonObject) {
        // 转换
        TradeVo01 tradeVo = JSONUtil.toBean(jsonObject, TradeVo01.class);
        // 去除密码
        jsonObject.remove("password");
        // 校验
        ValidationUtil.verify(tradeVo);
        BigDecimal amount = tradeVo.getData();
        String password = tradeVo.getPassword();
        String remark = tradeVo.getRemark();
        // 写入账单
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.TRANSFER, amount, password, remark);
        // 设置接收人
        this.setReceive(trade, receiveId)
                .setTradeStatus(ApproveEnum.PASS)
                .setUpdateTime(DateUtil.date());
        this.add(trade);
        // 推送
        this.pushTradeMsg(trade.getUserId(), tradeId, TradeTypeEnum.TRANSFER, amount, false);
        // 写入账单
        Long newTradeId = this.addReceiveTrade(trade, amount);
        // 推送
        this.pushTradeMsg(receiveId, newTradeId, TradeTypeEnum.TRANSFER, amount, true);
    }

    @Transactional
    @Override
    public void sendPacket(Long tradeId, Long receiveId, JSONObject jsonObject) {
        // 转换
        TradeVo02 tradeVo = JSONUtil.toBean(jsonObject, TradeVo02.class);
        // 去除密码
        jsonObject.remove("password");
        // 校验
        ValidationUtil.verify(tradeVo);
        BigDecimal packet = chatConfigService.queryConfig(ChatConfigEnum.SYS_PACKET).getBigDecimal();
        if (tradeVo.getData().compareTo(packet) == 1) {
            throw new BaseException(StrUtil.format("红包金额不能大于{}元", packet.setScale(2)));
        }
        String password = tradeVo.getPassword();
        BigDecimal amount = tradeVo.getData();
        String remark = tradeVo.getRemark();
        // 写入账单
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.PACKET, amount, password, remark);
        // 设置接收人
        this.setReceive(trade, receiveId);
        this.add(trade);
        // 拆分金额
        Integer total = NumberUtil.mul(amount, 100).intValue();
        List<String> dataList = TradeUtils.splitAmount(total, 1, YesOrNoEnum.YES);
        // 写入缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, trade.getTradeId());
        redisUtils.lRightPushAll(redisKey, dataList, 1, TimeUnit.DAYS);
        // 增加定时任务
        walletTaskService.addTask(trade);
    }

    @Transactional
    @Override
    public void sendPacketAssign(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject) {
        // 转换
        TradeVo03 tradeVo = JSONUtil.toBean(jsonObject, TradeVo03.class);
        // 校验
        ValidationUtil.verify(tradeVo);
        Long current = ShiroUtils.getUserId();
        Long receiveId = tradeVo.getReceiveId();
        // 验证自己
        if (current.equals(receiveId)) {
            throw new BaseException("不能发给自己哦");
        }
        BigDecimal amount = tradeVo.getData();
        BigDecimal packet = chatConfigService.queryConfig(ChatConfigEnum.SYS_PACKET).getBigDecimal();
        if (amount.compareTo(packet) == 1) {
            throw new BaseException(StrUtil.format("红包金额不能大于{}元", packet.setScale(2)));
        }
        // 去除密码
        jsonObject.remove("password");
        String password = tradeVo.getPassword();
        String remark = tradeVo.getRemark();
        // 写入账单
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.PACKET_ASSIGN, amount, password, remark);
        // 设置接收人
        this.setReceive(trade, receiveId);
        // 设置群组
        this.setGroup(trade, chatGroup, 1);
        this.add(trade);
        // 拆分金额
        Integer total = NumberUtil.mul(amount, 100).intValue();
        List<String> dataList = TradeUtils.splitAmount(total, 1, YesOrNoEnum.NO);
        // 写入缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, trade.getTradeId());
        redisUtils.lRightPushAll(redisKey, dataList, 1, TimeUnit.DAYS);
        // 增加定时任务
        walletTaskService.addTask(trade);
    }

    @Transactional
    @Override
    public void sendPacketGroup(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject, YesOrNoEnum normal) {
        // 转换
        TradeVo04 tradeVo = JSONUtil.toBean(jsonObject, TradeVo04.class);
        // 校验
        ValidationUtil.verify(tradeVo);
        BigDecimal amount = tradeVo.getData();
        Integer total = NumberUtil.mul(amount, 100).intValue();
        Integer count = tradeVo.getCount();
        // 红包数不能大于总金额
        if (total < count) {
            throw new BaseException("请输入正确的金额");
        }
        // 正常红包
        if (YesOrNoEnum.YES.equals(normal)) {
            // 求余数必须正确
            if (total % count > 0) {
                throw new BaseException("请输入正确的金额");
            }
        }
        // 单个红包金额
        BigDecimal packet = chatConfigService.queryConfig(ChatConfigEnum.SYS_PACKET).getBigDecimal();
        if (NumberUtil.mul(packet, count).compareTo(amount) == -1) {
            throw new BaseException(StrUtil.format("单个红包金额不能大于{}元", packet.setScale(2)));
        }
        // 验证群
        Integer memberSize = chatGroupMemberService.getMemberSize(chatGroup.getGroupId());
        if (count > memberSize) {
            throw new BaseException("红包数不能大于群员数");
        }
        // 去除密码
        jsonObject.remove("password");
        String remark = tradeVo.getRemark();
        // 写入账单
        TradeTypeEnum tradeType = TradeTypeEnum.PACKET_LUCK;
        if (YesOrNoEnum.YES.equals(normal)) {
            tradeType = TradeTypeEnum.PACKET_NORMAL;
        }
        // 写入账单
        WalletTrade trade = this.addTrade(tradeId, tradeType, amount, tradeVo.getPassword(), remark);
        // 写入群组
        this.setGroup(trade, chatGroup, count);
        this.add(trade);
        // 拆分金额
        List<String> dataList = TradeUtils.splitAmount(total, count, normal);
        // 写入缓存
        String groupKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, trade.getTradeId());
        redisUtils.lRightPushAll(groupKey, dataList, 1, TimeUnit.DAYS);
        // 增加定时任务
        walletTaskService.addTask(trade);
    }

    @Transactional
    @Override
    public void sendGroupTransfer(Long tradeId, ChatGroup chatGroup, JSONObject jsonObject) {
        // 转换
        TradeVo14 tradeVo = JSONUtil.toBean(jsonObject, TradeVo14.class);
        // 校验
        ValidationUtil.verify(tradeVo);
        Long current = ShiroUtils.getUserId();
        Long receiveId = tradeVo.getReceiveId();
        // 验证自己
        if (current.equals(receiveId)) {
            throw new BaseException("不能发给自己哦");
        }
        BigDecimal amount = tradeVo.getData();
        // 去除密码
        jsonObject.remove("password");
        String password = tradeVo.getPassword();
        String remark = tradeVo.getRemark();
        // 写入账单
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.GROUP_TRANSFER, amount, password, remark);
        // 设置接收人
        this.setReceive(trade, receiveId)
                .setTradeStatus(ApproveEnum.PASS)
                .setUpdateTime(DateUtil.date());
        // 设置群组
        this.setGroup(trade, chatGroup, 1);
        this.add(trade);
        // 推送
        this.pushTradeMsg(trade.getUserId(), tradeId, TradeTypeEnum.GROUP_TRANSFER, amount, false);
        // 写入账单
        Long newTradeId = this.addReceiveTrade(trade, amount);
        // 推送
        this.pushTradeMsg(receiveId, newTradeId, TradeTypeEnum.GROUP_TRANSFER, amount, true);
    }

    @Transactional
    @Override
    public TradeVo05 sendScan(TradeVo06 tradeVo) {
        Long current = ShiroUtils.getUserId();
        Long receiveId = tradeVo.getReceiveId();
        BigDecimal amount = tradeVo.getData();
        String password = tradeVo.getPassword();
        String remark = tradeVo.getRemark();
        // 校验是否是自己
        if (current.equals(receiveId)) {
            throw new BaseException("自己不能给自己支付");
        }
        // 增加记录
        Long tradeId = IdWorker.getId();
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.SCAN, amount, password, remark);
        // 设置用户
        this.setReceive(trade, receiveId)
                .setTradeStatus(ApproveEnum.PASS)
                .setUpdateTime(DateUtil.date());
        this.add(trade);
        // 推送
        this.pushTradeMsg(trade.getUserId(), tradeId, TradeTypeEnum.SCAN, amount, false);
        // 增加记录
        Long newTradeId = this.addReceiveTrade(trade, amount);
        // 推送
        this.pushTradeMsg(receiveId, newTradeId, TradeTypeEnum.SCAN, amount, true);
        // 返回
        return new TradeVo05(trade.getTradeId());
    }

    @Transactional
    @Override
    public void shopping(BigDecimal amount, String password, ChatGroup chatGroup, String remark) {
        // 写入账单
        Long tradeId = IdWorker.getId();
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.SHOPPING, amount, password, remark);
        // 设置群组
        this.setGroup(trade, chatGroup, 1)
                .setTradeStatus(ApproveEnum.PASS)
                .setUpdateTime(DateUtil.date());
        this.add(trade);
        // 写入消费
        walletShoppingService.addShopping(trade);
    }

    @Transactional
    @Override
    public void payment(String appId, String goodsName, BigDecimal goodsPrice, String orderNo, String password) {
        Long tradeId = IdWorker.getId();
        String remark = StrUtil.format("应用Id：{}，订单编号：{}", appId, orderNo);
        WalletTrade trade = this.addTrade(tradeId, TradeTypeEnum.SHOPPING, goodsPrice, password, remark);
        this.add(trade);
        // 写入消费
        walletShoppingService.addShopping(trade);
    }

    @Override
    public WalletTrade addTrade(Long tradeId, TradeTypeEnum tradeType, BigDecimal amount, String password, String remark) {
        // 金额
        amount = amount.abs();
        // 备注
        if (StringUtils.isEmpty(remark)) {
            remark = tradeType.getInfo();
        }
        Long current = ShiroUtils.getUserId();
        // 扣减余额
        BigDecimal walletBalance = walletInfoService.subtractBalance(current, amount, password);
        // 是否红包
        YesOrNoEnum tradePacket;
        switch (tradeType) {
            case PACKET:
            case PACKET_ASSIGN:
            case PACKET_LUCK:
            case PACKET_NORMAL:
                tradePacket = YesOrNoEnum.YES;
                break;
            default:
                tradePacket = YesOrNoEnum.NO;
        }
        // 新增交易
        return new WalletTrade()
                .setTradeId(tradeId)
                .setTradeType(tradeType)
                .setTradePacket(tradePacket)
                .setTradeStatus(ApproveEnum.APPLY)
                .setTradeAmount(NumberUtil.sub(BigDecimal.ZERO, amount))
                .setTradeBalance(walletBalance)
                .setUserId(ShiroUtils.getUserId())
                .setUserNo(ShiroUtils.getUserNo())
                .setNickname(ShiroUtils.getNickname())
                .setPhone(ShiroUtils.getPhone())
                .setPortrait(ShiroUtils.getPortrait())
                .setTradeRemark(remark)
                .setCreateTime(DateUtil.date());
    }

    /**
     * 新增交易
     */
    private Long addReceiveTrade(WalletTrade walletTrade, BigDecimal amount) {
        Long tradeId = IdWorker.getId();
        Long receiveId = walletTrade.getReceiveId();
        BigDecimal walletBalance = BigDecimal.ZERO;
        try {
            // 增加余额
            walletBalance = walletInfoService.addTransactional(receiveId, amount);
        } catch (Exception e) {
            walletReceiveService.addReceive(tradeId, receiveId, amount);
        } finally {
            Date now = DateUtil.date();
            WalletTrade newTrade = new WalletTrade()
                    .setTradeId(tradeId)
                    .setTradeType(walletTrade.getTradeType())
                    .setTradePacket(walletTrade.getTradePacket())
                    .setTradeStatus(ApproveEnum.PASS)
                    .setTradeAmount(amount)
                    .setTradeBalance(walletBalance)
                    .setTradeRemark(walletTrade.getTradeRemark())
                    .setTradeCount(walletTrade.getTradeCount())
                    .setSourceId(walletTrade.getTradeId())
                    .setSourceType(walletTrade.getTradeType())
                    .setCreateTime(now)
                    .setUpdateTime(now)
                    .setUserId(receiveId)
                    .setUserNo(walletTrade.getReceiveNo())
                    .setNickname(walletTrade.getReceiveName())
                    .setPhone(walletTrade.getReceivePhone())
                    .setPortrait(walletTrade.getReceivePortrait())
                    .setReceiveId(walletTrade.getUserId())
                    .setReceiveNo(walletTrade.getUserNo())
                    .setReceiveName(walletTrade.getNickname())
                    .setReceivePhone(walletTrade.getPhone())
                    .setReceivePortrait(walletTrade.getPortrait())
                    .setGroupId(walletTrade.getGroupId())
                    .setGroupNo(walletTrade.getGroupNo())
                    .setGroupName(walletTrade.getGroupName());
            this.add(newTrade);
        }
        return tradeId;
    }

    /**
     * 完成交易
     */
    private void finishTrade(Long tradeId) {
        // 关闭定时
        walletTaskService.deleteById(tradeId);
        // 关闭账单
        WalletTrade trade = new WalletTrade()
                .setTradeId(tradeId)
                .setTradeStatus(ApproveEnum.PASS)
                .setUpdateTime(DateUtil.date());
        this.updateById(trade);
    }

    @Transactional
    @Override
    public BigDecimal doReceive(Long tradeId) {
        // 查询
        WalletTrade walletTrade = findById(tradeId);
        // 验证trade
        this.verifyTrade(walletTrade);
        switch (walletTrade.getTradeType()) {
            // 个人红包
            case PACKET:
                this.receivePacket(walletTrade);
                break;
            // 专属红包
            case PACKET_ASSIGN:
                this.receivePacketAssign(walletTrade);
                break;
            // 手气红包
            // 普通红包
            case PACKET_LUCK:
            case PACKET_NORMAL:
                this.receivePacketGroup(walletTrade);
                break;
            default:
                throw new BaseException("数据不存在");
        }
        // 查询
        return walletPacketService.getAmount(tradeId);
    }

    /**
     * 个人红包
     */
    private void receivePacket(WalletTrade walletTrade) {
        Long current = ShiroUtils.getUserId();
        // 验证红包
        if (!current.equals(walletTrade.getReceiveId())) {
            return;
        }
        Long tradeId = walletTrade.getTradeId();
        Long receiveId = walletTrade.getUserId();
        // 交易金额
        BigDecimal amount = walletTrade.getAbsolute();
        // 红包验证
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, tradeId);
        // 已经被抢完了
        if (StringUtils.isEmpty(redisUtils.lLeftPop(redisKey))) {
            return;
        }
        // 写入packet
        walletPacketService.addPacket(tradeId, amount);
        // 写入账单
        this.addReceiveTrade(walletTrade, amount);
        // 抢完更新
        this.finishTrade(tradeId);
        // 发送通知
        ChatFriend friend1 = chatFriendService.getFriend(receiveId, current);
        if (friend1 != null) {
            String content = StrUtil.format(AppConstants.TIPS_PACKET_FROM, walletTrade.getReceiveName());
            pushService.pushSingle(friend1.getPushFrom(IdWorker.getId()), receiveId, content, PushMsgTypeEnum.TIPS);
        }
        // 发送通知
        ChatFriend friend2 = chatFriendService.getFriend(current, receiveId);
        if (friend2 != null) {
            String content = StrUtil.format(AppConstants.TIPS_PACKET_RECEIVE, walletTrade.getNickname());
            pushService.pushSingle(friend2.getPushFrom(IdWorker.getId()), current, content, PushMsgTypeEnum.TIPS);
        }
    }

    /**
     * 专属红包
     */
    private void receivePacketAssign(WalletTrade walletTrade) {
        Long current = ShiroUtils.getUserId();
        Long tradeId = walletTrade.getTradeId();
        BigDecimal amount = walletTrade.getAbsolute();
        // 验证专属
        if (!current.equals(walletTrade.getReceiveId())) {
            return;
        }
        // 红包验证
        String redisKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, tradeId);
        // 抢到的金额
        String amountStr = redisUtils.lLeftPop(redisKey);
        // 已经被抢完了
        if (StringUtils.isEmpty(amountStr)) {
            return;
        }
        // 写入packet
        walletPacketService.addPacket(tradeId, amount);
        // 抢完更新
        this.finishTrade(tradeId);
        // 写入账单
        this.addReceiveTrade(walletTrade, amount);
        // 发送红包通知
        this.sendGroupNotice(walletTrade, current);
    }

    /**
     * 手气红包
     * 普通红包
     */
    private void receivePacketGroup(WalletTrade walletTrade) {
        Long current = ShiroUtils.getUserId();
        Long tradeId = walletTrade.getTradeId();
        // 验证禁止红包
        this.verifyConfigReceive(walletTrade.getGroupId(), current);
        // 抢包验证
        String receiveKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_RECEIVE, tradeId, current);
        if (!redisUtils.setIfAbsent(receiveKey, receiveKey, 25, TimeUnit.HOURS)) {
            return;
        }
        // 抢包验证
        if (walletPacketService.queryPacket(tradeId, current) != null) {
            return;
        }
        // 红包验证
        String groupKey = StrUtil.format(AppConstants.REDIS_WALLET_PACKET_SPLIT, tradeId);
        // 抢到的金额
        String amountStr = redisUtils.lLeftPop(groupKey);
        // 已经被抢完了
        if (StringUtils.isEmpty(amountStr)) {
            return;
        }
        Long groupLength = redisUtils.lSize(groupKey);
        BigDecimal amount = NumberUtil.div(amountStr, "100");
        // 写入packet
        walletPacketService.addPacket(tradeId, amount);
        // 写入账单
        this.setReceive(walletTrade);
        // 写入账单
        this.addReceiveTrade(walletTrade, amount);
        // 发送红包通知
        this.sendGroupNotice(walletTrade, current);
        // 抢完更新
        if (groupLength.intValue() == 0) {
            this.finishTrade(tradeId);
        }
    }

    @Override
    public TradeVo11 getSender(Long tradeId) {
        WalletTrade walletTrade = findById(tradeId);
        // 验证trade
        this.verifyTrade(walletTrade);
        // 组装数据
        return new TradeVo11(walletTrade);
    }

    @Override
    public List<TradeVo12> getReceiver(Long tradeId) {
        WalletTrade walletTrade = this.findById(tradeId);
        // 验证trade
        verifyTrade(walletTrade);
        // 转账
        // 群内转账
        switch (walletTrade.getTradeType()) {
            case TRANSFER:
            case GROUP_TRANSFER:
                return Arrays.asList(new TradeVo12(walletTrade));
        }
        // 未被领完
        if (ApproveEnum.APPLY.equals(walletTrade.getTradeStatus())) {
            // 未被领取
            BigDecimal amount = walletPacketService.getAmount(tradeId);
            if (BigDecimal.ZERO.equals(amount)) {
                return new ArrayList<>();
            }
        }
        // 查询列表
        return walletPacketService.queryDataList(walletTrade);
    }

    @Override
    public PageInfo getGroupPacket(Long groupId) {
        Long current = ShiroUtils.getUserId();
        // 验证群组
        chatGroupMemberService.verifyGroupMaster(groupId, current);
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        // 查询数据
        List<WalletTrade> dataList = walletTradeDao.getGroupPacket(groupId, current, YesOrNoEnum.YES);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            dataList = walletTradeDao.getGroupPacket(groupId, current, YesOrNoEnum.NO);
        }
        // 组装
        List<TradeVo13> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new TradeVo13(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dictList.size());
    }

    /**
     * 验证交易
     */
    private void verifyTrade(WalletTrade walletTrade) {
        Long current = ShiroUtils.getUserId();
        Long receiveId = walletTrade.getReceiveId();
        Long userId = walletTrade.getUserId();
        Long groupId = walletTrade.getGroupId();
        TradeTypeEnum tradeType = walletTrade.getTradeType();
        // 当前人是发包人
        if (current.equals(userId)) {
            return;
        }
        // 当前人是接收人
        if (current.equals(receiveId)) {
            return;
        }
        // 当前人是群组人
        if (TradeTypeEnum.PACKET_ASSIGN.equals(tradeType)
                || TradeTypeEnum.PACKET_NORMAL.equals(tradeType)
                || TradeTypeEnum.PACKET_LUCK.equals(tradeType)) {
            chatGroupMemberService.verifyGroupMember(groupId, current);
            return;
        }
        // 报错
        throw new BaseException(EXIST_MSG);
    }

    /**
     * 验证禁止红包
     */
    private void verifyConfigReceive(Long groupId, Long current) {
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            return;
        }
        // 红包禁抢
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigReceive())) {
            return;
        }
        // 查询群员
        ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (groupMember == null) {
            return;
        }
        // 成员类型
        GroupMemberEnum memberType = groupMember.getMemberType();
        if (GroupMemberEnum.NORMAL.equals(memberType)) {
            if (YesOrNoEnum.NO.equals(groupMember.getPacketWhite())) {
                throw new BaseException("群开启了禁抢功能，抢包失败");
            }
        }
    }

    /**
     * 设置接收人
     */
    private WalletTrade setReceive(WalletTrade trade) {
        return trade.setReceiveId(ShiroUtils.getUserId())
                .setReceiveNo(ShiroUtils.getUserNo())
                .setReceiveName(ShiroUtils.getNickname())
                .setReceivePhone(ShiroUtils.getPhone())
                .setReceivePortrait(ShiroUtils.getPortrait());
    }

    /**
     * 设置接收人
     */
    private WalletTrade setReceive(WalletTrade trade, Long receiveId) {
        String receiveNo = "-";
        String receiveName = "-";
        String receivePhone = "-";
        String receivePortrait = "-";
        // 查询
        ChatUser chatUser = chatUserService.getById(receiveId);
        if (chatUser != null) {
            receiveNo = chatUser.getUserNo();
            receiveName = chatUser.getNickname();
            receivePhone = chatUser.getPhone();
            receivePortrait = chatUser.getPortrait();
        }
        return trade.setReceiveId(receiveId)
                .setReceiveNo(receiveNo)
                .setReceiveName(receiveName)
                .setReceivePhone(receivePhone)
                .setReceivePortrait(receivePortrait);
    }

    /**
     * 设置群组
     */
    private WalletTrade setGroup(WalletTrade trade, ChatGroup chatGroup, Integer count) {
        return trade.setGroupId(chatGroup.getGroupId())
                .setGroupNo(chatGroup.getGroupNo())
                .setGroupName(chatGroup.getGroupName())
                .setTradeCount(count);
    }

    /**
     * 发送红包通知
     */
    private void sendGroupNotice(WalletTrade trade, Long current) {
        Long userId = trade.getUserId();
        // 自己领取自己红包，不需要提醒
        if (current.equals(userId)) {
            return;
        }
        // 查询群组
        Long groupId = trade.getGroupId();
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            return;
        }
        PushFrom pushFrom = chatGroup.getPushFrom();
        PushGroup pushGroup = chatGroup.getPushGroup();
        // 发送红包通知
        ChatGroupMember chatGroupMember = chatGroupMemberService.queryGroupMember(groupId, current);
        if (chatGroupMember != null) {
            String content = StrUtil.format(AppConstants.TIPS_PACKET_FROM, chatGroupMember.getDefaultRemark());
            pushService.pushGroup(pushFrom, pushGroup, Arrays.asList(userId), content, PushMsgTypeEnum.TIPS);
        }
        // 发送红包通知
        chatGroupMember = chatGroupMemberService.queryGroupMember(groupId, userId);
        if (chatGroupMember != null) {
            String content = StrUtil.format(AppConstants.TIPS_PACKET_RECEIVE, chatGroupMember.getDefaultRemark());
            pushService.pushGroup(pushFrom, pushGroup, Arrays.asList(current), content, PushMsgTypeEnum.TIPS);
        }
    }

    // 通知推送
    @Override
    public void pushTradeMsg(Long userId, Long tradeId, TradeTypeEnum tradeType, BigDecimal amount, boolean payment) {
        // 查询服务
        PushFrom pushFrom = chatRobotService.getPushFrom(AppConstants.ROBOT_PAY);
        // 组装消息
        String title = "";
        String label = "";
        switch (tradeType) {
            case TRANSFER:
                title = "好友转账";
                label = payment ? "收款" : "转账";
                break;
            case GROUP_TRANSFER:
                title = "群内转账";
                label = payment ? "收款" : "转账";
                break;
            case SCAN:
                title = payment ? "扫码收款" : "扫码支付";
                label = payment ? "收款" : "扫码";
                break;
            case REFUND:
                title = "系统退款";
                label = "退款";
                break;
        }
        String content = StrUtil.format("￥ {}{} 元", payment ? "+" : "-", amount.abs().setScale(2));
        String remark = StrUtil.format("{}{}：￥ {} 元", label, payment ? "到账" : "支付", amount.abs().setScale(2));
        PushBox pushBox = new PushBox(remark, title, content, remark, PushBoxEnum.TRADE, tradeId);
        // 推送消息
        pushService.pushSingle(pushFrom, userId, JSONUtil.toJsonStr(pushBox), PushMsgTypeEnum.BOX);
    }

}
