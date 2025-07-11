package com.platform.modules.uni.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 小程序类型
 */
@Getter
public enum UniTypeEnum {

    /**
     * 网页
     */
    URL("url", "网页"),
    /**
     * 小程序
     */
    MINI("mini", "小程序"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    UniTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
