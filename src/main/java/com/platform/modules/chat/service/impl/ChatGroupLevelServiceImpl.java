package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.GroupVo47;
import com.platform.modules.chat.service.ChatGroupLevelService;
import com.platform.modules.chat.vo.GroupVo27;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组级别 服务层实现
 * </p>
 */
@Service("chatGroupLevelService")
public class ChatGroupLevelServiceImpl implements ChatGroupLevelService {

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<GroupVo27> queryPriceList(ChatGroup chatGroup) {
        // 组装信息
        Integer groupLevel = chatGroup.getGroupLevel();
        List<GroupVo27> dataList = new ArrayList<>();
        // 查询
        List<GroupVo47> levelList = this.queryDataList().stream().filter(data -> data.getGroupLevel() >= groupLevel).collect(Collectors.toList());
        // 顺序
        Collections.sort(levelList, Comparator.comparing(GroupVo47::getGroupLevel));
        // 扩容
        levelList.forEach(data -> {
            GroupVo27 groupVo = format(data, chatGroup);
            if (groupVo != null) {
                dataList.add(groupVo);
            }
        });
        return dataList;
    }

    @Override
    public GroupVo27 queryPriceInfo(Integer groupLevel, ChatGroup chatGroup) {
        // 查询
        GroupVo47 dataInfo = this.queryDataInfo(groupLevel);
        // 格式化
        GroupVo27 groupVo = format(dataInfo, chatGroup);
        if (groupVo == null) {
            throw new BaseException("价格已停用，请刷新后重试");
        }
        return groupVo;
    }

    /**
     * 获取详情
     */
    public GroupVo27 format(GroupVo47 groupVo, ChatGroup chatGroup) {
        if (groupVo == null) {
            return null;
        }
        // 比较等级
        Integer groupLevel = groupVo.getGroupLevel();
        if (groupLevel.intValue() < chatGroup.getGroupLevel().intValue()) {
            return null;
        }
        // 比较时间
        Date now = DateUtil.date();
        // 时间差额
        long between = DateUtil.between(chatGroup.getGroupLevelTime(), now, DateUnit.DAY);
        String remark = null;
        if (now.before(chatGroup.getGroupLevelTime())) {
            remark = StrUtil.format("容量有效期：{}天", between);
        }
        BigDecimal levelPrice = groupVo.getLevelPrice();
        Integer groupCount = groupVo.getLevelCount();
        // 续费
        if (groupVo.getGroupLevel().equals(chatGroup.getGroupLevel())) {
            return new GroupVo27(groupLevel, levelPrice, groupCount, YesOrNoEnum.NO, 30, remark);
        }
        // 扩容
        if (now.after(chatGroup.getGroupLevelTime())) {
            return new GroupVo27(groupLevel, levelPrice, groupCount, YesOrNoEnum.YES, 30, remark);
        }
        // 价格差额
        BigDecimal balance = NumberUtil.mul(NumberUtil.sub(levelPrice, chatGroup.getGroupLevelPrice()), NumberUtil.div(between + 1, 30));
        return new GroupVo27(groupLevel, balance.abs(), groupCount, YesOrNoEnum.REFUND, between + 1, remark);
    }

    /**
     * 查询列表
     */
    private List<GroupVo47> queryDataList() {
        // 查询字典
        List<SysDict> dataList = sysDictService.queryDict("group_level");
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            if (YesOrNoEnum.YES.getCode().equals(y.getRemark())) {
                x.add(new GroupVo47(y));
            }
        }, ArrayList::addAll);
    }

    /**
     * 查询详情
     */
    private GroupVo47 queryDataInfo(Integer groupLevel) {
        // 查询字典
        List<SysDict> dataList = sysDictService.queryDict("group_level");
        // 集合过滤
        dataList = dataList.stream().filter(data -> groupLevel.intValue() == data.getDictSort()
                && YesOrNoEnum.YES.getCode().equals(data.getRemark()))
                .collect(Collectors.toList());
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        // 查询
        return new GroupVo47(dataList.get(0));
    }

}
