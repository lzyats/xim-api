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
package com.platform.modules.auth.service.impl;

import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.ResultEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.modules.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token 服务层
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void generate(ShiroUserVo shiroUser) {
        String token = shiroUser.getToken();
        Map<String, Object> objectMap = shiroUser.toMap();
        redisUtils.hPutAll(makeKey(token), objectMap, PlatformConfig.TIMEOUT, TimeUnit.DAYS);
    }

    @Override
    public ShiroUserVo convert(String token) {
        String redisKey = makeKey(token);
        boolean result;
        try {
            result = redisUtils.hasKey(redisKey);
        } catch (Exception e) {
            throw new LoginException(ResultEnum.FAIL);
        }
        if (!result) {
            return null;
        }
        Map<Object, Object> objectMap = redisUtils.hEntries(redisKey);
        return ShiroUserVo.convert(objectMap);
    }

    @Override
    public void refresh(List<String> dataList, ShiroUserVo userVo) {
        Map<String, Object> objectMap = ShiroUserVo.refresh(userVo);
        dataList.forEach(token -> {
            String redisKey = this.makeKey(token);
            if (redisUtils.hasKey(redisKey)) {
                redisUtils.hPutAll(redisKey, objectMap, PlatformConfig.TIMEOUT, TimeUnit.DAYS);
            }
        });
    }

    @Override
    public void delete(String token) {
        if (StringUtils.isEmpty(token)) {
            return;
        }
        redisUtils.delete(makeKey(token));
    }

    private String makeKey(String token) {
        return HeadConstant.TOKEN_REDIS_APP + token;
    }

}
