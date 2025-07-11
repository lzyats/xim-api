package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典数据实体类
 * </p>
 */
@Data
@TableName("sys_dict")
@Accessors(chain = true) // 链式调用
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long dictId;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典编码
     */
    private String dictCode;
    /**
     * 字典排序
     */
    private Integer dictSort;
    /**
     * 备注
     */
    private String remark;

}
