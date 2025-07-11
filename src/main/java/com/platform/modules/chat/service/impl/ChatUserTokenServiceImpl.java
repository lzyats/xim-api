package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.constant.HeadConstant;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.DeviceEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.chat.dao.ChatUserTokenDao;
import com.platform.modules.chat.domain.ChatUserToken;
import com.platform.modules.chat.service.ChatUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户token 服务层实现
 * </p>
 */
@Service("chatUserTokenService")
public class ChatUserTokenServiceImpl extends BaseServiceImpl<ChatUserToken> implements ChatUserTokenService {

    @Resource
    private ChatUserTokenDao chatUserTokenDao;

    @Resource
    private TokenService tokenService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserTokenDao);
    }

    @Override
    public List<ChatUserToken> queryList(ChatUserToken t) {
        List<ChatUserToken> dataList = chatUserTokenDao.queryList(t);
        return dataList;
    }

    @Override
    public void resetToken(Long userId, String token, YesOrNoEnum special) {
        // 获取设备类型
        DeviceEnum deviceEnum = this.getDevice();
        // 删除数据
        if (!YesOrNoEnum.transform(special)) {
            // 查询
            List<ChatUserToken> dataList = chatUserTokenDao.queryList(new ChatUserToken(userId, deviceEnum));
            dataList.forEach(userToken -> {
                // 删除token
                tokenService.delete(userToken.getToken());
                // 删除数据
                this.deleteById(userToken.getId());
            });
        }
        // 新增
        ChatUserToken userToken = new ChatUserToken()
                .setUserId(userId)
                .setToken(token)
                .setDevice(deviceEnum)
                .setDeviceType(deviceEnum.getDeviceType())
                .setCreateTime(DateUtil.date());
        this.add(userToken);
    }

    @Override
    public List<String> queryTokenList(Long userId) {
        // 查询列表
        List<ChatUserToken> dataList = this.queryList(new ChatUserToken(userId));
        // 集合取属性
        return dataList.stream().map(ChatUserToken::getToken).collect(Collectors.toList());
    }

    @Override
    public void logout(String token) {
        // 清理token
        tokenService.delete(token);
        // 清理token
        ChatUserToken chatUserToken = queryOne(new ChatUserToken().setToken(token));
        if (chatUserToken == null) {
            return;
        }
        this.deleteById(chatUserToken.getId());
    }

    @Override
    public void deleted(Long userId) {
        List<String> dataList = queryTokenList(userId);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 删除登录
        this.delete(Wrappers.<ChatUserToken>lambdaUpdate()
                .eq(ChatUserToken::getUserId, userId));
        // 清理token
        dataList.forEach(token -> {
            tokenService.delete(token);
        });
    }

    /**
     * 获取设备类型
     */
    private DeviceEnum getDevice() {
        // 获取请求
        HttpServletRequest request = ServletUtils.getRequest();
        // 查询设备
        return EnumUtils.toEnum(DeviceEnum.class, request.getHeader(HeadConstant.DEVICE));
    }

}
