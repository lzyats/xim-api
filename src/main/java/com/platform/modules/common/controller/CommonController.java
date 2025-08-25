package com.platform.modules.common.controller;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.config.PlatformConfig;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.service.ChatHelpService;
import com.platform.modules.chat.service.ChatNoticeService;
import com.platform.modules.chat.service.ChatVersionService;
import com.platform.modules.sys.domain.SysHtml;
import com.platform.modules.sys.service.SysHtmlService;
import com.platform.modules.common.service.CommonService;
import com.platform.modules.common.service.FileService;
import com.platform.modules.common.vo.CommonVo01;
import com.platform.modules.common.vo.CommonVo06;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.util.List;

/**
 * 通用请求处理
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController extends BaseController {

    @Resource
    private FileService fileService;

    @Resource
    private ChatVersionService versionService;

    @Resource
    private ChatHelpService chatHelpService;

    @Resource
    private CommonService commonService;

    @Resource
    private ChatFeedbackService feedbackService;

    @Resource
    private ChatNoticeService chatNoticeService;

    @Resource
    private SysHtmlService sysHtmlService;


    /**
     * 获取映射
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getMapping")
    public AjaxResult getMapping() {
        commonService.getMapping();
        return AjaxResult.success();
    }

    /**
     * 获取上传凭证
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @RequestMapping(value = "/getUploadToken/{fileExt}")
    public AjaxResult getUploadToken(@PathVariable String fileExt) {
        return AjaxResult.success(fileService.getUploadToken(fileExt));
    }

    /**
     * 上传文件
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        if (file == null) {
            throw new BaseException("上传文件不能为空");
        }
        return AjaxResult.success(fileService.upload(file));
    }

    /**
     * 获取上传凭证
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @RequestMapping(value = "/getUploadTokenu/{fileExt}")
    public AjaxResult getUploadTokenu(@PathVariable String fileExt) {
        return AjaxResult.success(fileService.getUploadTokenu(fileExt));
    }

    /**
     * 上传文件
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/uploadu")
    public AjaxResult uploadu(MultipartFile file) {
        if (file == null) {
            throw new BaseException("上传文件不能为空");
        }
        return AjaxResult.success(fileService.uploadu(file));
    }

    /**
     * 音频转换
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/audio2Text/{msgId}")
    public AjaxResult getVoice(@PathVariable Long msgId) {
        return AjaxResult.success(fileService.audio2Text(msgId));
    }

    /**
     * 升级校验
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/upgrade")
    public AjaxResult upgrade() {
        return AjaxResult.success(versionService.upgrade());
    }

    /**
     * 帮助中心
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getHelpList")
    public AjaxResult getHelpList() {
        orderBy("sort");
        List<LabelVo> dataList = chatHelpService.queryDataList();
        return AjaxResult.success(dataList);
    }

    /**
     * 获取配置
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        CommonVo06 data = commonService.getConfig();
        //log.info("密钥：{}",PlatformConfig.SECRET);
        return AjaxResult.success(data, PlatformConfig.SECRET);
    }

    /**
     * 建议反馈
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/feedback")
    public AjaxResult feedback(@Validated @RequestBody CommonVo01 commonVo) {
        feedbackService.addFeedback(commonVo);
        return AjaxResult.success();
    }

    /**
     * 通知列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getNoticeList")
    public TableDataInfo getNoticeList() {
        startPage("createTime desc");
        return getDataTable(chatNoticeService.queryDataList(),PlatformConfig.SECRET);
    }

    /**
     * 通知列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getHtml/{roulekey}")
    public AjaxResult getHtml(@PathVariable String roulekey) {
       SysHtml data= sysHtmlService.getInfo(roulekey);
        return AjaxResult.success(data,PlatformConfig.SECRET);
    }

}
