package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo34 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "隐私不能为空")
    private YesOrNoEnum privacyNo;

}
