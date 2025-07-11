package com.platform.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 文件类型枚举
 */
@Getter
public enum FileTypeEnum {

    /**
     * 图片/拍照
     */
    IMAGE("image", "图片/拍照"),
    /**
     * 视频
     */
    VIDEO("video", "视频"),
    /**
     * 其他
     */
    OTHER("other", "其他"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    FileTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
