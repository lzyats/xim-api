package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.common.vo.CommonVo01;

/**
 * <p>
 * 建议反馈 服务层
 * </p>
 */
public interface ChatFeedbackService extends BaseService<ChatFeedback> {

    /**
     * 添加建议反馈
     */
    void addFeedback(CommonVo01 commonVo);

}
