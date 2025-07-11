package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo17 {

    private List<String> images;

    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容长度不能大于2000")
    private String content;

    public void setContent(String content) {
        this.content = StrUtil.trim(content);
    }

}
