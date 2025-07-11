package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 群组日志类型
 */
@Getter
public enum GroupLogEnum {

    /**
     * 创建群组
     */
    CREATE("1001", "创建群组"),
    /**
     * 转让群组
     */
    TRANSFER("1002", "转让群组"),
    /**
     * 设置管理员
     */
    MANAGER("1003", "设置管理员"),
    /**
     * 修改群名
     */
    GROUP_NAME("1004", "修改群名"),
    /**
     * 修改头像
     */
    PORTRAIT("1005", "修改头像"),
    /**
     * 修改公告
     */
    NOTICE("1006", "修改公告"),
    /**
     * 公告置顶
     */
    NOTICE_TOP("1007", "公告置顶"),
    /**
     * 解散群组
     */
    DISSOLVE("1008", "解散群组"),
    /**
     * 群组扩容
     */
    GROUP_SIZE("1009", "群组扩容"),
    /**
     * 成员保护
     */
    CONFIG_MEMBER("1010", "成员保护"),
    /**
     * 成员邀请
     */
    CONFIG_INVITE("1011", "成员邀请"),
    /**
     * 群组头衔
     */
    CONFIG_TITLE("1012", "群组头衔"),
    /**
     * 红包开关
     */
    CONFIG_PACKET("1013", "红包开关"),
    /**
     * 显示金额
     */
    CONFIG_AMOUNT("1014", "显示金额"),
    /**
     * 红包禁抢
     */
    CONFIG_RECEIVE("1015", "红包禁抢"),
    /**
     * 专属可见
     */
    CONFIG_ASSIGN("1016", "专属可见"),
    /**
     * 资源开关
     */
    CONFIG_MEDIA("1017", "资源开关"),
    /**
     * 全员禁言
     */
    CONFIG_SPEAK("1018", "全员禁言"),
    /**
     * 成员审核
     */
    CONFIG_AUDIT("1019", "成员审核"),
    /**
     * 群内昵称
     */
    CONFIG_NICKNAME("1020", "群内昵称"),
    /**
     * 隐私号码
     */
    PRIVACY_NO("1021", "隐私号码"),
    /**
     * 隐私扫码
     */
    PRIVACY_SCAN("1022", "隐私号码"),
    /**
     * 隐私名称
     */
    PRIVACY_NAME("1023", "隐私名称"),
    /**
     * 红包白名单
     */
    PACKET_WHITE("1024", "红包白名单"),
    /**
     * 二维码开关
     */
    CONFIG_SCAN("1025", "二维码开关"),
    /**
     * 封禁账号
     */
    BANNED("1026", "封禁群组"),
    /**
     * 解除封禁
     */
    BANNED_REMOVE("1027", "解除封禁"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    GroupLogEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
