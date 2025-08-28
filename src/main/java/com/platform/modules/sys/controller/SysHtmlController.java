package com.platform.modules.sys.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.sys.service.SysHtmlService;
import com.platform.modules.sys.domain.SysHtml;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * APP网页定制 控制层
 * </p>
 */
@RestController
@RequestMapping("/sys/html")
public class SysHtmlController extends BaseController {

    private final static String title = "APP网页定制";

    @Resource
    private SysHtmlService sysHtmlService;

    /**
     * 列表数据 TODO
     */
    @GetMapping(value = "/list")
    public TableDataInfo list() {
        startPage();
        List<SysHtml> list = sysHtmlService.queryList();
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @GetMapping("/info/{roulekey}")
    public AjaxResult getInfo(@PathVariable String roulekey) {
        SysHtml data=sysHtmlService.getInfo(roulekey);
        return AjaxResult.success(data, PlatformConfig.SECRET);
    }
}

