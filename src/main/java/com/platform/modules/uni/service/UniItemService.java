package com.platform.modules.uni.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.uni.domain.UniItem;

import java.util.List;

/**
 * <p>
 * 小程序表 服务层
 * </p>
 */
public interface UniItemService extends BaseService<UniItem> {

    /**
     * 获取列表
     */
    List<UniItem> queryDataList();

}
