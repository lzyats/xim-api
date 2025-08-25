package com.platform.modules.sys.service;

import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.sys.domain.SysHtml;
import com.platform.common.web.service.BaseService;

import java.util.List;

/**
 * <p>
 * APP网页定制 服务层
 * </p>
 */
public interface SysHtmlService extends BaseService<SysHtml> {
    /**
     * 查询HTML列表信息
     * @return
     */
    public List<SysHtml> queryList();

    /**
     * 根据roulekey查询HTML
     * @param roulekey
     */
    public SysHtml getInfo(String roulekey);
}
