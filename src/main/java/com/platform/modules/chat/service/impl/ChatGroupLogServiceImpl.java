package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupLogDao;
import com.platform.modules.chat.domain.ChatGroupLog;
import com.platform.modules.chat.enums.GroupLogEnum;
import com.platform.modules.chat.service.ChatGroupLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 群组日志 服务层实现
 * </p>
 */
@Service("chatGroupLogService")
public class ChatGroupLogServiceImpl extends BaseServiceImpl<ChatGroupLog> implements ChatGroupLogService {

    @Resource
    private ChatGroupLogDao chatGroupLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupLogDao);
    }

    @Override
    public List<ChatGroupLog> queryList(ChatGroupLog t) {
        List<ChatGroupLog> dataList = chatGroupLogDao.queryList(t);
        return dataList;
    }

    @Override
    public void addLog(Long groupId, GroupLogEnum logType) {
        this.addLog(groupId, logType, logType.getInfo());
    }

    @Override
    public void addLog(Long groupId, GroupLogEnum logType, Long content) {
        this.addLog(groupId, logType, NumberUtil.toStr(content));
    }

    @Override
    public void addLog(Long groupId, GroupLogEnum logType, String content) {
        // 判断 content 是否超过 200 个字符
        if (content != null && content.length() > 200) {
            // 如果超过 200 个字符，则截取前 200 个字符
            content = content.substring(0, 200);
        }
        ChatGroupLog groupLog = new ChatGroupLog()
                .setGroupId(groupId)
                .setLogType(logType)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(groupLog);
    }

    @Override
    public void addLog(Long groupId, GroupLogEnum logType, YesOrNoEnum config) {
        String tips = YesOrNoEnum.YES.equals(config) ? "开启" : "关闭";
        this.addLog(groupId, logType, tips);
    }

}
