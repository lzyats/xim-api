package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典数据 数据库访问层
 * </p>
 */
@Repository
public interface SysDictDao extends BaseDao<SysDict> {

    /**
     * 查询列表
     */
    List<SysDict> queryList(SysDict sysDict);

}
