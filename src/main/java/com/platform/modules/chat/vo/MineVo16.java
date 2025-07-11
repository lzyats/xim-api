package com.platform.modules.chat.vo;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo16 {

    /**
     * 禁用状态
     */
    private YesOrNoEnum banned;
    /**
     * 禁用原因
     */
    private String reason;
    /**
     * 禁用时间
     */
    private Long remain = 0L;
    /**
     * 禁用说明
     */
    private String explain = "请严格遵守用户协议，避免违规行为";

    public String getBannedLabel() {
        String label = null;
        if (banned != null) {
            switch (banned) {
                case YES:
                    label = "封禁";
                    break;
                case NO:
                    label = "正常";
                    break;
            }
        }
        return label;
    }

    public MineVo16() {
        this.banned = YesOrNoEnum.NO;
    }

    public MineVo16(String reason, Date bannedTime) {
        this.banned = YesOrNoEnum.YES;
        this.reason = reason;
        this.remain = DateUtil.between(DateUtil.date(), bannedTime, DateUnit.SECOND);
    }
}
