package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.common.utils.IpUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.utils.filter.FilterUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserDao;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.*;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.common.service.HookService;
import com.platform.modules.common.service.MessageService;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 服务层实现
 * </p>
 */
@Slf4j
@Service("chatUserService")
@CacheConfig(cacheNames = AppConstants.REDIS_CHAT_USER)
public class ChatUserServiceImpl extends BaseServiceImpl<ChatUser> implements ChatUserService {

    @Resource
    private ChatUserDao chatUserDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private MessageService messageService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatUserDeletedService chatUserDeletedService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private ChatRefreshService chatRefreshService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatUserTokenService chatUserTokenService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Resource
    private HookService hookService;

    @Resource
    private ChatResourceService chatResourceService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserDao);
    }

    @Override
    @Cacheable(key = "#userId", unless = "#result == null")
    public ChatUser getById(Long userId) {
        return super.getById(userId);
    }

    @Override
    public Integer updateById(ChatUser chatUser) {
        Integer result = super.updateById(chatUser);
        this.clearCache(chatUser.getUserId());
        return result;
    }

    @Override
    public List<ChatUser> queryList(ChatUser t) {
        List<ChatUser> dataList = chatUserDao.queryList(t);
        return dataList;
    }

    @Override
    public Dict sendCode(String phone, String email,String safestr, MessageTypeEnum messageType) {
        // 查询
        ChatUser chatUser = this.queryByPhone(phone);
        // 分发
        switch (messageType) {
            case REGISTER:
                if (chatUser != null) {
                    throw new BaseException("当前账号已存在，注册失败");
                }
                // 校验注册
                verifyRegister();
                break;
            case LOGIN:
                // 新系统
                if (PlatformConfig.EMAIL) {
                    if (chatUser == null) {
                        throw new BaseException("当前账号不存在，发送失败");
                    }
                }
                // 旧系统
                else if (chatUser == null) {
                    // 校验注册
                    verifyRegister();
                }
                break;
            case FORGET:
                if (chatUser == null) {
                    throw new BaseException("当前账号不存在，发送失败");
                }
                String  safe=chatUser.getSafestr();
                if(!safe.equals(safestr)){
                    throw new BaseException("安全密码校验失败");
                }
                break;
            case WALLET:
            case EMAIL:
            case RETRIEVE:
            case BINDING:
                break;
        }
        YesOrNoEnum special = YesOrNoEnum.NO;
        if (chatUser != null) {
            email = chatUser.getEmail();
            special = chatUser.getSpecial();
        }
        return messageService.sendSms(phone, email, messageType, special);
    }

    /**
     * 校验注册
     */
    private void verifyRegister() {
        ChatConfig chatConfig2 = chatConfigService.queryConfig(ChatConfigEnum.USER_REGISTER);
        if (!YesOrNoEnum.transform(chatConfig2.getYesOrNo())) {
            throw new BaseException("当前系统已停止新用户注册");
        }
    }

    @Override
    public ChatUser queryByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        return this.queryOne(new ChatUser().setPhone(phone));
    }

    @Transactional
    @Override
    public void resetPass(String phone, String pass) {
        // 查询用户
        ChatUser chatUser = chatUserService.queryByPhone(phone);
        if (chatUser == null) {
            throw new BaseException("账号不存在");
        }
        String salt = CodeUtils.salt();
        String password = CodeUtils.credentials(pass, salt);
        this.updateById(new ChatUser(chatUser.getUserId())
                .setSalt(salt)
                .setPassword(password)
                .setPass(YesOrNoEnum.YES));
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.PWD_RESET);
    }

    @Transactional
    @Override
    public void editPass(String oldPwd, String newPwd) {
        // 当前用户
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = chatUserService.getById(current);
        if (!CodeUtils.credentials(oldPwd, chatUser.getSalt()).equalsIgnoreCase(chatUser.getPassword())) {
            throw new BaseException("旧密码不正确");
        }
        String salt = CodeUtils.salt();
        ChatUser cu = new ChatUser(current)
                .setSalt(salt)
                .setPass(YesOrNoEnum.YES)
                .setPassword(CodeUtils.credentials(newPwd, salt));
        this.updateById(cu);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.PWD_EDIT);
        // 通知推送
        pushSetting(ChatUser.LABEL_PASS, YesOrNoEnum.YES.getCode());
    }

    @Transactional
    @Override
    public void setPass(MineVo04 mineVo) {
        // 当前用户
        Long current = ShiroUtils.getUserId();
        // 校验
        ChatUser chatUser = chatUserService.getById(current);
        if (YesOrNoEnum.YES.equals(chatUser.getPass())) {
            // 缓存
            this.clearCache(current);
            // 通知推送
            pushSetting(ChatUser.LABEL_PASS, YesOrNoEnum.YES.getCode());
            return;
        }
        String password = mineVo.getPassword();
        // 更新用户
        String salt = CodeUtils.salt();
        ChatUser cu = new ChatUser(current)
                .setSalt(salt)
                .setPass(YesOrNoEnum.YES)
                .setPassword(CodeUtils.credentials(password, salt));
        chatUserService.updateById(cu);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PWD_SET);
        // 通知推送
        pushSetting(ChatUser.LABEL_PASS, YesOrNoEnum.YES.getCode());
    }

    @Transactional
    @Override
    public void editIntro(String intro) {
        // 执行修改
        Long current = ShiroUtils.getUserId();
        // 更新数据
        this.update(Wrappers.<ChatUser>lambdaUpdate()
                .set(ChatUser::getIntro, FilterUtils.filter(intro, PlatformConfig.FILTER))
                .eq(ChatUser::getUserId, current));
        // 清除缓存
        this.clearCache(current);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.INTRO, intro);
        // 通知推送
        pushSetting(ChatUser.LABEL_INTRO, intro);
    }

    @Transactional
    @Override
    public void editCity(MineVo08 mineVo) {
        Long current = ShiroUtils.getUserId();
        String province = mineVo.getProvince();
        String city = mineVo.getCity();
        ChatUser chatUser = new ChatUser(current)
                .setProvince(province)
                .setCity(city);
        this.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.CITY, StrUtil.format("{}-{}", province, city));
        // 通知推送
        pushSetting(ChatUser.LABEL_CITY, province + "&" + city);
    }

    @Transactional
    @Override
    public void editBirthday(Date birthday) {
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = new ChatUser(current)
                .setBirthday(birthday);
        this.updateById(chatUser);
        // 新增日志
        String value = DateUtil.format(birthday, DatePattern.NORM_DATE_PATTERN);
        chatUserLogService.addLog(current, UserLogEnum.BIRTHDAY, value);
        // 通知推送
        pushSetting(ChatUser.LABEL_BIRTHDAY, value);
    }

    @Transactional
    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        // 执行修改
        ChatUser chatUser = new ChatUser(current)
                .setPortrait(portrait);
        this.updateById(chatUser);
        // 删除头像
        chatResourceService.delResource(portrait);
        // 刷新头像
        chatRefreshService.refreshPortrait(portrait);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.PORTRAIT, portrait);
        // 通知推送
        pushSetting(ChatUser.LABEL_PORTRAIT, portrait);
    }

    @Transactional
    @Override
    public void editNickname(MineVo03 mineVo) {
        String nickname = FilterUtils.filter(mineVo.getNickname(), PlatformConfig.FILTER);
        // 执行修改
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = new ChatUser(current)
                .setNickname(nickname);
        this.updateById(chatUser);
        // 刷新昵称
        chatRefreshService.refreshNickname(nickname);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.NICKNAME, mineVo.getNickname());
        // 通知推送
        pushSetting(ChatUser.LABEL_NICKNAME, nickname);
    }

    @Transactional
    @Override
    public void editGender(GenderEnum gender) {
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = new ChatUser(current)
                .setGender(gender);
        this.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.GENDER, gender.getInfo());
        // 通知推送
        pushSetting(ChatUser.LABEL_GENDER, gender.getCode());
    }

    @Transactional
    @Override
    public void editPrivacyNo(MineVo15 mineVo) {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum privacyNo = mineVo.getPrivacyNo();
        ChatUser chatUser = new ChatUser(current)
                .setPrivacyNo(privacyNo);
        chatUserService.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PRIVACY_NO, YesOrNoEnum.transform(privacyNo) ? "开启" : "关闭");
        // 通知推送
        pushSetting(ChatUser.LABEL_PRIVACY_NO, privacyNo.getCode());
    }

    @Transactional
    @Override
    public void editPrivacyPhone(MineVo14 mineVo) {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum privacyPhone = mineVo.getPrivacyPhone();
        ChatUser chatUser = new ChatUser(current)
                .setPrivacyPhone(privacyPhone);
        chatUserService.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PRIVACY_PHONE, YesOrNoEnum.transform(privacyPhone) ? "开启" : "关闭");
        // 通知推送
        pushSetting(ChatUser.LABEL_PRIVACY_PHONE, privacyPhone.getCode());
    }

    @Transactional
    @Override
    public void editPrivacyScan(MineVo13 mineVo) {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum privacyScan = mineVo.getPrivacyScan();
        ChatUser chatUser = new ChatUser(current)
                .setPrivacyScan(privacyScan);
        chatUserService.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PRIVACY_SCAN, YesOrNoEnum.transform(privacyScan) ? "开启" : "关闭");
        // 通知推送
        pushSetting(ChatUser.LABEL_PRIVACY_SCAN, privacyScan.getCode());
    }

    @Transactional
    @Override
    public void editPrivacyCard(MineVo11 mineVo) {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum privacyCard = mineVo.getPrivacyCard();
        ChatUser chatUser = new ChatUser(current)
                .setPrivacyCard(privacyCard);
        chatUserService.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PRIVACY_CARD, YesOrNoEnum.transform(privacyCard) ? "开启" : "关闭");
        // 通知推送
        pushSetting(ChatUser.LABEL_PRIVACY_CARD, privacyCard.getCode());
    }

    @Transactional
    @Override
    public void editPrivacyGroup(MineVo10 mineVo) {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum privacyGroup = mineVo.getPrivacyGroup();
        ChatUser chatUser = new ChatUser(current)
                .setPrivacyGroup(privacyGroup);
        chatUserService.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.PRIVACY_GROUP, YesOrNoEnum.transform(privacyGroup) ? "开启" : "关闭");
        // 通知推送
        pushSetting(ChatUser.LABEL_PRIVACY_GROUP, privacyGroup.getCode());
    }

    @Override
    public MineVo06 getInfo() {
        Long current = ShiroUtils.getUserId();
        String sign = ShiroUtils.getSign();
        ChatUser chatUser = findById(current);
        return MineVo06.init(chatUser, sign);
    }

    @Transactional
    @Override
    public void deleted() {
        try {
            Long current = ShiroUtils.getUserId();
            String phone = ShiroUtils.getPhone();
            // 写入注销
            chatUserDeletedService.deleted(current, phone);
            // 删除登录信息
            chatUserTokenService.deleted(current);
            // 新增日志
            chatUserLogService.addLog(current, UserLogEnum.DELETED);
            // 删除用户
            this.deleteById(current);
            // 清空缓存
            this.clearCache(current);
            // 执行退出
            ShiroUtils.getSubject().logout();
            log.info("注销成功。。。。");
        } catch (Exception ex) {
            log.error("注销异常", ex);
        }
    }

    @Override
    public void logout() {
        try {
            String token = ShiroUtils.getToken();
            Long userId = ShiroUtils.getUserId();
            // 删除登录
            chatUserTokenService.logout(token);
            // 新增日志
            chatUserLogService.addLog(userId, UserLogEnum.LOGOUT);
            // 执行退出
            ShiroUtils.getSubject().logout();
            log.info("退出成功。。。。");
        } catch (Exception ex) {
            log.error("退出异常", ex);
        }
    }

    @Transactional
    @Override
    public void refresh() {
        // 获取当前用户
        Long current = ShiroUtils.getUserId();
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.REFRESH);
        // 当前IP
        String ip = getIpAddr();
        // 刷新用户
        ThreadUtil.execAsync(() -> {
            String ipAddr = IpUtils.getIpAddr(ip);
            ChatUser chatUser = new ChatUser(current)
                    .setIp(ip)
                    .setIpAddr(ipAddr)
                    .setAbnormal(_abnormal(current, ipAddr))
                    .setOnLine(DateUtil.date());
            // 更新
            this.updateById(chatUser);
        });
    }

    /**
     * 异常账号
     */
    private YesOrNoEnum _abnormal(Long userId, String ipAddr) {
        // 查询
        ChatUser chatUser = this.getById(userId);
        // 忽略
        if (YesOrNoEnum.REFUND.equals(chatUser.getAbnormal())) {
            return YesOrNoEnum.REFUND;
        }
        // 异常
        if (!StringUtils.isEmpty(ipAddr) && !ipAddr.contains("内网IP")) {
            if (ipAddr.contains("香港") || ipAddr.contains("台湾") || ipAddr.contains("澳门") || !ipAddr.contains("中国")) {
                hookService.handle(PushAuditEnum.APPLY_SPECIAL);
                return YesOrNoEnum.YES;
            }
        }
        // 正常
        return YesOrNoEnum.NO;
    }

    @Override
    @Transactional
    public void setPayment() {
        Long current = ShiroUtils.getUserId();
        YesOrNoEnum payment = YesOrNoEnum.YES;
        ChatUser chatUser = new ChatUser(current)
                .setPayment(payment);
        this.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.PAYMENT);
        // 通知推送
        pushSetting(ChatUser.LABEL_PAYMENT, payment.getCode());
    }

    @Override
    public void editEmail(String email) {
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = new ChatUser(current)
                .setEmail(email);
        this.updateById(chatUser);
        // 新增日志
        chatUserLogService.addLog(chatUser.getUserId(), UserLogEnum.EMAIL, email);
        // 通知推送
        pushSetting(ChatUser.LABEL_EMAIL, DesensitizedUtil.email(email));
    }

    /**
     * 获取ip
     */
    private String getIpAddr() {
        // 请求
        HttpServletRequest request = ServletUtils.getRequest();
        // 获取ip
        return IpUtils.getIpAddr(request);
    }

    /**
     * 移除缓存
     */
    private void clearCache(Long userId) {
        redisUtils.delete(AppConstants.REDIS_CHAT_USER + "::" + userId);
    }

    // 通知推送
    private void pushSetting(String label, String value) {
        Long current = ShiroUtils.getUserId();
        PushSetting setting = new PushSetting(PushSettingEnum.MINE, current, label, value);
        pushService.pushSetting(setting, Arrays.asList(current));
    }

}
