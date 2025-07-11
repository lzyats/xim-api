package com.platform.modules.common.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CommonVo01 {

    /**
     * 图片
     */
    private List<String> images;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容长度不能大于2000")
    private String content;

    public void setContent(String content) {
        this.content = StrUtil.trim(content);
    }

}
