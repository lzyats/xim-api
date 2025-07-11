package com.platform.modules.chat.vo;

import cn.hutool.core.util.StrUtil;
import com.platform.modules.chat.enums.FriendSourceEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 申请添加
 */
@Data
public class FriendVo02 {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotBlank(message = "申请理由不能为空")
    @Size(max = 20, message = "申请理由长度不能大于20")
    private String reason;

    @NotNull(message = "好友来源不能为空")
    private FriendSourceEnum source;

    @Size(max = 15, message = "备注长度不能大于15")
    private String remark;

    public void setReason(String reason) {
        this.reason = StrUtil.trim(reason);
    }

    public void setRemark(String remark) {
        this.remark = StrUtil.trim(remark);
    }
}
