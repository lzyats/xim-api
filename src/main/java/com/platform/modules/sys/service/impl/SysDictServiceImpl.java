package com.platform.modules.sys.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysDictDao;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 字典数据 服务层实现
 * </p>
 */
@Service("sysDictService")
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {

    @Resource
    private SysDictDao sysDictDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysDictDao);
    }

    @Override
    public List<SysDict> queryList(SysDict t) {
        List<SysDict> dataList = sysDictDao.queryList(t);
        return dataList;
    }

    @Override
    public List<SysDict> queryDict(String dictType) {
        return this.queryList(new SysDict().setDictType(dictType));
    }

}
