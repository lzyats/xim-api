package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MineVo03 {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 15, message = "昵称长度不能大于15")
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = StrUtil.trim(nickname);
    }

}
