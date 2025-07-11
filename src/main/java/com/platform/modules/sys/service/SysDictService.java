package com.platform.modules.sys.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.sys.domain.SysDict;

import java.util.List;

/**
 * <p>
 * 字典数据 服务层
 * </p>
 */
public interface SysDictService extends BaseService<SysDict> {

    /**
     * 查询字典
     */
    List<SysDict> queryDict(String dictType);
}
