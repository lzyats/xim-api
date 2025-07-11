package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo02 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotBlank(message = "群组名称不能为空")
    @Size(max = 20, message = "群组名称长度不能大于20")
    private String groupName;

    public void setGroupName(String groupName) {
        this.groupName = StrUtil.trim(groupName);
    }

}
