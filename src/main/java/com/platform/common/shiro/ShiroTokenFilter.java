package com.platform.common.shiro;

import cn.hutool.json.JSONUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.ResultEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.utils.PathUtil;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.filter.auth.AuthConfig;
import com.platform.common.web.filter.banned.BannedConfig;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * auth2过滤器
 */
public class ShiroTokenFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse servletResponse) {
        //获取请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return null;
        }
        return token;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(httpRequest.getMethod())) {
            return true;
        }
        // 获取路径
        String requestPath = ServletUtils.getAttribute(httpRequest, HeadConstant.REQUEST_PATH);
        // 验证登录
        return PathUtil.verifyUrl(requestPath, AuthConfig.DATA_LIST);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return error(response, ResultEnum.UNAUTHORIZED, null);
        }
        try {
            // 登录
            getSubject(request, response).login(token);
            // 过滤
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 获取路径
            String requestPath = ServletUtils.getAttribute(httpRequest, HeadConstant.REQUEST_PATH);
            if (PathUtil.verifyUrl(requestPath, BannedConfig.DATA_LIST)) {
                return true;
            }
            // 验证禁用
            if (YesOrNoEnum.transform(ShiroUtils.getBanned())) {
                return error(response, ResultEnum.BANNED, null);
            }
            return true;
        } catch (LoginException e) {
            if (ResultEnum.FAIL.equals(e.getResultEnum())) {
                return error(response, ResultEnum.FAIL, e.getMessage());
            }
            return error(response, ResultEnum.UNAUTHORIZED, e.getMessage());
        } catch (AuthenticationException e) {
            return error(response, ResultEnum.UNAUTHORIZED, null);
        }
    }

    private boolean error(ServletResponse response, ResultEnum resultEnum, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.getWriter().print(JSONUtil.toJsonStr(AjaxResult.result(resultEnum, msg)));
        return false;
    }

    /**
     * 请求的token
     */
    private ShiroLoginToken getToken(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(HeadConstant.TOKEN_HEADER_ADMIN);
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(HeadConstant.TOKEN_HEADER_ADMIN);
        }
        if (!StringUtils.isEmpty(token)) {
            return new ShiroLoginToken(token);
        }
        return null;
    }

}
