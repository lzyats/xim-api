package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GroupVo32 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "接龙id不能为空")
    private Long solitaireId;

    @Valid
    private List<GroupVo30> dataList;

}
