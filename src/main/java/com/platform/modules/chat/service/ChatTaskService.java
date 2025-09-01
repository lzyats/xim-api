package com.platform.modules.chat.service;

/**
 * <p>
 * 聊天任务 服务层
 * </p>
 */
public interface ChatTaskService {

    /**
     * 日活统计
     */
    void visit();

    /**
     * 用户解封
     */
    void banned();

    /**
     * 删除任务日志
     */
    void dellogs();

    /**
     * 用户解封
     */
    void doBanned(Long userId);

    /**
     * 群组降级
     */
    void level();

    /**
     * 群组降级
     */
    void doLevel(Long groupId, Integer levelCount);

}
