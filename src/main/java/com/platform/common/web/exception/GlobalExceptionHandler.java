package com.platform.common.web.exception;

import cn.hutool.extra.spring.SpringUtil;
import com.platform.common.enums.ResultEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.service.SysErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        String token = ShiroUtils.getToken();
        if (ShiroUtils.isLogin()) {
            SpringUtil.getBean(SysErrorService.class).addMessage(e.getMessage());
            token = ShiroUtils.getUserId().toString();
        } else if (StringUtils.isEmpty(token)) {
            token = "-";
        }
        log.error("全局异常:{}:{}", token, e.getMessage(), e);
        /**
         * 路径不存在
         */
        if (e instanceof NoHandlerFoundException
                || e instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            return AjaxResult.result(ResultEnum.NOT_FOUND);
        }
        /**
         * 校验异常
         */
        if (e instanceof MethodArgumentNotValidException) {
            return AjaxResult.fail(((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage());
        }
        /**
         * 自定义异常
         */
        if (e instanceof BaseException) {
            BaseException exception = ((BaseException) e);
            if (ResultEnum.VERSION.equals(exception.getResultEnum())) {
                return AjaxResult.result(ResultEnum.VERSION);
            }
            if (ResultEnum.SUCCESS.equals(exception.getResultEnum())) {
                return AjaxResult.success();
            }
            return AjaxResult.fail(exception.getMessage());
        }
        return AjaxResult.fail();
    }

}
