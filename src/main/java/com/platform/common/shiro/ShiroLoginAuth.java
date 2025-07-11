package com.platform.common.shiro;

import com.platform.modules.chat.domain.ChatUser;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * phone
 */
@Data
public class ShiroLoginAuth implements AuthenticationToken {

    private String username;
    private char[] password;
    private String salt;
    private String pass;
    private ShiroUserVo loginUser;

    public ShiroLoginAuth(String phone, String password, ChatUser chatUser) {
        this.username = phone;
        this.password = password.toCharArray();
        this.salt = chatUser.getSalt();
        this.pass = chatUser.getPassword();
        this.loginUser = new ShiroUserVo(chatUser);
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

}
