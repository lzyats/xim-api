package com.platform.modules.friend.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendMediasService;
import com.platform.modules.friend.domain.FriendMedias;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import com.platform.modules.friend.vo.*;

/**
 * <p>
 * 朋友圈媒体资源表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/medias")
public class FriendMediasController extends BaseController {

    private final static String title = "朋友圈媒体资源表";

    @Resource
    private FriendMediasService friendMediasService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMedias friendMedias) {
        startPage();
        List<FriendMedias> list = friendMediasService.queryList(friendMedias);
        return getDataTable(list);
    }




}

