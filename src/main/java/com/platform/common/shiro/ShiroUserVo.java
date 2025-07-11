package com.platform.common.shiro;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.EncryptUtils;
import com.platform.modules.chat.domain.ChatUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录用户身份权限
 */
@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ShiroUserVo {

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户号码
     */
    private String userNo;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户签名
     */
    private String sign;

    /**
     * 强制禁用
     */
    private YesOrNoEnum banned;

    /**
     * 消息id
     */
    private String lastId;

    public ShiroUserVo(ChatUser chatUser) {
        JSONObject jsonObject = new JSONObject()
                .set("timestamp", RandomUtil.randomString(14))
                .set("userId", chatUser.getUserId());
        this.token = EncryptUtils.encrypt(JSONUtil.toJsonStr(jsonObject), PlatformConfig.SECRET);
        this.userId = chatUser.getUserId();
        this.nickname = chatUser.getNickname();
        this.portrait = chatUser.getPortrait();
        this.sign = RandomUtil.randomString(32);
        this.phone = chatUser.getPhone();
        this.userNo = chatUser.getUserNo();
        this.banned = chatUser.getBanned();
        this.lastId = "0";
    }

    public Map<String, Object> toMap() {
        return MapUtil.builder(new HashMap<String, Object>())
                .put("token", token)
                .put("userId", NumberUtil.toStr(userId))
                .put("nickname", nickname)
                .put("portrait", portrait)
                .put("sign", sign)
                .put("phone", phone)
                .put("userNo", userNo)
                .put("banned", banned.getCode())
                .put("lastId", lastId)
                .build();
    }

    public static Map<String, Object> refresh(ShiroUserVo userVo) {
        String nickname = userVo.getNickname();
        String portrait = userVo.getPortrait();
        String lastId = userVo.getLastId();
        Map<String, Object> objectMap = new HashMap<>();
        if (!StringUtils.isEmpty(nickname)) {
            objectMap.put("nickname", nickname);
        }
        if (!StringUtils.isEmpty(portrait)) {
            objectMap.put("portrait", portrait);
        }
        if (!StringUtils.isEmpty(lastId)) {
            objectMap.put("lastId", lastId);
        }
        return objectMap;
    }

    public static ShiroUserVo convert(Map<Object, Object> map) {
        return new ShiroUserVo()
                .setToken(map.get("token").toString())
                .setUserId(NumberUtil.parseLong(map.get("userId").toString()))
                .setNickname(map.get("nickname").toString())
                .setSign(map.get("sign").toString())
                .setUserNo(map.get("userNo").toString())
                .setPortrait(map.get("portrait").toString())
                .setPhone(map.get("phone").toString())
                .setBanned(EnumUtils.toEnum(YesOrNoEnum.class, map.get("banned").toString()))
                .setLastId(map.get("lastId") == null ? "0" : map.get("lastId").toString())
                ;
    }

}
