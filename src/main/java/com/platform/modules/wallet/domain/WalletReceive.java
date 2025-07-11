package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包余额实体类
 * </p>
 */
@Data
@TableName("wallet_receive")
@Accessors(chain = true) // 链式调用
public class WalletReceive extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId
    private Long tradeId;
    /**
     * 接收人
     */
    private Long userId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 状态
     */
    private YesOrNoEnum status;
    /**
     * 执行版本
     */
    @Version
    private Integer version;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 执行时间
     */
    private Date updateTime;

}
