package com.platform.modules.chat.vo;

import com.platform.modules.chat.enums.FriendSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 搜索结果
 */
@Data
@Accessors(chain = true) // 链式调用
public class FriendVo01 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 好友来源
     */
    private FriendSourceEnum source;

}
