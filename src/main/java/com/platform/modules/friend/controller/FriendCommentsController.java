package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.friend.vo.*;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendCommentsService;
import com.platform.modules.friend.domain.FriendComments;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 朋友圈评论表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/comments")
public class FriendCommentsController extends BaseController {

    private final static String title = "朋友圈评论表";

    @Resource
    private FriendCommentsService friendCommentsService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendComments friendComments) {
        startPage();
        List<FriendComments> list = friendCommentsService.queryList(friendComments);
        return getDataTable(list);
    }

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:list"})
    @GetMapping(value = "/listall/{momentId}")
    public TableDataInfo listall(@PathVariable Long momentId) {
        QueryWrapper<FriendComments> wrapper = new QueryWrapper<>();
        wrapper
                .eq("moment_id", momentId)
                .orderByDesc("create_time");
        List<FriendComments> list = friendCommentsService.queryList(wrapper);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:comments:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(friendCommentsService.getById(id));
    }


    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendComments friendComments) {
        friendCommentsService.updateById(friendComments);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendCommentsService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendCommentsService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }


}

