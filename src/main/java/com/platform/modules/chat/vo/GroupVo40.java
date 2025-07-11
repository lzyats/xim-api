package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import com.platform.common.validation.ValidList;
import com.platform.modules.chat.enums.BannedTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo40 {

    /**
     * 举报类型
     */
    @NotNull(message = "举报类型不能为空")
    private BannedTypeEnum informType;
    /**
     * 举报对象
     */
    @NotNull(message = "举报对象不能为空")
    private Long groupId;
    /**
     * 图片
     */
    private ValidList<String> images;
    /**
     * 内容
     */
    @Size(max = 2000, message = "内容长度不能大于2000")
    private String content;

    public void setContent(String content) {
        this.content = StrUtil.trim(content);
    }
}
