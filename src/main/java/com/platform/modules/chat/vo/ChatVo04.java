package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo04 {

    private List<String> dataList;

}
