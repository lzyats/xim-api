package com.platform.modules.sys.domain;

import java.util.Date;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP网页定制实体类
 * </p>
 */
@Data
@TableName("sys_html")
@Accessors(chain = true) // 链式调用
public class SysHtml extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 网页ID
     */
    @TableId
    private Long id;
    /**
     * 网页内容
     */
    private String html;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 标识符
     */
    private String roulekey;
    /**
     * 网页说明
     */
    private String remake;
    /**
     * 跳转地址
     */
    private String url;

}
