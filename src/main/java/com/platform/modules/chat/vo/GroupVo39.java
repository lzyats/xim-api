package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.GroupSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo39 {

    /**
     * 群组ID
     */
    private Long groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组号码
     */
    private String groupNo;
    /**
     * 群组头像
     */
    private String portrait;
    /**
     * 群组审核
     */
    private YesOrNoEnum configAudit;
    /**
     * 群组来源
     */
    private GroupSourceEnum source;
    /**
     * 是否成员
     */
    private YesOrNoEnum isMember;

}
