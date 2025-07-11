package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupApply;
import com.platform.modules.chat.enums.GroupSourceEnum;

import java.util.List;

/**
 * <p>
 * 群组申请表 服务层
 * </p>
 */
public interface ChatGroupApplyService extends BaseService<ChatGroupApply> {

    /**
     * 申请群组
     */
    void apply(ChatGroup chatGroup, List<Long> userList, GroupSourceEnum source);

    /**
     * 申请记录
     */
    PageInfo queryDataList();

    /**
     * 忽略申请
     */
    void ignore(Long applyId);

    /**
     * 删除申请
     */
    void applyDelete(Long applyId);

    /**
     * 同意申请
     */
    void agree(Long applyId);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 修改群名
     */
    void editGroupName(Long groupId, String groupName);
}
