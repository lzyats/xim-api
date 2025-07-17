package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonCreator;
/**
 * <p>
 * 朋友圈评论表实体类
 * </p>
 */
@Data
@TableName("friend_comments")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class FriendComments extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId
    private  Long commentId;
    /**
     * 关联动态ID
     */
    private Long momentId;
    /**
     * 评论用户ID
     */
    private Long userId;
    /**
     * 回复的评论ID（可为空）
     */
    private Long replyTo;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 逻辑删除标记
     */
    private Integer isDeleted;
    /**
     * 是否为发贴者本人回复
     */
    private Integer source;

    public FriendComments(Long momentId) {
        this.momentId = momentId;
    }

    /**
     * 字段
     */
    public static final String LABEL_COMMENT_ID = "commentId";
    public static final String LABEL_MOMENT_ID = "momentId";
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_REPLY_TO = "replyTo";
    public static final String LABEL_CONTENT = "content";
    public static final String LABEL_IS_DELETED = "isDeleted";
    public static final String LABEL_CREATE_TIME = "createTime";
    public static final String LABEL_SOURCE = "source";

}
