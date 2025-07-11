package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.FriendSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 好友申请表实体类
 * </p>
 */
@Data
@TableName("chat_friend_apply")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatFriendApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long applyId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 聊天号码
     */
    private String userNo;
    /**
     * 申请理由
     */
    private String reason;
    /**
     * 接收id
     */
    private Long receiveId;
    /**
     * 接收备注
     */
    private String receiveRemark;
    /**
     * 申请状态
     */
    private ApproveEnum status;
    /**
     * 申请来源
     */
    private FriendSourceEnum source;
    /**
     * 创建时间
     */
    private Date createTime;

    public ChatFriendApply(Long userId, Long receiveId) {
        this.userId = userId;
        this.receiveId = receiveId;
    }

    /**
     * 字段
     */
    public static final String COLUMN_RECEIVE_ID = "receive_id";
    public static final String COLUMN_CREATE_TIME = "create_time";

}
