package com.platform.modules.common.controller;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Index处理
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController extends BaseController {

    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/")
    public AjaxResult index() {
        return AjaxResult.success();
    }

}
