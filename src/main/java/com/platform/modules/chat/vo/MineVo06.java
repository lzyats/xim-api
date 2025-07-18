package com.platform.modules.chat.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.ChatUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;


@Data
@Accessors(chain = true) // 链式调用
public class MineVo06 {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 签名
     */
    private String sign;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 性别1男2女
     */
    private GenderEnum gender;
    /**
     * 账号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 支付标志
     */
    private YesOrNoEnum payment;
    /**
     * 密码标志
     */
    private YesOrNoEnum pass;
    /**
     * 认证状态
     */
    private ApproveEnum auth;
    /**
     * 隐私no
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私账号
     */
    private YesOrNoEnum privacyPhone;
    /**
     * 隐私扫码
     */
    private YesOrNoEnum privacyScan;
    /**
     * 隐私名片
     */
    private YesOrNoEnum privacyCard;
    /**
     * 隐私群组
     */
    private YesOrNoEnum privacyGroup;
    /**
     * 微信绑定
     */
    private YesOrNoEnum wx;
    /**
     * 苹果绑定
     */
    private YesOrNoEnum ios;
    /**
     * QQ绑定
     */
    private YesOrNoEnum qq;

    public static MineVo06 init(ChatUser chatUser, String sign) {
        // 数据转换
        String phone = chatUser.getPhone();
        if (Validator.isMobile(phone)) {
            phone = DesensitizedUtil.mobilePhone(phone);
        }
        // 数据转换
        String email = chatUser.getEmail();
        if (Validator.isEmail(email)) {
            email = DesensitizedUtil.email(email);
        }
        return BeanUtil.toBean(chatUser, MineVo06.class)
                .setPhone(phone)
                .setEmail(email)
                .setSign(sign)
                .setWx(YesOrNoEnum.transform(!StringUtils.isEmpty(chatUser.getLoginWx())))
                .setIos(YesOrNoEnum.transform(!StringUtils.isEmpty(chatUser.getLoginIos())))
                .setQq(YesOrNoEnum.transform(!StringUtils.isEmpty(chatUser.getLoginQq())))
                .setBirthday(DateUtil.format(chatUser.getBirthday(), DatePattern.NORM_DATE_PATTERN));
    }

}
