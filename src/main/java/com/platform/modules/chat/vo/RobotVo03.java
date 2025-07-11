package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RobotVo03 {

    @NotNull(message = "服务号不能为空")
    private Long robotId;

    @NotNull(message = "状态不能为空")
    private YesOrNoEnum disturb;

}
