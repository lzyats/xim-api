package com.platform.modules.chat.vo;

import cn.hutool.core.util.DesensitizedUtil;
import com.platform.common.enums.ApproveEnum;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo18 {

    /**
     * 认证状态
     */
    private ApproveEnum auth;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 认证原因
     */
    private String authReason;

    public String getAuthLabel() {
        if (auth == null) {
            return null;
        }
        switch (auth) {
            case APPLY:
                return "审核中";
            case NONE:
                return "未认证";
            case PASS:
                return "已认证";
            case REJECT:
                return "已驳回";
        }
        return null;
    }

    public MineVo18(ChatUser chatUser, ChatUserInfo userInfo) {
        this.auth = chatUser.getAuth();
        this.name = userInfo.getName();
        this.idCard = DesensitizedUtil.idCardNum(userInfo.getIdCard(), 3, 2);
        this.authReason = userInfo.getAuthReason();
    }

}
