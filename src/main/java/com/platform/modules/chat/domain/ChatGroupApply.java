package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.GroupSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 群组申请表实体类
 * </p>
 */
@Data
@TableName("chat_group_apply")
@Accessors(chain = true) // 链式调用
public class ChatGroupApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long applyId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 接收id
     */
    private Long receiveId;
    /**
     * 申请状态0无1同意2拒绝3忽略
     */
    private ApproveEnum applyStatus;
    /**
     * 申请来源
     */
    private GroupSourceEnum applySource;
    /**
     * 申请来源
     */
    @TableField(exist = false)
    private String applySourceLabel;
    /**
     * 申请时间
     */
    private Date createTime;

    public String getApplyStatusLabel() {
        if (applyStatus == null) {
            return null;
        }
        switch (applyStatus) {
            case APPLY:
                return "审核中";
            case PASS:
                return "已同意";
            case REJECT:
                return "已拒绝";
        }
        return null;
    }

    public String getApplySourceLabel() {
        if (applySource != null) {
            applySourceLabel = applySource.getInfo();
        }
        return applySourceLabel;
    }

    /**
     * 字段
     */
    public static final String COLUMN_RECEIVE_ID = "receive_id";
    public static final String COLUMN_CREATE_TIME = "create_time";
}
