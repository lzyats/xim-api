package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class MineVo07 {

    @Size(max = 20, message = "个性签名长度限1-20位")
    private String intro;

    public void setIntro(String intro) {
        this.intro = StrUtil.trim(intro);
    }
}
