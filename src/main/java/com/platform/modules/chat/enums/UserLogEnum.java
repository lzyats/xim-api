package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户日志类型
 */
@Getter
public enum UserLogEnum {

    /**
     * 用户注册
     */
    REGISTER("1001", "用户注册"),
    /**
     * 密码登录
     */
    LOGIN_PWD("1002", "密码登录"),
    /**
     * 验证码登录
     */
    LOGIN_CODE("1003", "验证码登录"),
    /**
     * 重置密码
     */
    PWD_RESET("1004", "重置密码"),
    /**
     * 退出登录
     */
    LOGOUT("1005", "退出登录"),
    /**
     * 建议反馈
     */
    FEEDBACK("1006", "建议反馈"),
    /**
     * 设置密码
     */
    PWD_SET("1007", "设置密码"),
    /**
     * 修改密码
     */
    PWD_EDIT("1008", "修改密码"),
    /**
     * 修改头像
     */
    PORTRAIT("1009", "修改头像"),
    /**
     * 修改昵称
     */
    NICKNAME("1010", "修改昵称"),
    /**
     * 修改性别
     */
    GENDER("1011", "修改性别"),
    /**
     * 修改签名
     */
    INTRO("1012", "修改签名"),
    /**
     * 修改城市
     */
    CITY("1013", "修改城市"),
    /**
     * 修改生日
     */
    BIRTHDAY("1014", "修改生日"),
    /**
     * 修改隐私号码
     */
    PRIVACY_NO("1015", "修改隐私号码"),
    /**
     * 修改隐私手机
     */
    PRIVACY_PHONE("1016", "修改隐私手机"),
    /**
     * 修改隐私扫码
     */
    PRIVACY_SCAN("1017", "修改隐私扫码"),
    /**
     * 修改隐私名片
     */
    PRIVACY_CARD("1018", "修改隐私名片"),
    /**
     * 修改隐私群组
     */
    PRIVACY_GROUP("1019", "修改隐私群组"),
    /**
     * 举报他人
     */
    INFORM_FROM("1020", "举报他人"),
    /**
     * 被他人举报
     */
    INFORM_TO("1021", "被他人举报"),
    /**
     * 封禁账号
     */
    BANNED("1022", "封禁账号"),
    /**
     * 解除封禁
     */
    BANNED_REMOVE("1023", "解除封禁"),
    /**
     * 申请解封
     */
    BANNED_APPEAL("1024", "申请解封"),
    /**
     * 支付密码
     */
    PAYMENT("1025", "支付密码"),
    /**
     * 用户刷新
     */
    REFRESH("1026", "用户刷新"),
    /**
     * 修改邮箱
     */
    EMAIL("1027", "修改邮箱"),
    /**
     * 用户注销
     */
    DELETED("9999", "用户注销"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    UserLogEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
