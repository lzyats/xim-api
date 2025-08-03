package com.platform.modules.chat.domain;

import java.util.Date;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员注册邀请表实体类
 * </p>
 */
@Data
@TableName("chat_user_inv")
@Accessors(chain = true) // 链式调用
public class ChatUserInv extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long inid;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 推荐人ID
     */
    private Long userInid;
    /**
     * 推荐层级
     */
    private Double invUsdt;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    private Boolean deleted;
    /**
     * 0未处理1已处理2其他状态
     */
    private Boolean status;

}
