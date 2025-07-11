package com.platform.modules.chat.vo;

import com.platform.common.enums.ApproveEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo25 {

    /**
     * 主键
     */
    private Long applyId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 申请状态0无1同意2拒绝3忽略
     */
    private ApproveEnum status;
    /**
     * 来源
     */
    private String remark;

}
