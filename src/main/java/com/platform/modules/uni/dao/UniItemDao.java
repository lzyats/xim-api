package com.platform.modules.uni.dao;

import com.platform.modules.uni.domain.UniItem;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 小程序表 数据库访问层
 * </p>
 */
@Repository
public interface UniItemDao extends BaseDao<UniItem> {

    /**
     * 查询列表
     */
    List<UniItem> queryList(UniItem uniItem);

}
