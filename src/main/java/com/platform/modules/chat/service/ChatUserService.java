package com.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.enums.GenderEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.vo.*;
import com.platform.modules.common.enums.MessageTypeEnum;

import java.util.Date;

/**
 * <p>
 * 用户表 服务层
 * </p>
 */
public interface ChatUserService extends BaseService<ChatUser> {

    /**
     * 发送短信
     */
    Dict sendCode(String phone, String email, MessageTypeEnum messageType);

    /**
     * 通过账号查询
     */
    ChatUser queryByPhone(String phone);

    /**
     * 重置密码
     */
    void resetPass(String phone, String pass);

    /**
     * 修改密码
     */
    void editPass(String oldPwd, String newPwd);

    /**
     * 设置密码
     */
    void setPass(MineVo04 mineVo);

    /**
     * 修改个性签名
     */
    void editIntro(String intro);

    /**
     * 修改省市
     */
    void editCity(MineVo08 mineVo);

    /**
     * 设置生日
     */
    void editBirthday(Date birthday);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 修改昵称
     */
    void editNickname(MineVo03 mineVo);

    /**
     * 修改性别
     */
    void editGender(GenderEnum gender);

    /**
     * 修改隐私no
     */
    void editPrivacyNo(MineVo15 mineVo);

    /**
     * 修改隐私账号
     */
    void editPrivacyPhone(MineVo14 mineVo);

    /**
     * 修改隐私扫码
     */
    void editPrivacyScan(MineVo13 mineVo);

    /**
     * 修改隐私名片
     */
    void editPrivacyCard(MineVo11 mineVo);

    /**
     * 修改隐私名片
     */
    void editPrivacyGroup(MineVo10 mineVo);

    /**
     * 获取基本信息
     */
    MineVo06 getInfo();

    /**
     * 用户注销
     */
    void deleted();

    /**
     * 退出登录
     */
    void logout();

    /**
     * 刷新在线
     */
    void refresh();

    /**
     * 设置支付密码
     */
    void setPayment();

    /**
     * 修改邮箱
     */
    void editEmail(String email);

}
