package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo31 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "接龙id不能为空")
    private Long solitaireId;

}
