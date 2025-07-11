package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class GroupVo30 {

    private Long line;

    @Size(max = 50, message = "内容长度不能大于50")
    private String content;

    public void setContent(String content) {
        this.content = StrUtil.trim(content);
    }
}
