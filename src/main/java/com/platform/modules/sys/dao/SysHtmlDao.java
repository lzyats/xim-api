package com.platform.modules.sys.dao;

import com.platform.modules.sys.domain.SysHtml;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * APP网页定制 数据库访问层
 * </p>
 */
@Repository
public interface SysHtmlDao extends BaseDao<SysHtml> {

    /**
     * 查询列表
     */
    List<SysHtml> queryList(SysHtml sysHtml);
    /**
     * 查询列表
     */
    List<SysHtml> queryList();

}
