package com.platform.common.exception;

import com.platform.common.enums.ResultEnum;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;

/**
 * 登录异常
 */
public class LoginException extends AuthenticationException {

    @Getter
    private ResultEnum resultEnum;

    public LoginException(ResultEnum resultEnum) {
        super(resultEnum.getInfo());
        this.resultEnum = resultEnum;
    }

}
