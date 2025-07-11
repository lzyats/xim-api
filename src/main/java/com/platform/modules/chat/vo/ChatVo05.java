package com.platform.modules.chat.vo;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
@Deprecated
public class ChatVo05 {

    /**
     * 消息ID
     */
    private Long msgId;
    /**
     * 同步ID
     */
    private Long syncId;
    /**
     * 创建时间
     */
    private Long createTime;

    public ChatVo05() {
        this.msgId = IdWorker.getId();
        this.syncId = IdWorker.getId();
        this.createTime = DateUtil.current();
    }
}
