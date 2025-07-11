package com.platform.modules.uni.controller;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.uni.domain.UniItem;
import com.platform.modules.uni.service.UniItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小程序
 */
@RestController
@Slf4j
@RequestMapping("/uni")
public class UniController extends BaseController {

    @Resource
    private UniItemService uniItemService;

    /**
     * 获取列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/list")
    public AjaxResult getInfo() {
        List<UniItem> dataList = uniItemService.queryDataList();
        return AjaxResult.success(dataList);
    }

}
