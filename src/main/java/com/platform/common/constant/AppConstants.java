package com.platform.common.constant;

import java.math.BigDecimal;

/**
 * 通用常量信息
 */
public class AppConstants {

    /**
     * 机器人-在线客服
     */
    public static final Long ROBOT_ID = 10001L;

    /**
     * 机器人-支付助手
     */
    public static final Long ROBOT_PAY = 10002L;

    /**
     * 机器人-羊驼助手
     */
    public static final Long ROBOT_PUSH = 10003L;

    /**
     * 消息条数
     */
    public static final Integer MESSAGE_LIMIT = 200;

    /**
     * 文件预览
     */
    public static String PREVIEW = "/file/**";

    /**
     * 图标
     */
    public static String FAVICON = "/favicon.ico";

    /**
     * 提示
     */
    public static final String TIPS_FRIEND_NONE = "对方不是你的好友";

    /**
     * 提示
     */
    public static final String TIPS_FRIEND_NEW = "你们已经是好友啦，现在开始聊天吧";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_CREATE_MASTER = "你创建了群聊[{}]";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_CREATE_MEMBER = "你被[{}]邀请加入了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_LOGOUT = "[{}]退出了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_TRANSFER = "[{}]已成为新群主";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_MANAGER = "你已成为管理员";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_INVITE = "[{}]邀请[{}]加入了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_JOIN = "[{}]加入了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_KICKED = "你被[管理员]移出了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_DISSOLVE = "[群主]解散了群聊";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_NAME = "[{}]修改群名为[{}]";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_NOTICE = "[{}]修改群公告为[{}]";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_FORBID = "[{}]{}了群禁言";

    /**
     * 提示
     */
    public static final String TIPS_GROUP_PROTECT = "[{}][{}]了群成员保护";

    /**
     * 提示
     */
    public static final String TIPS_PACKET_FROM = "[{}]领取了你的红包";

    /**
     * 提示
     */
    public static final String TIPS_PACKET_RECEIVE = "你领取了[{}]的红包";

    /**
     * chat:snowflake
     */
    public static final String REDIS_CHAT_SNOWFLAKE = "chat:snowflake";

    /**
     * chat:user::{userId}
     */
    public static final String REDIS_CHAT_USER = "chat:user";

    /**
     * redis_admin
     */
    public static final String REDIS_CHAT_ADMIN = "chat:admin";

    /**
     * chat:pwd:{20240101}:{userId}
     */
    public static final String REDIS_CHAT_PWD = "chat:pwd:{}:{}";

    /**
     * chat:special:{type}
     */
    public static final String REDIS_CHAT_SPECIAL = "chat:special:{}";

    /**
     * chat:special:{url}
     */
    public static final String REDIS_CHAT_PORTRAIT = "chat:portrait:{}";

    /**
     * chat:wallet:{20240101}:{userId}
     */
    public static final String REDIS_CHAT_WALLET = "chat:wallet:{}:{}";

    /**
     * redis_chat_scan
     */
    public static final String REDIS_CHAT_SCAN = "chat:scan:{}";

    /**
     * chat:notice
     */
    public static final String REDIS_CHAT_NOTICE = "chat:notice";

    /**
     * chat:no
     */
    public static final String REDIS_CHAT_NO = "chat:no";

    /**
     * chat:group::{groupId}
     */
    public static final String REDIS_CHAT_GROUP = "chat:group";

    /**
     * chat:group:member::{groupId}:{userId}
     */
    public static final String REDIS_CHAT_GROUP_MEMBER = "chat:group:member";

    /**
     * chat:group:receive:{groupId}
     */
    public static final String REDIS_CHAT_GROUP_RECEIVE = "chat:group:receive:{}";

    /**
     * chat:robot::{robotId}
     */
    public static final String REDIS_CHAT_ROBOT = "chat:robot";

    /**
     * chat:friend::{userId}:{friendId}
     */
    public static final String REDIS_CHAT_FRIEND = "chat:friend";

    /**
     * 账户余额
     */
    public static final BigDecimal WALLET_BALANCE = new BigDecimal("9000000000.00");

    /**
     * chat:apply:friend:{20200201}:{userId}
     */
    public static final String REDIS_APPLY_FRIEND = "chat:apply:friend:{}:{}";

    /**
     * chat:apply:group:{20200201}:{userId}
     */
    public static final String REDIS_APPLY_GROUP = "chat:apply:group:{}:{}";

    /**
     * wallet:packet:split:{tradeId}
     */
    public static final String REDIS_WALLET_PACKET_SPLIT = "wallet:packet:split:{}";

    /**
     * wallet:packet:receive:{tradeId}:{userId}
     */
    public static final String REDIS_WALLET_PACKET_RECEIVE = "wallet:packet:receive:{}:{}";

    /**
     * wallet:recharge:{tradeNo}
     */
    public static final String REDIS_WALLET_RECHARGE = "wallet:recharge:{}";

    public static final String REDIS_WALLET_ROBOT = "wallet:cash";

}
