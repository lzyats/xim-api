package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GroupVo37 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    /**
     * 列表
     */
    private List<Long> dataList;

}
