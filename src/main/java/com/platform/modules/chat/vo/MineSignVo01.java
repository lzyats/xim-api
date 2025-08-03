package com.platform.modules.chat.vo;

import com.platform.modules.chat.domain.ChatUserSign;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import org.joda.time.DateTime;


@Data
public class MineSignVo01 {
    private Long signid;
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "签到日期不能为空")
    private Date signDate;
    private Long tradeId;
    // 将BigDecimal改为double，初始化值改为0.0
    private double rewardAmount = 0.0;
    private Date createTime;

    /**
     * 从 ChatUserSign 实体转换为 MineSignVo01（包含格式化逻辑）
     */
    public MineSignVo01(ChatUserSign trade) {
        this.signid = trade.getSignid();
        this.userId = trade.getUserId();
        this.tradeId = trade.getTradeId();
        this.signDate = trade.getSignDate();
        // 若原实体中是BigDecimal，需转换为double；若原实体已是double则直接赋值
        this.rewardAmount = trade.getRewardAmount();
        this.createTime = trade.getCreateTime();
    }

    /**
     * 格式化签到类型为文本（1-正常签到，2-补签）
     */
    private String formatSignType(Integer signType) {
        if (signType == null) {
            return "";
        }
        return signType == 1 ? "正常签到" : "补签";
    }

    /**
     * 格式化是否有效为文本（1-有效，0-无效）
     */
    private String formatIsValid(Integer isValid) {
        if (isValid == null) {
            return "";
        }
        return isValid == 1 ? "有效" : "无效";
    }
}