package com.platform.modules.chat.service.impl;

import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.ServletUtils;
import com.platform.common.utils.VersionUtils;
import com.platform.common.web.filter.version.VersionConfig;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVersionDao;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.service.ChatVersionService;
import com.platform.modules.common.vo.CommonVo05;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 版本 服务层实现
 * </p>
 */
@Service("chatVersionService")
public class ChatVersionServiceImpl extends BaseServiceImpl<ChatVersion> implements ChatVersionService {

    @Resource
    private ChatVersionDao chatVersionDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatVersionDao);
    }

    @Override
    public List<ChatVersion> queryList(ChatVersion t) {
        List<ChatVersion> dataList = chatVersionDao.queryList(t);
        return dataList;
    }

    @Override
    public CommonVo05 upgrade() {
        HttpServletRequest request = ServletUtils.getRequest();
        // 设备
        String device = request.getHeader(HeadConstant.DEVICE);
        ChatVersion chatVersion = this.queryOne(new ChatVersion().setDevice(device));
        if (chatVersion == null) {
            return new CommonVo05();
        }
        // 版本
        String version = request.getHeader(HeadConstant.VERSION);
        String lowest = VersionConfig.LOWEST;
        return new CommonVo05()
                .setVersion(chatVersion.getVersion())
                .setUrl(chatVersion.getUrl())
                .setContent(chatVersion.getContent())
                .setUpgrade(YesOrNoEnum.transform(VersionUtils.compareTo(version, chatVersion.getVersion()) < 0))
                .setForce(YesOrNoEnum.transform(VersionUtils.compareTo(version, lowest) < 0));
    }

}
