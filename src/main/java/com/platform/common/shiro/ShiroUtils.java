
package com.platform.common.shiro;

import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.ServletUtils;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSync;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static ShiroUserVo getLoginUser() {
        return (ShiroUserVo) getSubject().getPrincipal();
    }

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        Subject subject = getSubject();
        return subject != null && subject.getPrincipal() != null;
    }

    /**
     * 获取token
     */
    public static String getToken() {
        ShiroUserVo loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getToken();
        }
        return ServletUtils.getRequest().getHeader(HeadConstant.TOKEN_HEADER_ADMIN);
    }

    public static String getPhone() {
        return getLoginUser().getPhone();
    }

    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    public static String getNickname() {
        return getLoginUser().getNickname();
    }

    public static String getUserNo() {
        return getLoginUser().getUserNo();
    }

    public static String getPortrait() {
        return getLoginUser().getPortrait();
    }

    public static String getSign() {
        return getLoginUser().getSign();
    }

    public static YesOrNoEnum getBanned() {
        return getLoginUser().getBanned();
    }

    public static String getLastId() {
        return getLoginUser().getLastId();
    }

    public static String getLastMomentId() {
        return getLoginUser().getLastMomentId();
    }

    public static PushFrom getPushFrom(Long msgId, Long syncId) {
        ShiroUserVo loginUser = getLoginUser();
        return new PushFrom()
                .setGroupId(loginUser.getUserId())
                .setUserId(loginUser.getUserId())
                .setNickname(loginUser.getNickname())
                .setPortrait(loginUser.getPortrait())
                .setSign(loginUser.getSign())
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setChatTalk(ChatTalkEnum.FRIEND.getType());
    }

    public static PushSync getPushSync() {
        ShiroUserVo loginUser = getLoginUser();
        return new PushSync()
                .setUserId(loginUser.getUserId())
                .setNickname(loginUser.getNickname())
                .setPortrait(loginUser.getPortrait())
                .setChatTalk(ChatTalkEnum.FRIEND.getType());
    }

}
