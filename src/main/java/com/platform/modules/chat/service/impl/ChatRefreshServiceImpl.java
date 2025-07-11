package com.platform.modules.chat.service.impl;

import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.chat.service.*;
import com.platform.modules.wallet.service.WalletPacketService;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.service.WalletTradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 刷新 服务层实现
 * </p>
 */
@Service("chatRefreshService")
public class ChatRefreshServiceImpl implements ChatRefreshService {

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatFriendApplyService chatFriendApplyService;

    @Resource
    private ChatGroupApplyService chatGroupApplyService;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private ChatUserTokenService chatUserTokenService;

    @Resource
    private WalletPacketService walletPacketService;

    @Resource
    private WalletRechargeService walletRechargeService;

    @Resource
    private TokenService tokenService;

    @Override
    public void refreshNickname(String nickname) {
        chatFriendService.editNickname(nickname);
        chatFriendApplyService.editNickname(nickname);
        chatGroupApplyService.editNickname(nickname);
        chatGroupMemberService.editNickname(nickname);
        walletTradeService.editNickname(nickname);
        walletPacketService.editNickname(nickname);
        walletRechargeService.editNickname(nickname);
        this.refresh(new ShiroUserVo().setNickname(nickname));
    }

    @Override
    public void refreshPortrait(String portrait) {
        chatFriendService.editPortrait(portrait);
        chatFriendApplyService.editPortrait(portrait);
        chatGroupApplyService.editPortrait(portrait);
        chatGroupMemberService.editPortrait(portrait);
        walletTradeService.editPortrait(portrait);
        walletPacketService.editPortrait(portrait);
        this.refresh(new ShiroUserVo().setPortrait(portrait));
    }

    @Override
    public void refreshGroupName(Long groupId, String groupName) {
        chatGroupApplyService.editGroupName(groupId, groupName);
        walletTradeService.editGroupName(groupId, groupName);
    }

    @Override
    public void refresh(ShiroUserVo userVo) {
        Long current = ShiroUtils.getUserId();
        List<String> dataList = chatUserTokenService.queryTokenList(current);
        tokenService.refresh(dataList, userVo);
    }

}
