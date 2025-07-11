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
 * 钱包消费实体类
 * </p>
 */
@Data
@TableName("wallet_shopping")
@Accessors(chain = true) // 链式调用
public class WalletShopping extends BaseEntity {

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
     * 用户号码
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
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 交易备注
     */
    private String remark;
    /**
     * 交易时间
     */
    private Date createTime;

}
