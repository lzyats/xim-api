package com.platform.modules.common.service;

import com.platform.modules.common.vo.CommonVo06;

/**
 * 公共服务
 */
public interface CommonService {

    /**
     * 获取映射
     */
    void getMapping();

    /**
     * 获取配置
     */
    CommonVo06 getConfig();

}
