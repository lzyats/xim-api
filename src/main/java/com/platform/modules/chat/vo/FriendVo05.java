package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class FriendVo05 {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Size(max = 15, message = "昵称长度不能大于15")
    private String remark;

    private String nickname;

    public void setRemark(String remark) {
        this.remark = StrUtil.trim(remark);
    }
}
