package com.platform.modules.chat.tencent;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 腾讯工具类
 */
@Component
public class TencentBuilder {

    @Autowired
    private TencentConfig config;

    /**
     * 语音识别
     */
    public String audio2Text(String voicePath) {
        if (!YesOrNoEnum.YES.getCode().equals(config.getEnabled())) {
            return "正在识别，请稍后再试";
        }
        try {
            Credential cred = new Credential(config.getAccessKey(), config.getSecretKey());
            // 创建客户端配置对象
            AsrClient client = new AsrClient(cred, null);
            SentenceRecognitionRequest request = new SentenceRecognitionRequest();
            request.setEngSerViceType("16k_zh");
            request.setVoiceFormat("aac");
            request.setSourceType(0L);
            request.setUrl(voicePath);
            SentenceRecognitionResponse response = client.SentenceRecognition(request);
            return response.getResult();
        } catch (Exception e) {
            throw new BaseException("语音识别接口调用异常，请稍后再试");
        }
    }

}
