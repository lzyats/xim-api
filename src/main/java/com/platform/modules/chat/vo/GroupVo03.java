package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo03 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @Size(max = 1200, message = "群组公告长度不能大于1200")
    private String notice;

    public void setNotice(String notice) {
        this.notice = StrUtil.trim(notice);
    }
}
