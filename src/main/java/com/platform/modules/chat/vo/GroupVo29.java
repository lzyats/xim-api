package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class GroupVo29 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotBlank(message = "主题不能为空")
    @Size(max = 50, message = "主题长度不能大于50")
    private String subject;

    @Size(max = 200, message = "例子长度不能大于200")
    private String example;

    /**
     * 内容
     */
    @Valid
    private List<GroupVo30> dataList;

    public void setSubject(String subject) {
        this.subject = StrUtil.trim(subject);
    }

    public void setExample(String example) {
        this.example = StrUtil.trim(example);
    }
}
