package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 钱包卡包实体类
 * </p>
 */
@Data
@TableName("wallet_bank")
@Accessors(chain = true) // 链式调用
public class WalletBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 卡包id
     */
    @TableId
    private Long bankId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 账户
     */
    private String wallet;

}
