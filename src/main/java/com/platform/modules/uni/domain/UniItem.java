package com.platform.modules.uni.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.uni.enums.UniTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 小程序表实体类
 * </p>
 */
@Data
@TableName("uni_item")
@Accessors(chain = true) // 链式调用
public class UniItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long uniId;
    /**
     * appId
     */
    private String appId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 版本
     */
    private Long version;
    /**
     * 地址
     */
    private String path;
    /**
     * 类型
     */
    private UniTypeEnum type;
    /**
     * 状态
     */
    private YesOrNoEnum status;

}
