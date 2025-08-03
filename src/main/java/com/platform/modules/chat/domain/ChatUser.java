package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户表实体类
 * </p>
 */
@Data
@TableName("chat_user")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ChatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long userId;
    /**
     * 账号
     */
    private String phone;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 性别1男2女
     */
    private GenderEnum gender;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 盐
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码标志
     */
    private YesOrNoEnum pass;
    /**
     * 禁用标志
     */
    private YesOrNoEnum banned;
    /**
     * 特殊
     */
    private YesOrNoEnum special;
    /**
     * 异常(Y异常N正常R忽略)
     */
    private YesOrNoEnum abnormal;
    /**
     * 支付密码
     */
    private YesOrNoEnum payment;
    /**
     * 认证状态
     */
    private ApproveEnum auth;
    /**
     * 隐私no
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私账号
     */
    private YesOrNoEnum privacyPhone;
    /**
     * 隐私扫码
     */
    private YesOrNoEnum privacyScan;
    /**
     * 隐私名片
     */
    private YesOrNoEnum privacyCard;
    /**
     * 隐私群组
     */
    private YesOrNoEnum privacyGroup;
    /**
     * 微信id
     */
    private String loginWx;
    /**
     * 苹果id
     */
    private String loginIos;
    /**
     * qq
     */
    private String loginQq;
    /**
     * 在线时间
     */
    private Date onLine;
    /**
     * ip
     */
    private String ip;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 安全码
     */
    private String safestr;

    /**
     * 邀请码
     */
    private String incode;

    /**
     * 用户层级
     */
    private Integer userDep;

    /**
     * 层级关系表
     */
    private String userLevel;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 注册时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    @TableLogic
    private Integer deleted;

    public ChatUser(Long userId) {
        this.userId = userId;
    }

    // 当前登录对象
    public static ChatUser current() {
        return new ChatUser()
                .setUserId(ShiroUtils.getUserId())
                .setUserNo(ShiroUtils.getUserNo())
                .setNickname(ShiroUtils.getNickname())
                .setPortrait(ShiroUtils.getPortrait());
    }

    /**
     * 字段
     */
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_PAYMENT = "payment";
    public static final String LABEL_PASS = "pass";
    public static final String LABEL_BIRTHDAY = "birthday";
    public static final String LABEL_GENDER = "gender";
    public static final String LABEL_AUTH = "auth";
    public static final String LABEL_CITY = "city";
    public static final String LABEL_INTRO = "intro";
    public static final String LABEL_EMAIL = "email";
    public static final String LABEL_PRIVACY_GROUP = "privacyGroup";
    public static final String LABEL_PRIVACY_CARD = "privacyCard";
    public static final String LABEL_PRIVACY_SCAN = "privacyScan";
    public static final String LABEL_PRIVACY_PHONE = "privacyPhone";
    public static final String LABEL_PRIVACY_NO = "privacyNo";
    /**
     * 字段
     */
    public static final String COLUMN_CREATE_TIME = "create_time";

}
