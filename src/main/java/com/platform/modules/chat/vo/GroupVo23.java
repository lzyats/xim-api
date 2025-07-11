package com.platform.modules.chat.vo;

import com.platform.modules.chat.enums.GroupSourceEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo23 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "进群来源不能为空")
    private GroupSourceEnum source;

}
