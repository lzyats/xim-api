package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 朋友圈点赞表实体类
 * </p>
 */
@Data
@TableName("friend_likes")
@Accessors(chain = true) // 链式调用
public class FriendLikes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞ID
     */
    @TableId
    private Long likeId;
    /**
     * 关联动态ID
     */
    private Long momentId;
    /**
     * 点赞用户ID
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 逻辑删除标记
     */
    private Integer isDeleted;

}
