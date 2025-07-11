package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包实体类
 * </p>
 */
@Data
@TableName("wallet_info")
@Accessors(chain = true) // 链式调用
public class WalletInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户
     */
    @TableId
    private Long userId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 盐巴
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 版本信息
     */
    @Version
    private Integer version;

}
