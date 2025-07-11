package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.utils.IpUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserLogDao;
import com.platform.modules.chat.domain.ChatUserLog;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.ChatUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户日志 服务层实现
 * </p>
 */
@Service("chatUserLogService")
public class ChatUserLogServiceImpl extends BaseServiceImpl<ChatUserLog> implements ChatUserLogService {

    @Resource
    private ChatUserLogDao chatUserLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserLogDao);
    }

    @Override
    public List<ChatUserLog> queryList(ChatUserLog t) {
        List<ChatUserLog> dataList = chatUserLogDao.queryList(t);
        return dataList;
    }

    @Override
    public void addLog(Long userId, UserLogEnum logType) {
        this.addLog(userId, logType, logType.getInfo());
    }

    @Override
    public void addLog(Long userId, UserLogEnum logType, Long content) {
        this.addLog(userId, logType, NumberUtil.toStr(content));
    }

    @Override
    public void addLog(Long userId, UserLogEnum logType, String content) {
        HttpServletRequest request = ServletUtils.getRequest();
        String ip = IpUtils.getIpAddr(request);
        String version = request.getHeader(HeadConstant.VERSION);
        String device = request.getHeader(HeadConstant.DEVICE);
        ThreadUtil.execAsync(() -> {
            String ipAddr = IpUtils.getIpAddr(ip);
            ChatUserLog userLog = new ChatUserLog()
                    .setUserId(userId)
                    .setLogType(logType)
                    .setContent(content)
                    .setIp(ip)
                    .setIpAddr(ipAddr)
                    .setDeviceVersion(version)
                    .setDeviceType(device)
                    .setCreateTime(DateUtil.date());
            this.add(userLog);
        });
    }

    @Override
    public void addLog(Long userId) {
        UserLogEnum logType = UserLogEnum.BANNED_REMOVE;
        ChatUserLog userLog = new ChatUserLog()
                .setUserId(userId)
                .setLogType(logType)
                .setContent(logType.getInfo())
                .setIp("-")
                .setIpAddr("-")
                .setDeviceVersion("-")
                .setDeviceType("-")
                .setCreateTime(DateUtil.date());
        this.add(userLog);
    }

}
