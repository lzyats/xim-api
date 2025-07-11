package com.platform.modules.chat.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserCollectDao;
import com.platform.modules.chat.domain.ChatUserCollect;
import com.platform.modules.chat.vo.CollectVo01;
import com.platform.modules.chat.vo.CollectVo02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 收藏表 服务层实现
 * </p>
 */
@Service("chatUserCollectService")
public class ChatCollectServiceImpl extends BaseServiceImpl<ChatUserCollect> implements ChatUserCollectService {

    @Resource
    private ChatUserCollectDao chatUserCollectDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserCollectDao);
    }

    @Override
    public List<ChatUserCollect> queryList(ChatUserCollect t) {
        List<ChatUserCollect> dataList = chatUserCollectDao.queryList(t);
        return dataList;
    }

    @Override
    public void addCollect(CollectVo01 collectVo) {
        ChatUserCollect collect = new ChatUserCollect()
                .setUserId(ShiroUtils.getUserId())
                .setMsgType(collectVo.getMsgType())
                .setContent(JSONUtil.toJsonStr(collectVo.getContent()))
                .setCreateTime(DateUtil.date());
        this.add(collect);
    }

    @Override
    public Integer deleteById(Long collectId) {
        ChatUserCollect collect = this.getById(collectId);
        if (collect == null) {
            return 0;
        }
        Long current = ShiroUtils.getUserId();
        if (!current.equals(collect.getUserId())) {
            return 0;
        }
        return super.deleteById(collectId);
    }

    @Override
    public PageInfo queryDataList(ChatUserCollect collect) {
        collect.setUserId(ShiroUtils.getUserId());
        List<ChatUserCollect> collectList = queryList(collect);
        List<CollectVo02> dataList = new ArrayList<>();
        collectList.forEach(e -> {
            dataList.add(BeanUtil.toBean(e, CollectVo02.class));
        });
        return getPageInfo(dataList, collectList);
    }

}
