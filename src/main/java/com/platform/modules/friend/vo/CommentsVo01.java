package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true) // 链式调用
public class CommentsVo01 {

    /** 媒体类型 */
    private boolean  source;

    /** 媒体资源 */
    private String fromUser;

    /** 缩略图 */
    private String toUser;

    /** 缩略图 */
    private String content;

}
