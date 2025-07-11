package com.platform.modules.sys.dao;

import com.platform.modules.sys.domain.SysError;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 角色信息表 数据库访问层
 * </p>
 */
@Repository
public interface SysErrorDao extends BaseDao<SysError> {

    /**
     * 查询列表
     */
    List<SysError> queryList(SysError sysError);

}
