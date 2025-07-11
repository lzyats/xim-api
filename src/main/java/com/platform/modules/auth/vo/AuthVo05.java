package com.platform.modules.auth.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthVo05 {

    /**
     * 通信token
     */
    private String token;

    /**
     * 过期时间
     */
    private Integer expired;

    public AuthVo05(String token, Integer expired) {
        this.token = token;
        this.expired = expired;
    }
}
