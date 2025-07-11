package com.platform.modules.auth.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录
 */
@Data
@NoArgsConstructor
public class AuthVo00 {

    /**
     * token
     */
    private String token;
    /**
     * 封禁
     */
    private YesOrNoEnum banned;

    public AuthVo00(String token) {
        this.token = token;
        this.banned = YesOrNoEnum.NO;
    }

    public AuthVo00(String token, YesOrNoEnum banned) {
        this.token = token;
        this.banned = banned;
    }
}
