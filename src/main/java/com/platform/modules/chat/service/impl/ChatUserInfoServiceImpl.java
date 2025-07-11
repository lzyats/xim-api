package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserInfoDao;
import com.platform.modules.chat.domain.ChatBanned;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.service.ChatBannedService;
import com.platform.modules.chat.service.ChatResourceService;
import com.platform.modules.chat.service.ChatUserInfoService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.MineVo09;
import com.platform.modules.chat.vo.MineVo16;
import com.platform.modules.chat.vo.MineVo18;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户详情 服务层实现
 * </p>
 */
@Service("chatUserInfoService")
public class ChatUserInfoServiceImpl extends BaseServiceImpl<ChatUserInfo> implements ChatUserInfoService {

    @Resource
    private ChatUserInfoDao chatUserInfoDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private PushService pushService;

    @Resource
    private HookService hookService;

    @Resource
    private ChatBannedService chatBannedService;

    @Resource
    private ChatResourceService chatResourceService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserInfoDao);
    }

    @Override
    public List<ChatUserInfo> queryList(ChatUserInfo t) {
        List<ChatUserInfo> dataList = chatUserInfoDao.queryList(t);
        return dataList;
    }

    @Override
    public void addInfo(Long userId) {
        this.add(new ChatUserInfo(userId));
    }

    @Override
    public MineVo16 getBannedInfo() {
        YesOrNoEnum banned = ShiroUtils.getBanned();
        if (!YesOrNoEnum.transform(banned)) {
            return new MineVo16();
        }
        Long current = ShiroUtils.getUserId();
        // 查询
        ChatBanned chatBanned = chatBannedService.getById(current);
        if (chatBanned == null) {
            chatBanned = new ChatBanned()
                    .setBannedReason("其他原因")
                    .setBannedTime(DateUtil.offset(DateUtil.date(), DateField.YEAR, 3));
        }
        return new MineVo16(chatBanned.getBannedReason(), chatBanned.getBannedTime());
    }

    @Override
    public MineVo18 getAuthInfo() {
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = chatUserService.getById(current);
        ChatUserInfo userInfo = this.getById(current);
        return new MineVo18(chatUser, userInfo);
    }

    @Transactional
    @Override
    public void editAuth(MineVo09 mineVo) {
        // 验证身份证号码
        String idCard = mineVo.getIdCard();
        if (!IdcardUtil.isValidCard(idCard)) {
            throw new BaseException("请输入有效的身份证号码");
        }
        Long current = ShiroUtils.getUserId();
        ChatUser chatUser = chatUserService.getById(current);
        switch (chatUser.getAuth()) {
            case APPLY:
            case PASS:
                return;
        }
        // 更新数据
        ChatUserInfo info = new ChatUserInfo()
                .setUserId(current)
                .setName(mineVo.getName())
                .setIdCard(idCard)
                .setIdentity1(mineVo.getIdentity1())
                .setIdentity2(mineVo.getIdentity2())
                .setHoldCard(mineVo.getHoldCard())
                .setAuthTime(DateUtil.date());
        this.updateById(info);
        // 更新用户
        chatUserService.updateById(new ChatUser(current).setAuth(ApproveEnum.APPLY));
        // 删除头像
        chatResourceService.delResource(mineVo.getIdentity1());
        // 删除头像
        chatResourceService.delResource(mineVo.getIdentity2());
        // 删除头像
        chatResourceService.delResource(mineVo.getHoldCard());
        // 发送通知
        hookService.handle(PushAuditEnum.APPLY_AUTH);
        // 通知推送
        pushSetting(current, ChatUser.LABEL_AUTH, ApproveEnum.APPLY.getCode());
    }

    // 通知推送
    private void pushSetting(Long current, String label, String value) {
        PushSetting setting = new PushSetting(PushSettingEnum.MINE, current, label, value);
        pushService.pushSetting(setting, Arrays.asList(current));
    }

}
