package com.platform.common.shiro;

import com.platform.common.enums.ResultEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.utils.CodeUtils;
import com.platform.modules.auth.service.TokenService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * ShiroRealm
 */
public class ShiroRealm extends AuthorizingRealm {

    @Lazy
    @Resource
    private TokenService tokenService;

    /**
     * 提供用户信息，返回权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object object = ShiroUtils.getSubject().getPrincipal();
        if (object == null) {
            return null;
        }
        if (object instanceof ShiroUserVo) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            return info;
        }
        return null;
    }

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof ShiroLoginToken
                || authenticationToken instanceof ShiroLoginAuth;
    }

    /**
     * 身份认证/登录，验证用户是不是拥有相应的身份； 用于登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // token
        if (authenticationToken instanceof ShiroLoginToken) {
            String token = ((ShiroLoginToken) authenticationToken).getToken();
            ShiroUserVo loginUser = tokenService.convert(token);
            if (loginUser == null) {
                throw new LoginException(ResultEnum.UNAUTHORIZED);
            }
            String salt = CodeUtils.salt();
            String credentials = CodeUtils.credentials(token, salt);
            return new SimpleAuthenticationInfo(loginUser, credentials, ByteSource.Util.bytes(salt), getName());
        }
        // 账号+密码登录
        if (authenticationToken instanceof ShiroLoginAuth) {
            ShiroLoginAuth auth = (ShiroLoginAuth) authenticationToken;
            return new SimpleAuthenticationInfo(auth.getLoginUser(), auth.getPass(), ByteSource.Util.bytes(auth.getSalt()), getName());
        }
        return null;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return super.isPermitted(principals, permission);
    }

}
