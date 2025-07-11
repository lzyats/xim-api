package com.platform.modules.common.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.platform.modules.chat.domain.ChatNotice;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class CommonVo04 {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发布时间
     */
    private String createTime;

    public CommonVo04(ChatNotice chatNotice) {
        this.title = chatNotice.getTitle();
        this.content = chatNotice.getContent();
        this.createTime = chatNotice.getContent();
        this.createTime = DateUtil.format(chatNotice.getCreateTime(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
    }
}
