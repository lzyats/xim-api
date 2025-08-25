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
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatFriendApply;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.FriendSourceEnum;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.service.impl.ChatNoticeServiceImpl;
import com.platform.modules.common.service.HookService;
import com.platform.modules.common.vo.CommonVo06;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.vo.MomentVo01;
import com.platform.modules.push.dto.PushBox;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.service.WalletInfoService;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.platform.modules.common.service.CommonService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.platform.modules.auth.util.WuxiaNameGenerator;

import com.platform.modules.chat.domain.ChatUserInv;
import org.springframework.web.bind.annotation.GetMapping;

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
    private CommonService commonService;

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

    @Resource
    private ChatUserInvService chatUserInvService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private FriendMomentsService friendMomentsService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private SmsService smsService;

    // 注入Spring管理的XianxiaNameGenerator实例
    @Autowired
    private WuxiaNameGenerator wuxiaNameGenerator;

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);



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
            ShiroUserVo shiroUserVo = this.doRegister(phone, null,null,"123456","666666",null);
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
        // 昵称
        String nickname=authVo.getNickname();
        // 邮箱
        String email = authVo.getEmail();
        // 密码
        String pass = authVo.getPass();
        // 安全码
        String safestr = authVo.getSafestr();
        // 邀请码
        String incodes=authVo.getIncode();
        // 查询用户
        ChatUser chatUser = chatUserService.queryByPhone(phone);
        if (chatUser != null) {
            throw new BaseException("当前账号已存在，注册失败");
        }
        // 验证
        chatUserDeletedService.register(phone);
        // 执行注册
        ShiroUserVo shiroUserVo = this.doRegister(phone,nickname, email,pass,safestr,incodes);
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
        // 开始登录
        log.info("开始登录");
        // 生成新token
        tokenService.generate(shiroUser);
        log.info("token:｛｝",shiroUser.getToken());
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
    private ShiroUserVo doRegister (String phone, String nickname,String email,String pass,String safestr,String incodes) {
        String salt = CodeUtils.salt();
        String userNo = chatNumberService.queryNextNo();
        String portrait = chatPortraitService.queryUserPortrait();
        //String nickname = chatConfigService.queryConfig(ChatConfigEnum.SYS_NICKNAME).getStr();
        if(nickname==null || nickname.isEmpty())  nickname =wuxiaNameGenerator.generateRandomName();
        String incode=wuxiaNameGenerator.generateInvitationCode();
        String password = pass;
        if(pass.isEmpty()){
            password = CodeUtils.password();
        }

        Integer userDep=0;
        String userLevel="";
        Long parentId=0L;
        String parentNo="";

        boolean doinv=false;
        ChatUserInv chatUserInv=new ChatUserInv()
                .setCreateTime(DateUtil.date());
                ;
        // 执行邀请
        ChatUser chatUsert=new ChatUser();
        if(incodes!=null || !incode.isEmpty()){
            //查询邀请码推荐人信息
            chatUsert=chatUserService.queryByIncode(incodes);
            logger.info("获取推荐人信息，data: {}", chatUsert);
            // 查询结果不为空则处理
            if(chatUsert!=null){
                // 当前用户的user_dep
                userDep=chatUsert.getUserDep()+1;
                String userLevel1=chatUsert.getUserLevel();
                parentId=chatUsert.getUserId();
                chatUserInv.setUserNo(chatUsert.getUserNo());
                chatUserInv.setUserInid(parentId);
                chatUserInv.setPhone(chatUsert.getPhone());
                chatUserInv.setNickname(chatUsert.getNickname());
                if( userLevel1==null || userLevel1.isEmpty()){
                    userLevel=parentId.toString();
                }else{
                    userLevel= userLevel1 + "|"+parentId;
                }
                doinv=true;
            }
        }
        log.info("<添加新用户>");
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
                .setSafestr(safestr)
                .setIncode(incode)
                .setUserLevel(userLevel)
                .setUserDep(userDep)
                .setParentId(parentId)
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
        // 插入邀请数据
        if(doinv){
            log.info("<获取系统推荐奖励>");
            //获取系统推荐奖励
            //CommonVo06 vo06= commonService.getConfig();
            double getInvo=chatConfigService.queryConfig(ChatConfigEnum.SYS_INVO).getBigDecimal().doubleValue();
            chatUserInv.setUserId(loginUser.getUserId());
            chatUserInv.setInvUsdt(getInvo);
            chatUserInvService.invode(chatUserInv);
            //执行自动加好友
            String adduser = chatConfigService.queryConfig(ChatConfigEnum.SYS_INVOADUS).getYesOrNo().getCode();
            log.info("<是否自动加好友>{}",adduser);
            if(adduser==null || adduser.isEmpty()){

            }else{
                if(adduser.equals("Y")){
                    // 调用提取后的自动加好友方法
                    autoAddFriend(chatUser, chatUsert);
                }
            }
        }
        //自动添加好友
        String addfriendlist = chatConfigService.queryConfig(ChatConfigEnum.SYS_FRIENDS).getStr();
        if(addfriendlist!=null && !addfriendlist.isEmpty()){
            //异步执行
            ThreadUtil.execAsync(() -> {
                try {
                    // 按逗号分割字符串为数组（处理可能的空格，如"11028524, 21228991"）
                    String[] friendIds = addfriendlist.split("\\s*,\\s*");
                    // 遍历所有元素
                    for (String friendIdStr : friendIds) {
                        // 跳过空字符串（避免分割后出现空元素）
                        if (friendIdStr == null || friendIdStr.trim().isEmpty()) {
                            continue;
                        }
                        // 转换为Long类型（根据实际业务类型调整，如需要int则用Integer.parseInt）
                        String friendId = friendIdStr.trim();
                        ChatUser chatUser1 = chatUserService.queryOne(new ChatUser().setUserNo(friendId));
                        autoAddFriend(chatUser, chatUser1);
                        // 例如：添加好友、发送通知等
                        log.info("处理好友ID: {}", friendId);
                    }
                } catch (NumberFormatException e) {
                    log.error("好友ID格式错误，字符串: {}", addfriendlist, e);
                } catch (Exception e) {
                    log.error("处理好友列表异常", e);
                }
            });
        }
        //补发所有朋友圈信息
        String sendmoment = chatConfigService.queryConfig(ChatConfigEnum.SYS_SENDMOMENT).getYesOrNo().getCode();
        if(sendmoment.equals("Y")){
            //异步执行
            ThreadUtil.execAsync(() -> {
                friendMomentsService.pushlistdata(chatUser,loginUser);
            });
        }
        // 新增token
        chatUserTokenService.resetToken(loginUser.getUserId(), loginUser.getToken(), YesOrNoEnum.NO);
        return loginUser;
    }

    /**
     * 自动添加双向好友关系（提取后的独立方法）
     * @param chatUser 当前用户信息
     * @param chatUsert 推荐人用户信息
     */
    private void autoAddFriend(ChatUser chatUser, ChatUser chatUsert) {
        Long current = chatUser.getUserId();
        Long userId = chatUsert.getUserId();
        // 生成群组ID（双向好友共用一个群组ID）
        Long groupId = IdWorker.getId();
        FriendSourceEnum source = FriendSourceEnum.SELF;

        // 1. 创建当前用户 -> 推荐人的好友记录
        ChatFriend friend1 = new ChatFriend()
                .setCurrentId(current)
                .setGroupId(groupId)
                .setUserId(userId)
                .setNickname(chatUsert.getNickname())
                .setPortrait(chatUsert.getPortrait())
                .setUserNo(chatUsert.getUserNo())
                .setSource(source)
                .setBlack(YesOrNoEnum.NO)
                .setDisturb(YesOrNoEnum.NO)
                .setTop(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        chatFriendService.add(friend1);

        // 2. 创建推荐人 -> 当前用户的好友记录（双向）
        ChatFriend friend2 = new ChatFriend()
                .setCurrentId(userId)
                .setGroupId(groupId)
                .setUserId(current)
                .setNickname(chatUser.getNickname())
                .setPortrait(chatUser.getPortrait())
                .setUserNo(chatUser.getUserNo())
                .setSource(source)
                .setBlack(YesOrNoEnum.NO)
                .setDisturb(YesOrNoEnum.NO)
                .setTop(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        chatFriendService.add(friend2);

        // 3. 发送新好友通知（当前用户）
        String content = AppConstants.TIPS_FRIEND_NEW;
        pushService.pushSingle(friend1.getPushFrom(IdWorker.getId()), Arrays.asList(current), content, PushMsgTypeEnum.TIPS);

        // 4. 发送新好友通知（推荐人）
        pushService.pushSingle(friend2.getPushFrom(IdWorker.getId()), Arrays.asList(userId), content, PushMsgTypeEnum.TIPS);

        // 5. 推送好友设置通知（双向）
        chatFriendService.pushSetting(current, userId, ChatFriend.LABEL_CREATE, "");
        chatFriendService.pushSetting(userId, current, ChatFriend.LABEL_CREATE, "");
    }

}
