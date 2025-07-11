package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包交易实体类
 * </p>
 */
@Data
@TableName("wallet_trade")
@Accessors(chain = true) // 链式调用
public class WalletTrade extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long tradeId;
    /**
     * 交易类型
     */
    private TradeTypeEnum tradeType;
    /**
     * 是否红包
     */
    private YesOrNoEnum tradePacket;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 钱包余额
     */
    private BigDecimal tradeBalance;
    /**
     * 交易数量
     */
    private Integer tradeCount;
    /**
     * 交易备注
     */
    private String tradeRemark;
    /**
     * 来源id
     */
    private Long sourceId;
    /**
     * 来源类型
     */
    private TradeTypeEnum sourceType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户号码
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户账号
     */
    private String phone;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 群组
     */
    private Long groupId;
    /**
     * 群号
     */
    private String groupNo;
    /**
     * 群名
     */
    private String groupName;
    /**
     * 接收人
     */
    private Long receiveId;
    /**
     * 接收号码
     */
    private String receiveNo;
    /**
     * 接收昵称
     */
    private String receiveName;
    /**
     * 接收账号
     */
    private String receivePhone;
    /**
     * 接收头像
     */
    private String receivePortrait;
    /**
     * 交易状态
     */
    private ApproveEnum tradeStatus;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;
    /**
     * 注销0正常null注销
     */
    private Integer deleted;

    public BigDecimal getAbsolute() {
        if (tradeAmount == null) {
            tradeAmount = BigDecimal.ZERO;
        }
        return tradeAmount.abs();
    }

    /**
     * 字段
     */
    public static final String COLUMN_TRADE_TYPE = "trade_type";
    public static final String COLUMN_TRADE_STATUS = "trade_status";
    public static final String COLUMN_TRADE_AMOUNT = "trade_amount";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_DELETED = "deleted";
    public static final String COLUMN_CREATE_TIME = "create_time";
}
