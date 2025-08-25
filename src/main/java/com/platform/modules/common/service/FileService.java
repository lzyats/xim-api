package com.platform.modules.common.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.upload.vo.UploadFileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 */
public interface FileService {

    /**
     * 获取上传凭证
     */
    Dict getUploadToken(String fileExt);

    /**
     * 获取上传凭证系统专用
     */
    Dict getUploadTokenu(String fileExt);

    /**
     * 文件音频
     */
    void uploadVoice(Long msgId, String voicePath);

    /**
     * 上传文件
     */
    UploadFileVo upload(MultipartFile file);

    /**
     * 上传文件系统专用
     */
    UploadFileVo uploadu(MultipartFile file);

    /**
     * 文件音频
     */
    String audio2Text(Long msgId);

}
