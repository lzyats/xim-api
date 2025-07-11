package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo20 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "资源开关不能为空")
    private YesOrNoEnum configMedia;

}
