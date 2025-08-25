package com.platform.modules.common.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class CommonVo06 {

    /**
     * 分享页面
     */
    private String sharePath;
    /**
     * 系统水印
     */
    private String watermark = "";
    /**
     * 系统截屏
     */
    private YesOrNoEnum screenshot = YesOrNoEnum.YES;
    /**
     * 通知公告
     */
    private String notice = "";
    /**
     * 系统通告类型
     */
    private int notype = 0;
    /**
     * 备案信息
     */
    private String beian;
    /**
     * 红包金额
     */
    private BigDecimal packet;
    /**
     * 音视频
     */
    private String callKit;
    /**
     * 群聊名称搜索
     */
    private YesOrNoEnum groupSearch;
    /**
     * 手持身份证
     */
    private YesOrNoEnum holdCard;
    /**
     * 消息条数
     */
    private Integer messageLimit;

    /**
     * 分享奖励
     */
    private double invo;

    /**
     * 注册自动加推荐人
     */
    private YesOrNoEnum invoadus;

    /**
     * 注册自动补发朋友圈
     */
    private YesOrNoEnum sendmoment;
    /**
     * 自动加好友ID
     */
    private String friends;

    /**
     * 签到奖励
     */
    private double sign;
    /**
     * 签到奖励是否计入总账
     */
    private YesOrNoEnum signtoal;
    /**
     * 货币名称
     */
    private String cashname;
    /**
     * 货币符号
     */
    private String cashstr;


}
