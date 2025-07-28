package com.platform.modules.wallet.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class TradeVo08 {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能大于20")
    private String name;
    /**
     * 账户
     */
    @NotBlank(message = "钱包地址不能为空")
    @Size(min=26,max = 36, message = "钱包地址长度不能小于26且大于36个字符")
    private String wallet;

    public void setName(String name) {
        this.name = StrUtil.trim(name);
    }

    public void setWallet(String wallet) {
        this.wallet = StrUtil.trim(wallet);
    }

}
