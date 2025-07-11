package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 角色信息表实体类
 * </p>
 */
@Data
@TableName("sys_error")
@Accessors(chain = true) // 链式调用
public class SysError extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 文本内容
     */
    private String message;
    /**
     * 创建时间
     */
    private Date createTime;

}
