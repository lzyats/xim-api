package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true) // 链式调用
public class MediasVo02 {

    private Long momentId;

    /** 媒体类型 */
    private Integer type;

    /** 媒体资源 */
    private String url;

    /** 缩略图 */
    private String thumbnail;

}
