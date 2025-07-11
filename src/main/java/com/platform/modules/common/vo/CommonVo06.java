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


}
