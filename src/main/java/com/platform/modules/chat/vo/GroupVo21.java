package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo21 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Size(max = 15, message = "昵称长度不能大于15")
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = StrUtil.trim(nickname);
    }
}
