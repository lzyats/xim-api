package com.platform.modules.wallet.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class TradeVo07 {

    private static final long serialVersionUID = 1L;

    private Long bankId;

    /**
     * 姓名
     */
    private String name;
    /**
     * 账户
     */
    private String wallet;

}
