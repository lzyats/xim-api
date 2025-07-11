package com.platform.modules.chat.vo;

import com.platform.common.enums.ApproveEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class FriendVo10 {

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
     * 理由
     */
    private String reason;
    /**
     * 申请状态
     */
    private ApproveEnum status;
    /**
     * 来源
     */
    private String remark;

}
