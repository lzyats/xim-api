package com.platform.modules.chat.vo;

import cn.hutool.json.JSONObject;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CollectVo01 {

    @NotNull(message = "收藏类型不能为空")
    private PushMsgTypeEnum msgType;

    @NotNull(message = "收藏内容不能为空")
    private JSONObject content;

}
