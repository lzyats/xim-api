package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserAppealDao;
import com.platform.modules.chat.domain.ChatUserAppeal;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.ChatUserAppealService;
import com.platform.modules.chat.service.ChatUserLogService;
import com.platform.modules.chat.vo.MineVo17;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.enums.PushAuditEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户申诉 服务层实现
 * </p>
 */
@Service("chatUserAppealService")
public class ChatUserAppealServiceImpl extends BaseServiceImpl<ChatUserAppeal> implements ChatUserAppealService {

    @Resource
    private ChatUserAppealDao chatUserAppealDao;

    @Resource
    private HookService hookService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserAppealDao);
    }

    @Override
    public List<ChatUserAppeal> queryList(ChatUserAppeal t) {
        List<ChatUserAppeal> dataList = chatUserAppealDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public void appealBanned(MineVo17 mineVo) {
        YesOrNoEnum banned = ShiroUtils.getBanned();
        if (!YesOrNoEnum.transform(banned)) {
            return;
        }
        Long current = ShiroUtils.getUserId();
        // 删除
        this.deleteById(current);
        // 新增
        ChatUserAppeal appeal = new ChatUserAppeal()
                .setUserId(current)
                .setImages(JSONUtil.toJsonStr(mineVo.getImages()))
                .setContent(mineVo.getContent())
                .setCreateTime(DateUtil.date());
        this.add(appeal);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.BANNED_APPEAL);
        // 通知
        hookService.handle(PushAuditEnum.APPLY_BANNED);
    }

}
