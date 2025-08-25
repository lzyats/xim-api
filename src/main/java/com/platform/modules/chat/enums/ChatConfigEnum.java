package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 配置枚举
 */
@Getter
public enum ChatConfigEnum {

    /**
     * 申请好友单日次数
     */
    APPLY_FRIEND("apply_friend"),
    /**
     * 申请群组单日次数
     */
    APPLY_GROUP("apply_group"),
    /**
     * 群组成员默认数量
     */
    GROUP_LEVEL_COUNT("group_level_count"),
    /**
     * 群组名称搜索开关
     */
    GROUP_NAME_SEARCH("group_name_search"),
    /**
     * 系统通告
     */
    NOTICE_CONTENT("notice_content"),
    /**
     * 系统通告类型
     */
    NOTICE_NOTYPE("notice_notype"),
    /**
     * 系统通告开关
     */
    NOTICE_STATUS("notice_status"),
    /**
     * 审核开关
     */
    SYS_AUDIT("sys_audit"),
    /**
     * 备案信息
     */
    SYS_BEIAN("sys_beian"),
    /**
     * 系统验证码
     */
    SYS_CAPTCHA("sys_captcha"),
    /**
     * 货币单位
     */
    SYS_CASHNAME("sys_cashname"),
    /**
     * 货币符号
     */
    SYS_CASHSTR("sys_cashstr"),
    /**
     *  批量好友ID
     */
    SYS_FRIENDS("sys_friends"),
    /**
     * webHook地址
     */
    SYS_HOOK("sys_hook"),
    /**
     * 邀请注册
     */
    SYS_INVO("sys_invo"),
    /**
     * 邀请注册是否自动加好友
     */
    SYS_INVOADUS("sys_invoadus"),
    /**
     * 注册昵称
     */
    SYS_NICKNAME("sys_nickname"),
    /**
     * 红包金额
     */
    SYS_PACKET("sys_packet"),
    /**
     * 审核账号
     */
    SYS_PHONE("sys_phone"),
    /**
     * 系统名称
     */
    SYS_PROJECT("sys_project"),
    /**
     * 撤回时间
     */
    SYS_RECALL("sys_recall"),
    /**
     * 系统截屏
     */
    SYS_SCREENSHOT("sys_screenshot"),
    /**
     * 注册后是补发朋友圈
     */
    SYS_SENDMOMENT("sys_sendmoment"),
    /**
     * 分享页面
     */
    SYS_SHARE("sys_share"),
    /**
     *  签到日奖励
     */
    SYS_SIGN("sys_sign"),
    /**
     *  签到奖励入钱包
     */
    SYS_SIGNTOAL("sys_signtoal"),
    /**
     * 系统水印
     */
    SYS_WATERMARK("sys_watermark"),
    /**
     * 用户注销间隔
     */
    USER_DELETED("user_deleted"),
    /**
     * 用户手持身份证
     */
    USER_HOLD("user_hold"),
    /**
     * 用户注册开关
     */
    USER_REGISTER("user_register"),
    /**
     * 用户短信开关
     */
    USER_SMS("user_sms"),
    /**
     * 钱包提现认证开关
     */
    WALLET_CASH_AUTH("wallet_cash_auth"),
    /**
     * 钱包提现服务费用
     */
    WALLET_CASH_COST("wallet_cash_cost"),
    /**
     * 钱包提现单日次数
     */
    WALLET_CASH_COUNT("wallet_cash_count"),
    /**
     * 钱包提现单日最大金额
     */
    WALLET_CASH_MAX("wallet_cash_max"),
    /**
     * 钱包提现单日最小金额
     */
    WALLET_CASH_MIN("wallet_cash_min"),
    /**
     * 钱包提现手续费比率
     */
    WALLET_CASH_RATE("wallet_cash_rate"),
    /**
     * 钱包提现汇率
     */
    WALLET_CASH_RATES("wallet_cash_rates"),
    /**
     * 钱包提现提醒消息
     */
    WALLET_CASH_REMARK("wallet_cash_remark"),
    /**
     * 钱包充值单日总金额
     */
    WALLET_RECHARGE_AMOUNT("wallet_recharge_amount"),
    /**
     * 钱包充值安卓开关
     */
    WALLET_RECHARGE_ANDROID("wallet_recharge_android"),
    /**
     * 钱包充值单日单人次数
     */
    WALLET_RECHARGE_COUNT("wallet_recharge_count"),
    /**
     * 钱包充值时间限制
     */
    WALLET_RECHARGE_END("wallet_recharge_end"),
    /**
     * 钱包充值苹果开关
     */
    WALLET_RECHARGE_IOS("wallet_recharge_ios"),
    /**
     * 钱包充值时间限制
     */
    WALLET_RECHARGE_START("wallet_recharge_start"),
    /**
     * 钱包充值单日总笔数
     */
    WALLET_RECHARGE_TOTAL("wallet_recharge_total"),
    ;

    @EnumValue
    @JsonValue
    private String code;

    // 构造方法
    ChatConfigEnum(String code) {
        this.code = code;
    }

}
