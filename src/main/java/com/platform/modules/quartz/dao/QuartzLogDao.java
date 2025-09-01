package com.platform.modules.quartz.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.platform.modules.quartz.domain.QuartzLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 数据库访问层
 * </p>
 */
@Repository
public interface QuartzLogDao extends BaseDao<QuartzLog> {

    /**
     * 查询列表
     */
    List<QuartzLog> queryList(QuartzLog quartzLog);
    /**
     * 查询列表
     */
    int dellogs(@Param(Constants.WRAPPER) QueryWrapper<QuartzLog> ew);

}
