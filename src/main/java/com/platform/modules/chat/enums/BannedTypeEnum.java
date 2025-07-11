package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 举报类型
 */
@Getter
public enum BannedTypeEnum {

    /**
     * 1->涉及诽谤/辱骂/威胁/挑衅
     */
    ABUSE("1", "涉及诽谤/辱骂/威胁/挑衅"),
    /**
     * 2->涉嫌广告推销
     */
    ADVERTISING("2", "涉嫌广告推销"),
    /**
     * 3->涉嫌色情暴力
     */
    PORN("3", "涉嫌色情暴力"),
    /**
     * 4->涉嫌反动/诈骗/谣言
     */
    FRAUD("4", "涉嫌反动/诈骗/谣言"),
    /**
     * 5->涉及散布他人隐私
     */
    PRIVACY("5", "涉及散布隐私"),
    /**
     * 6->其他违规行为
     */
    OTHER("6", "其他违规行为"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    BannedTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
