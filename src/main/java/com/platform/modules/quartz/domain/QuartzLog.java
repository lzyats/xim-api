package com.platform.modules.quartz.domain;

import java.util.Date;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务调度日志表实体类
 * </p>
 */
@Data
@TableName("quartz_log")
@Accessors(chain = true) // 链式调用
public class QuartzLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 任务日志ID
     */
    @TableId
    private Long logId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 调用目标字符串
     */
    private String invokeTarget;
    /**
     * 日志信息
     */
    private String message;
    /**
     * 执行状态
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;

}
