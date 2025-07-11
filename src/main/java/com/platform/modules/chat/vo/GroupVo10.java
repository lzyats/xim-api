package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo10 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @Size(max = 15, message = "群昵称长度不能大于15")
    private String remark;

    public void setRemark(String remark) {
        this.remark = StrUtil.trim(remark);
    }

}
