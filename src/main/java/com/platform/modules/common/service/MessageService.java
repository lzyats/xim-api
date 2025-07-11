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
package com.platform.modules.common.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.common.enums.MessageTypeEnum;

/**
 * <p>
 * 短信 服务层
 * </p>
 */
public interface MessageService {

    /**
     * 发送短信
     */
    Dict sendSms(String phone, String email, MessageTypeEnum messageType, YesOrNoEnum special);

    /**
     * 验证短信
     */
    void verifySms(String phone, String code, MessageTypeEnum messageType);

}
