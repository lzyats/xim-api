package com.platform.modules.chat.vo;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.platform.modules.chat.enums.GroupMemberEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo08 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 聊天号码
     */
    private String userNo;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 成员类型
     */
    private GroupMemberEnum memberType;
    /**
     * 禁言时间
     */
    private String speakTimeLabel;

    public GroupVo08 setRemark(String remark) {
        if (!StringUtils.isEmpty(remark)) {
            this.nickname = remark;
        }
        return this;
    }

    public GroupVo08 setSpeakTimeLabel(Date now, Date speakTime) {
        if (speakTime != null && speakTime.after(now)) {
            long betweenDay = DateUtil.betweenMs(now, speakTime);
            this.speakTimeLabel = DateUtil.formatBetween(betweenDay, BetweenFormatter.Level.MINUTE);
        }
        return this;
    }
}
