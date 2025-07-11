package com.platform.common.web.version;

import lombok.Getter;

/**
 * 版本枚举值
 */
@Getter
public enum VersionEnum {

    V1_0_0("1.0.0"),
    ;

    private final String code;

    VersionEnum(String code) {
        this.code = code;
    }

}
