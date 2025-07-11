package com.platform.modules.wallet.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo03 {

    /**
     * 充值次数
     */
    private Integer count;

    /**
     * 充值说明
     */
    private String remark;

}
