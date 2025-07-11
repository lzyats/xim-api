package com.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.FriendSourceEnum;
import com.platform.modules.chat.enums.FriendTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 好友详情
 */
@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class FriendVo09 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 性别1男2女
     */
    private GenderEnum gender;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 置顶
     */
    private YesOrNoEnum top = YesOrNoEnum.NO;
    /**
     * 静默
     */
    private YesOrNoEnum disturb = YesOrNoEnum.NO;
    /**
     * 是否黑名单
     */
    private YesOrNoEnum black = YesOrNoEnum.NO;
    /**
     * 好友类型
     */
    private FriendTypeEnum friendType = FriendTypeEnum.OTHER;
    /**
     * 好友来源
     */
    private FriendSourceEnum friendSource = FriendSourceEnum.OTHER;

    public FriendVo09(ChatUser chatUser) {
        this.userId = chatUser.getUserId();
        this.nickname = chatUser.getNickname();
        this.portrait = chatUser.getPortrait();
        this.userNo = chatUser.getUserNo();
        this.gender = chatUser.getGender();
        this.intro = chatUser.getIntro();
    }

}
