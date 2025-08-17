package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true) // 链式调用
public class CommentsVo01 {

    /** 是否发布者 */
    private boolean source;

    /** 评论人 */
    private String fromUser;

    /** 被评论人 */
    private String toUser;

    /** 评论内容 */
    private String content;

    public boolean getSource() {
        return source;
    }
}
