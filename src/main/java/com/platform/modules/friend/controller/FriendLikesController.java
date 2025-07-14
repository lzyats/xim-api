package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendLikesService;
import com.platform.modules.friend.domain.FriendLikes;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 朋友圈点赞表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/likes")
public class FriendLikesController extends BaseController {

    private final static String title = "朋友圈点赞表";

    @Resource
    private FriendLikesService friendLikesService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:likes:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendLikes friendLikes) {
        startPage();
        List<FriendLikes> list = friendLikesService.queryList(friendLikes);
        return getDataTable(list);
    }

}

