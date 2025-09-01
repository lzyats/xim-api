package com.platform.modules.quartz.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.quartz.service.QuartzLogService;
import com.platform.modules.quartz.dao.QuartzLogDao;
import com.platform.modules.quartz.domain.QuartzLog;

/**
 * <p>
 * 定时任务调度日志表 服务层实现
 * </p>
 */
@Service("quartzLogService")
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLog> implements QuartzLogService {

    @Resource
    private QuartzLogDao quartzLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(quartzLogDao);
    }

    @Override
    public List<QuartzLog> queryList(QuartzLog t) {
        List<QuartzLog> dataList = quartzLogDao.queryList(t);
        return dataList;
    }

}
