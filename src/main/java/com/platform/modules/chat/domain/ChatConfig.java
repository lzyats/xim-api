package com.platform.modules.chat.domain;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatConfigEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 设置表实体类
 * </p>
 */
@Data
@TableName("chat_config")
@Accessors(chain = true) // 链式调用
public class ChatConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId
    private ChatConfigEnum configKey;
    /**
     * value
     */
    private String configValue;

    public ChatConfig setValue(String value) {
        this.configValue = value;
        return this;
    }

    public ChatConfig setValue(Number value) {
        this.configValue = NumberUtil.toStr(value);
        return this;
    }

    public ChatConfig setValue(BigDecimal value) {
        this.configValue = NumberUtil.toStr(value);
        return this;
    }

    public ChatConfig setValue(YesOrNoEnum value) {
        this.configValue = value.getCode();
        return this;
    }

    public ChatConfig setValue(Date value) {
        this.configValue = DateUtil.format(value, DatePattern.NORM_DATETIME_FORMAT);
        return this;
    }

    public String getStr() {
        return configValue;
    }

    public Integer getInt() {
        return NumberUtil.parseInt(configValue);
    }

    public Long getLong() {
        return NumberUtil.parseLong(configValue);
    }

    public BigDecimal getBigDecimal() {
        return NumberUtil.toBigDecimal(configValue);
    }

    public YesOrNoEnum getYesOrNo() {
        return EnumUtils.toEnum(YesOrNoEnum.class, configValue, YesOrNoEnum.NO);
    }

    public Date getDate() {
        return DateUtil.parseDateTime(configValue);
    }

    public DateTime getTime() {
        return DateUtil.parseTime(configValue);
    }

    // 在 ChatConfig 类中添加
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("configKey", this.getConfigKey().name()); // 配置枚举的名称
        map.put("configValue", this.getConfigValue()); // 配置值（核心字段）
        return map;
    }

}
