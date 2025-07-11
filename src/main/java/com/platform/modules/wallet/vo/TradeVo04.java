package com.platform.modules.wallet.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class TradeVo04 {

    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    private String password;
    /**
     * 金额
     */
    @Digits(integer = 8, fraction = 2, message = "红包金额格式不正确")
    @DecimalMin(value = "0.01", message = "红包金额不能小于0.01元")
    @DecimalMax(value = "999999.99", message = "红包金额不能大于999999.99元")
    @NotNull(message = "红包金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal data;
    /**
     * 备注
     */
    @Size(max = 200, message = "备注内容长度不能大于200")
    private String remark;
    /**
     * 红包个数
     */
    @NotNull(message = "红包个数不能为空")
    @Min(value = 1, message = "红包个数不能小于1")
    private Integer count;

    public void setRemark(String remark) {
        this.remark = StrUtil.trim(remark);
    }
}
