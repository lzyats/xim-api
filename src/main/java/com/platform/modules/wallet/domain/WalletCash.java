package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包提现实体类
 * </p>
 */
@Data
@TableName("wallet_cash")
@Accessors(chain = true) // 链式调用
public class WalletCash extends BaseEntity {

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
     * 姓名
     */
    private String name;
    /**
     * 钱包账户
     */
    private String wallet;
    /**
     * 申请金额
     */
    private BigDecimal amount;
    /**
     * 服务费
     */
    private BigDecimal charge;
    /**
     * 申请利率
     */
    private BigDecimal rate;
    /**
     * 申请加成
     */
    private BigDecimal cost;
    /**
     * 拒绝原因
     */
    private String reason;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;

    /**
     * 字段
     */
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATE_TIME = "create_time";


}
