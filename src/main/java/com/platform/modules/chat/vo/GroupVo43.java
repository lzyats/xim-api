package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo43 {

    /**
     * 主键
     */
    private Long solitaireId;
    /**
     * 群组
     */
    private Long groupId;
    /**
     * 主题
     */
    private String subject;
    /**
     * 例子
     */
    private String example;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 过期时间
     */
    private YesOrNoEnum expired;
    /**
     * 加载数据
     */
    private List<GroupVo33> dataList;

}
