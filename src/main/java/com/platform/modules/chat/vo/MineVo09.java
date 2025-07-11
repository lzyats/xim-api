package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo09 {

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能大于20")
    private String name;

    @NotBlank(message = "身份证不能为空")
    @Size(max = 20, message = "身份证长度不能大于20")
    private String idCard;

    @NotBlank(message = "身份证正面不能为空")
    @Size(max = 2000, message = "身份证正面长度不能大于2000")
    private String identity1;

    @NotBlank(message = "身份证反面不能为空")
    @Size(max = 2000, message = "身份证反面长度不能大于2000")
    private String identity2;

    @Size(max = 2000, message = "身份证手持长度不能大于2000")
    private String holdCard;

    public void setName(String name) {
        this.name = StrUtil.trim(name);
    }

    public void setIdCard(String idCard) {
        this.idCard = StrUtil.trim(idCard);
    }
}
