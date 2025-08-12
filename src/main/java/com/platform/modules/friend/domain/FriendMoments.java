package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 朋友圈动态表实体类
 * </p>
 */
@Data
@TableName("friend_moments")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class FriendMoments extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 动态ID
     */
    @TableId
    private Long momentId;
    /**
     * 发布用户ID
     */
    private Long userId;
    /**
     * 文字内容
     */
    private String content;
    /**
     * 位置信息
     */
    private String location;
    /**
     * 可见性：0-公开，1-私密，2-部分可见，3-不给谁看
     */
    private Integer visibility;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 逻辑删除标记
     */
    private Integer isDeleted;
    // 可见人群
    private String visuser;

    public FriendMoments(Long momentId) {
        this.momentId = momentId;
    }



}
