package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo13 {

    @NotNull(message = "隐私扫码不能为空")
    private YesOrNoEnum privacyScan;

}
