package com.platform.modules.friend.vo;

import com.platform.modules.friend.domain.FriendComments;
import com.platform.modules.friend.domain.FriendMedias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true) // 链式调用
public class MediasVo01 {

    /** 媒体类型 */
    private Integer type;

    /** 媒体资源 */
    private String url;

    /** 缩略图 */
    private String thumbnail;
}
