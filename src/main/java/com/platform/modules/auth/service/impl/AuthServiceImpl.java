/**
 * Licensed to the Apache Software Foundation （ASF） under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * （the "License"）； you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.platform.modules.auth.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.exception.LoginException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroLoginAuth;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.sms.service.SmsService;
import com.platform.common.utils.CodeUtils;
import com.platform.modules.auth.service.AuthService;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.auth.vo.AuthVo00;
import com.platform.modules.auth.vo.AuthVo03;
import com.platform.modules.auth.vo.AuthVo05;
import com.platform.modules.auth.vo.AuthVo06;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.dto.PushBox;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.service.WalletInfoService;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.platform.modules.auth.util.WuxiaNameGenerator;
/**
 * 鉴权 服务层
 */
@EqualsAndHashCode
@Slf4j
@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private ChatUserTokenService chatUserTokenService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private ChatUserDeletedService chatUserDeletedService;

    @Resource
    private ChatNumberService chatNumberService;

    @Resource
    private PushService pushService;

    @Resource
    private HookService hookService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private ChatRobotReplyService chatRobotReplyService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Resource
    private ChatPortraitService chatPortraitService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private SmsService smsService;

    // 注入Spring管理的XianxiaNameGenerator实例
    @Autowired
    private WuxiaNameGenerator wuxiaNameGenerator;

    @Transactional
    @Override
    public AuthVo00 loginByPwd(String phone, String password) {
        // 登录次数
        String redisKey = makeLoginKey(phone);
        Long count = redisUtils.increment(redisKey, 1, 1, TimeUnit.DAYS);
        if (count > 5) {
            throw new BaseException("密码错误超过5次，请使用验证码登录");
        }
        // 查询用户
        ChatUser chatUser = chatUserService.queryByPhone(phone);
        if (chatUser == null) {
            throw new BaseException("账号或密码不正确");
        }
        String msg = null;
        try {
            ShiroUtils.getSubject().login(new ShiroLoginAuth(phone, password, chatUser));
        } catch (LoginException e) {
            msg = e.getMessage();
        } catch (AuthenticationException e) {
            msg = "账号或密码不正确";
        } catch (Exception e) {
            msg = "登录异常，请稍后重试";
            log.error(msg, e);
        }
        if (!StringUtils.isEmpty(msg)) {
            throw new BaseException(msg);
        }
        // 执行登录
        String token = doLogin(chatUser, UserLogEnum.LOGIN_PWD);
        return new AuthVo00(token, chatUser.getBanned());
    }

    /**
     * 组装登录key
     */
    private String makeLoginKey(String phone) {
        Date now = DateUtil.date();
        Integer day = DateUtil.dayOfMonth(now);
        return StrUtil.format(AppConstants.REDIS_CHAT_PWD, day, phone);
    }

    @Transactional
    @Override
    public AuthVo00 loginByCode(AuthVo03 authVo) {
        // 账号
        String phone = authVo.getPhone();
        // 查询用户
        ChatUser chatUser = chatUserService.queryByPhone(phone);
        if (chatUser == null) {
            if (PlatformConfig.EMAIL) {
                throw new BaseException("当前账号不存在，登录失败");
            }
            // 验证
            chatUserDeletedService.register(phone);
            // 执行注册
            ShiroUserVo shiroUserVo = this.doRegister(phone, null);
            // 注册推送
            hookService.handle(PushAuditEnum.USER_REGISTER);
            // 注册推送
            chatRobotReplyService.subscribe(shiroUserVo.getUserId());
            // 返回
            return new AuthVo00(shiroUserVo.getToken());
        }
        // 执行登录
        String token = doLogin(chatUser, UserLogEnum.LOGIN_CODE);
        return new AuthVo00(token, chatUser.getBanned());
    }

    @Override
    public AuthVo00 register(AuthVo06 authVo) {
        // 账号
        String phone = authVo.getPhone();
        // 邮箱
        String email = authVo.getEmail();
        // 查询用户
        ChatUser chatUser = chatUserService.queryByPhone(phone);
        if (chatUser != null) {
            throw new BaseException("当前账号已存在，注册失败");
        }
        // 验证
        chatUserDeletedService.register(phone);
        // 执行注册
        ShiroUserVo shiroUserVo = this.doRegister(phone, email);
        // 注册推送
        hookService.handle(PushAuditEnum.USER_REGISTER);
        // 注册推送
        chatRobotReplyService.subscribe(shiroUserVo.getUserId());
        // 返回
        return new AuthVo00(shiroUserVo.getToken());
    }

    @Override
    public AuthVo05 getQrCode() {
        // 生成token
        String token = RandomUtil.randomString(64);
        Integer expired = 60;
        // 存储token
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_SCAN, token);
        redisUtils.set(redisKey, "0", expired + 5, TimeUnit.SECONDS);
        return new AuthVo05(token, expired);
    }

    @Override
    public void confirmQrCode(String token) {
        // 验证token
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_SCAN, token);
        if (!redisUtils.hasKey(redisKey)) {
            throw new BaseException("二维码已过期，请重新登录");
        }
        // 查询用户
        ChatUser chatUser = chatUserService.getById(ShiroUtils.getUserId());
        // 生成token
        ShiroUserVo shiroUserVo = new ShiroUserVo(chatUser);
        tokenService.generate(shiroUserVo);
        // 发送推送
        pushService.pushScan(token, shiroUserVo.getToken());
        // 推送通知
        PushFrom pushFrom = chatRobotService.getPushFrom(AppConstants.ROBOT_PUSH);
        // 组装消息
        String title = "扫码登录";
        String content = "登录成功";
        String remark = StrUtil.format("登录时间：{}", DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_FORMAT));
        PushBox pushBox = new PushBox(title, title, content, remark);
        // 推送消息
        pushService.pushSingle(pushFrom, ShiroUtils.getUserId(), JSONUtil.toJsonStr(pushBox), PushMsgTypeEnum.BOX);
    }

    /**
     * 执行登录
     */
    private String doLogin(ChatUser chatUser, UserLogEnum typeEnum) {
        ShiroUserVo shiroUser = ShiroUtils.getLoginUser();
        if (shiroUser == null) {
            shiroUser = new ShiroUserVo(chatUser);
        }
        // 生成新token
        tokenService.generate(shiroUser);
        // 移除次数
        redisUtils.delete(makeLoginKey(shiroUser.getPhone()));
        // 重置token
        chatUserTokenService.resetToken(shiroUser.getUserId(), shiroUser.getToken(), chatUser.getSpecial());
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), typeEnum);
        return shiroUser.getToken();
    }

    /**
     * 注册接口
     */
    private ShiroUserVo doRegister(String phone, String email) {
        String salt = CodeUtils.salt();
        String userNo = chatNumberService.queryNextNo();
        String portrait = chatPortraitService.queryUserPortrait();
        //String nickname = chatConfigService.queryConfig(ChatConfigEnum.SYS_NICKNAME).getStr();
        String nickname =wuxiaNameGenerator.generateRandomName();
        String password = CodeUtils.password();
        // 增加用户
        ChatUser chatUser = new ChatUser()
                .setPhone(phone)
                .setEmail(email)
                .setUserNo(userNo)
                .setNickname(StrUtil.isNotEmpty(nickname) ? nickname : phone)
                .setPortrait(portrait)
                .setGender(GenderEnum.MALE)
                .setBirthday(DateUtil.parseDate("1970-01-01"))
                .setProvince("北京市")
                .setCity("北京城区")
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(CodeUtils.md5(password), salt))
                .setPass(YesOrNoEnum.NO)
                .setAuth(ApproveEnum.NONE)
                .setBanned(YesOrNoEnum.NO)
                .setSpecial(YesOrNoEnum.NO)
                .setAbnormal(YesOrNoEnum.NO)
                .setPayment(YesOrNoEnum.NO)
                .setPrivacyNo(YesOrNoEnum.YES)
                .setPrivacyPhone(YesOrNoEnum.YES)
                .setPrivacyScan(YesOrNoEnum.YES)
                .setPrivacyCard(YesOrNoEnum.YES)
                .setPrivacyGroup(YesOrNoEnum.YES)
                .setCreateTime(DateUtil.date());
        chatUserService.add(chatUser);
        // 新增钱包
        walletInfoService.addWallet(chatUser.getUserId());
        // 新增用户
        chatUserInfoService.addInfo(chatUser.getUserId());
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.REGISTER);
        // 生成新token
        ShiroUserVo loginUser = new ShiroUserVo(chatUser);
        tokenService.generate(loginUser);
        // 新增token
        chatUserTokenService.resetToken(loginUser.getUserId(), loginUser.getToken(), YesOrNoEnum.NO);
        return loginUser;
    }


}
