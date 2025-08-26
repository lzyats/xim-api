package com.platform.modules.chat.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true) // 链式调用
public class ChatSignVo01 {

    /**
     * 签到日期（仅记录年月日，精确到天）
     */
    @TableField(value = "sign_date")
    private String signDate;
    /**
     * 签到奖励（如USDT数量）
     */
    @TableField(value = "reward_amount")
    private double rewardAmount;

}
