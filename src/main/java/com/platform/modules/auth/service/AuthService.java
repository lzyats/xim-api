/**
 * Licensed to the Apache Software Foundation （ASF） under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * （the "License"）； you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.platform.modules.auth.service;

import com.platform.modules.auth.vo.*;

/**
 * <p>
 * 鉴权 服务层
 * </p>
 */
public interface AuthService {

    /**
     * 通过账号+密码登录
     */
    AuthVo00 loginByPwd(String phone, String password);

    /**
     * 通过账号登录
     */
    AuthVo00 loginByCode(AuthVo03 authVo);

    /**
     * 注册
     */
    AuthVo00 register(AuthVo06 authVo);

    /**
     * 生成二维码
     */
    AuthVo05 getQrCode();

    /**
     * 确认二维码
     */
    void confirmQrCode(String token);

}
