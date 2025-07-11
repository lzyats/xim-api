package com.platform.modules.chat.vo;

import com.platform.common.validation.ValidList;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo15 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "成员不能为空")
    private ValidList<Long> memberList;

}
