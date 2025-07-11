package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupVo33 {

    private String userId;

    private String line;

    private String content;

    public GroupVo33(String line, String userId, String content) {
        this.line = line;
        this.userId = userId;
        this.content = content;
    }

}
