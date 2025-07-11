package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFeedbackDao;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.service.ChatUserLogService;
import com.platform.modules.common.service.HookService;
import com.platform.modules.common.vo.CommonVo01;
import com.platform.modules.push.enums.PushAuditEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 建议反馈 服务层实现
 * </p>
 */
@Service("chatFeedbackService")
public class ChatFeedbackServiceImpl extends BaseServiceImpl<ChatFeedback> implements ChatFeedbackService {

    @Resource
    private ChatFeedbackDao chatFeedbackDao;

    @Resource
    private HookService hookService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFeedbackDao);
    }

    @Override
    public List<ChatFeedback> queryList(ChatFeedback t) {
        List<ChatFeedback> dataList = chatFeedbackDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public void addFeedback(CommonVo01 commonVo) {
        Long current = ShiroUtils.getUserId();
        String version = ServletUtils.getRequest().getHeader(HeadConstant.VERSION);
        ChatFeedback feedback = new ChatFeedback()
                .setUserId(current)
                .setImages(JSONUtil.toJsonStr(commonVo.getImages()))
                .setContent(commonVo.getContent())
                .setStatus(YesOrNoEnum.NO)
                .setVersion(version)
                .setCreateTime(DateUtil.date());
        this.add(feedback);
        // 新增日志
        chatUserLogService.addLog(current, UserLogEnum.FEEDBACK, feedback.getId());
        // 通知
        hookService.handle(PushAuditEnum.USER_FEEDBACK);
    }
}
