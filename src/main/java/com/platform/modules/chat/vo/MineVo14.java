package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo14 {

    @NotNull(message = "隐私账号不能为空")
    private YesOrNoEnum privacyPhone;

}
