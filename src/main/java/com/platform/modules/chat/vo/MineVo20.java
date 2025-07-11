package com.platform.modules.chat.vo;

import com.platform.modules.common.enums.MessageTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 短信对象
 */
@Data
@Accessors(chain = true) // 链式调用
public class MineVo20 {

    /**
     * 短信类型
     */
    @NotNull(message = "短信类型不能为空")
    private MessageTypeEnum type;

}
