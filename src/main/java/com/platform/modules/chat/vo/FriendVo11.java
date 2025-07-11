package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 同意申请
 */
@Data
public class FriendVo11 {

    @NotNull(message = "申请id不能为空")
    private Long applyId;

    @Size(max = 15, message = "备注长度不能大于15")
    private String remark;

    public void setRemark(String remark) {
        this.remark = StrUtil.trim(remark);
    }

}
