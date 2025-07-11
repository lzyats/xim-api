package com.platform.modules.chat.vo;

import com.platform.common.validation.ValidList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo16 {

    @Size(max = 20, message = "群组名称长度不能大于20")
    private String groupName;

    @NotNull(message = "好友列表不能为空")
    private ValidList<Long> friendList;

}
