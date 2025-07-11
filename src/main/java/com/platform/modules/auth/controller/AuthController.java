package com.platform.modules.auth.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.exception.BaseException;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.auth.service.AuthService;
import com.platform.modules.auth.vo.*;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.common.service.MessageService;
import com.platform.modules.common.vo.CommonVo03;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 认证
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private MessageService messageService;

    @Resource
    private AuthService authService;

    /**
     * 发送短信（注册/登录/忘记密码）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping(value = "/sendCode")
    @SubmitRepeat
    public AjaxResult sendCode(@Validated @RequestBody CommonVo03 commonVo) {
        // 账号
        String phone = commonVo.getPhone();
        // 邮箱
        String email = commonVo.getEmail();
        // 类型
        MessageTypeEnum messageType = commonVo.getType();
        // 分发
        switch (messageType) {
            case REGISTER:
                // 验证
                ValidationUtil.verify(commonVo, ValidateGroup.ONE.class);
                if (!Validator.isEmail(email)) {
                    throw new BaseException("请输入正确的邮箱");
                }
                break;
            case LOGIN:
            case FORGET:
                break;
            default:
                throw new BaseException("短信类型不正确");
        }
        Dict data = chatUserService.sendCode(phone, email, messageType);
        return AjaxResult.success(data);
    }

    /**
     * 登录方法（根据账号+密码登录）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/loginByPwd")
    @SubmitRepeat
    public AjaxResult loginByPwd(@Validated @RequestBody AuthVo02 authVo) {
        AuthVo00 data = authService.loginByPwd(authVo.getPhone(), authVo.getPassword());
        return AjaxResult.success(data);
    }

    /**
     * 登录方法（根据账号+验证码登录）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/loginByCode")
    @SubmitRepeat
    public AjaxResult loginByCode(@Validated @RequestBody AuthVo03 authVo) {
        // 验证
        messageService.verifySms(authVo.getPhone(), authVo.getCode(), MessageTypeEnum.LOGIN);
        // 执行登录
        AuthVo00 data = authService.loginByCode(authVo);
        return AjaxResult.success(data);
    }

    /**
     * 注册方法
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/register")
    @SubmitRepeat
    public AjaxResult register(@Validated @RequestBody AuthVo06 authVo) {
        // 验证
        if (!Validator.isEmail(authVo.getEmail())) {
            throw new BaseException("请输入正确的邮箱");
        }
        // 验证
        messageService.verifySms(authVo.getPhone(), authVo.getCode(), MessageTypeEnum.REGISTER);
        // 执行注册
        AuthVo00 data = authService.register(authVo);
        return AjaxResult.success(data);
    }

    /**
     * 找回密码（根据账号）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/forget")
    public AjaxResult forget(@Validated @RequestBody AuthVo01 authVo) {
        // 验证
        String phone = authVo.getPhone();
        messageService.verifySms(phone, authVo.getCode(), MessageTypeEnum.FORGET);
        // 重置
        chatUserService.resetPass(phone, authVo.getPassword());
        return AjaxResult.success();
    }

    /**
     * 退出系统
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/logout")
    public AjaxResult logout() {
        chatUserService.logout();
        return AjaxResult.success();
    }

    /**
     * 生成二维码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getQrCode")
    public AjaxResult getQrCode() {
        // 调用
        AuthVo05 data = authService.getQrCode();
        return AjaxResult.success(data);
    }

    /**
     * 确认二维码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/confirmQrCode")
    public AjaxResult confirmQrCode(@Validated @RequestBody AuthVo04 authVo) {
        // 调用
        authService.confirmQrCode(authVo.getToken());
        return AjaxResult.success();
    }

}
