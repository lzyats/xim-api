package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.platform.common.exception.BaseException;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserDeletedDao;
import com.platform.modules.chat.domain.ChatUserDeleted;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatUserDeletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 注销表 服务层实现
 * </p>
 */
@Service("chatUserDeletedService")
public class ChatUserDeletedServiceImpl extends BaseServiceImpl<ChatUserDeleted> implements ChatUserDeletedService {

    @Resource
    private ChatUserDeletedDao chatUserDeletedDao;

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserDeletedDao);
    }

    @Override
    public List<ChatUserDeleted> queryList(ChatUserDeleted t) {
        List<ChatUserDeleted> dataList = chatUserDeletedDao.queryList(t);
        return dataList;
    }

    @Override
    public void register(String phone) {
        // 排序
        PageHelper.orderBy(StrUtil.toUnderlineCase("createTime desc"));
        ChatUserDeleted userDeleted = this.queryOne(new ChatUserDeleted(phone));
        PageHelper.clearPage();
        if (userDeleted == null) {
            return;
        }
        // 注销保护期
        Integer day = chatConfigService.queryConfig(ChatConfigEnum.USER_DELETED).getInt();
        Date createTime = userDeleted.getCreateTime();
        Long between = DateUtil.between(DateUtil.date(), createTime, DateUnit.DAY);
        if (between > day) {
            return;
        }
        // 验证
        throw new BaseException(StrUtil.format("按照平台运营规范，为保护用户信息安全，已经注销账号关联的账号{}天内不能重新注册。", day));
    }

    @Override
    public void deleted(Long userId, String phone) {
        this.add(new ChatUserDeleted(userId, phone));
    }

}
