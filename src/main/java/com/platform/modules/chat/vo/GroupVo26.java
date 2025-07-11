package com.platform.modules.chat.vo;

import com.platform.modules.chat.enums.GroupSpeakEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo26 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "时间类型不能为空")
    private GroupSpeakEnum speakType;

}
