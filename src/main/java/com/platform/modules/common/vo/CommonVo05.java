package com.platform.modules.common.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class CommonVo05 {

    /**
     * 是否升级
     */
    private YesOrNoEnum upgrade = YesOrNoEnum.NO;
    /**
     * 版本
     */
    private String version = "1.0.0";
    /**
     * 地址
     */
    private String url = "https://apps.apple.com/cn/app/%E8%80%83%E6%8B%89im/id6443972441";
    /**
     * 内容
     */
    private String content = "第一个版本";
    /**
     * 是否强制升级
     */
    private YesOrNoEnum force = YesOrNoEnum.NO;

}
