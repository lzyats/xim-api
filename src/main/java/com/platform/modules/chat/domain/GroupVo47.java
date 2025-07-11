package com.platform.modules.chat.domain;

import cn.hutool.core.util.NumberUtil;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.sys.domain.SysDict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 群组容量
 * </p>
 */
@Data
@Accessors(chain = true) // 链式调用
public class GroupVo47 {

    /**
     * 主键
     */
    private Long principal;
    /**
     * 等级
     */
    private Integer groupLevel;
    /**
     * 容量
     */
    private Integer levelCount;
    /**
     * 价格
     */
    private BigDecimal levelPrice;
    /**
     * 状态
     */
    private YesOrNoEnum status;

    public GroupVo47(SysDict sysDict) {
        this.principal = sysDict.getDictId();
        this.groupLevel = sysDict.getDictSort();
        this.levelCount = NumberUtil.parseInt(sysDict.getDictCode());
        this.levelPrice = NumberUtil.toBigDecimal(sysDict.getDictName()).setScale(2);
        this.status = EnumUtils.toEnum(YesOrNoEnum.class, sysDict.getRemark(), YesOrNoEnum.NO);
    }
}
