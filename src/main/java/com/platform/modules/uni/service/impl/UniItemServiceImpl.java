package com.platform.modules.uni.service.impl;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.uni.dao.UniItemDao;
import com.platform.modules.uni.domain.UniItem;
import com.platform.modules.uni.service.UniItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序表 服务层实现
 * </p>
 */
@Service("uniItemService")
public class UniItemServiceImpl extends BaseServiceImpl<UniItem> implements UniItemService {

    @Resource
    private UniItemDao uniItemDao;

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(uniItemDao);
    }

    @Override
    public List<UniItem> queryList(UniItem t) {
        List<UniItem> dataList = uniItemDao.queryList(t);
        return dataList;
    }

    @Override
    public List<UniItem> queryDataList() {
        // 查询
        List<UniItem> dataList = this.queryList(new UniItem().setStatus(YesOrNoEnum.YES));
        // 审核
        String phoneConfig = chatConfigService.queryConfig(ChatConfigEnum.SYS_PHONE).getStr();
        if (ShiroUtils.getPhone().equals(phoneConfig)) {
            // 集合过滤
            dataList = dataList.stream().filter(data -> !data.getUniId().equals(10003L))
                    .collect(Collectors.toList());
        }
        return dataList;
    }

}
