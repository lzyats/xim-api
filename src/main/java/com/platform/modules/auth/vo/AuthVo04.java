package com.platform.modules.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthVo04 {

    /**
     * token
     */
    @NotBlank(message = "token不能为空")
    private String token;

}
