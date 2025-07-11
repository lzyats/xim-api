package com.platform.modules.test;

import cn.hutool.core.lang.Console;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.utils.CodeUtils;
import com.platform.common.utils.EncryptUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController extends BaseController {

    /**
     * 测试
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/test1")
    public AjaxResult test1() {
        return AjaxResult.success();
    }

    /**
     * 测试
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/test2")
    public AjaxResult test2() {
        return AjaxResult.success();
    }

    public static void main(String[] args) {
        Console.log(EncryptUtils.generate());
        Console.log("f9d2cae0802ed380dd7b0c050661a428".equals(CodeUtils.credentials(CodeUtils.md5("lcm040217"), "o3dt")));
        Console.log("b74894d03ca5536d912c85b9c4c597ca".equals(CodeUtils.md5(CodeUtils.md5("168168"))));
        Console.log("b74894d03ca5536d912c85b9c4c597ca".equals(CodeUtils.md5(CodeUtils.md5("168168"))));
    }

}
