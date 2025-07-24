package com.platform.modules.common.vo;

import com.platform.common.validation.ValidateGroup;
import com.platform.modules.common.enums.MessageTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 短信对象
 */
@Data
@Accessors(chain = true) // 链式调用
public class CommonVo03 {
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank(message = "请输入正确的邮箱", groups = ValidateGroup.ONE.class)
    private String email;

    /**
     * 短信类型
     */
    @NotNull(message = "短信类型不能为空")
    private MessageTypeEnum type;

    /**
     * 系统安全码
     */
    private String safestr;

}
