package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GroupVo27 {

    private Integer level;
    private BigDecimal amount;
    private YesOrNoEnum extend;
    private String extendLabel;
    private Integer count;
    private Integer between;
    private String remark;

    public GroupVo27(Integer groupLevel, BigDecimal levelPrice, Integer groupCount, YesOrNoEnum extend, Number between, String remark) {
        this.level = groupLevel;
        this.amount = levelPrice;
        this.extend = extend;
        if (YesOrNoEnum.YES.equals(extend)) {
            this.extendLabel = "扩容";
        } else if (YesOrNoEnum.NO.equals(extend)) {
            this.extendLabel = "续费";
        } else {
            this.extendLabel = "升级";
        }
        this.count = groupCount;
        this.between = between.intValue();
        this.remark = remark;
    }

}
