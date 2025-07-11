package com.platform.modules.sys.service;

import com.platform.modules.sys.domain.SysError;
import com.platform.common.web.service.BaseService;

/**
 * <p>
 * 角色信息表 服务层
 * </p>
 */
public interface SysErrorService extends BaseService<SysError> {

    /**
     * 增加信息
     */
    void addMessage(String message);

}
