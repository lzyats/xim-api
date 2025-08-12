package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 导入 @TableField 注解（MyBatis-Plus 提供）
import com.baomidou.mybatisplus.annotation.TableField;

// 导入 JacksonTypeHandler 类型处理器（MyBatis-Plus 扩展提供）
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

// 如果需要处理 List 等集合类型，还需要导入 Java 集合类
import java.util.List;

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
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> visuser;

    public FriendMoments(Long momentId) {
        this.momentId = momentId;
    }



}
