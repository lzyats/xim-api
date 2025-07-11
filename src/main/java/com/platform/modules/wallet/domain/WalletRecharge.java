package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.wallet.enums.TradePayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包充值实体类
 * </p>
 */
@Data
@TableName("wallet_recharge")
@Accessors(chain = true) // 链式调用
public class WalletRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId
    private Long tradeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 支付类型
     */
    private TradePayEnum payType;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 交易号码
     */
    private String tradeNo;
    /**
     * 交易号码
     */
    private String orderNo;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 处理时间
     */
    private Date updateTime;

    /**
     * 字段
     */
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATE_TIME = "create_time";

}
