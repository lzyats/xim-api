package com.platform.modules.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysErrorDao;
import com.platform.modules.sys.domain.SysError;
import com.platform.modules.sys.service.SysErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色信息表 服务层实现
 * </p>
 */
@Service("sysErrorService")
public class SysErrorServiceImpl extends BaseServiceImpl<SysError> implements SysErrorService {

    @Resource
    private SysErrorDao sysErrorDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysErrorDao);
    }

    @Override
    public List<SysError> queryList(SysError t) {
        List<SysError> dataList = sysErrorDao.queryList(t);
        return dataList;
    }

    @Override
    public void addMessage(String message) {
        Long userId = ShiroUtils.getUserId();
        ThreadUtil.execAsync(() -> {
            SysError error = new SysError()
                    .setUserId(userId)
                    .setMessage(message)
                    .setCreateTime(DateUtil.date());
            this.add(error);
        });
    }

}
