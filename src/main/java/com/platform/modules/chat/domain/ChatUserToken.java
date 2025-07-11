package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.DeviceEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户token实体类
 * </p>
 */
@Data
@TableName("chat_user_token")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatUserToken extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * token
     */
    private String token;
    /**
     * 设备
     */
    private DeviceEnum device;
    /**
     * 设备
     */
    private DeviceEnum.DeviceTypeEnum deviceType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 删除
     */
    @TableLogic
    private Integer deleted;

    public ChatUserToken(Long userId) {
        this.userId = userId;
    }

    public ChatUserToken(Long userId, DeviceEnum device) {
        this.userId = userId;
        this.deviceType = device.getDeviceType();
    }

}
