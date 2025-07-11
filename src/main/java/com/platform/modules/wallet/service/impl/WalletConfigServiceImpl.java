package com.platform.modules.wallet.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import com.platform.modules.wallet.service.WalletConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 钱包容量 服务层实现
 * </p>
 */
@Service("walletConfigService")
public class WalletConfigServiceImpl implements WalletConfigService {

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<BigDecimal> queryList() {
        // 查询字典
        List<SysDict> dataList = sysDictService.queryDict("recharge_amount");
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            if (YesOrNoEnum.YES.getCode().equals(y.getRemark())) {
                x.add(NumberUtil.toBigDecimal(y.getDictName()).setScale(2));
            }
        }, ArrayList::addAll);
    }

}
