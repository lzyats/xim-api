package com.platform.modules.common.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import com.platform.common.upload.enums.UploadTypeEnum;
import com.platform.common.upload.service.UploadService;
import com.platform.common.upload.service.UploadServiceu;
import com.platform.common.upload.vo.UploadFileVo;
import com.platform.modules.chat.domain.ChatVoice;
import com.platform.modules.chat.service.ChatResourceService;
import com.platform.modules.chat.service.ChatVoiceService;
import com.platform.modules.chat.tencent.TencentBuilder;
import com.platform.modules.common.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Resource
    private UploadService uploadService;

    @Resource
    private UploadServiceu uploadServiceu;

    @Autowired
    private TencentBuilder tencentBuilder;

    @Resource
    private ChatVoiceService chatVoiceService;

    @Resource
    private ChatResourceService chatResourceService;

    @Override
    public Dict getUploadToken(String fileExt) {
        Dict data = uploadService.getFileToken(fileExt);
        String filePath = data.getStr("filePath");
        UploadTypeEnum uploadType = data.getEnum(UploadTypeEnum.class, "uploadType");
        if (!UploadTypeEnum.LOCAL.equals(uploadType)) {
            chatResourceService.addResource(filePath);
        }
        return data;
    }

    @Override
    public Dict getUploadTokenu(String fileExt) {
        log.info("使用系统预上传");
        Dict data = uploadServiceu.getFileToken(fileExt);
        String filePath = data.getStr("filePath");
        UploadTypeEnum uploadType = data.getEnum(UploadTypeEnum.class, "uploadType");
        if (!UploadTypeEnum.LOCAL.equals(uploadType)) {
            chatResourceService.addResource(filePath);
        }
        return data;
    }

    @Override
    public void uploadVoice(Long msgId, String voicePath) {
        // 新增
        chatVoiceService.addVoice(msgId, voicePath);
        // 解析
        this.parsingVoice(msgId, voicePath);
    }

    @Override
    public UploadFileVo upload(MultipartFile file) {
        UploadFileVo data = uploadService.uploadFile(file);
        chatResourceService.addResource(data.getFilePath());
        return data;
    }

    @Override
    public UploadFileVo uploadu(MultipartFile file) {
        log.info("使用系统上传");
        UploadFileVo data = uploadServiceu.uploadFile(file);
        chatResourceService.addResource(data.getFilePath());
        return data;
    }

    @Override
    public String audio2Text(Long msgId) {
        ChatVoice chatVoice = chatVoiceService.getById(msgId);
        if (chatVoice == null) {
            return "";
        }
        String voiceText = chatVoice.getVoiceText();
        if (!StringUtils.isEmpty(voiceText)) {
            return voiceText;
        }
        // 解析
        this.parsingVoice(msgId, chatVoice.getVoiceUrl());
        return "正在识别，请稍后再试";
    }

    /**
     * 解析声音
     */
    private void parsingVoice(Long msgId, String voicePath) {
        ThreadUtil.execAsync(() -> {
            String voiceText = tencentBuilder.audio2Text(voicePath);
            ChatVoice chatVoice = new ChatVoice()
                    .setMsgId(msgId)
                    .setVoiceText(voiceText);
            chatVoiceService.updateById(chatVoice);
        });
    }

}
