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

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:list"})
    @GetMapping(value = "/listall/{momentId}")
    public TableDataInfo listall(@PathVariable Long momentId) {
        QueryWrapper<FriendMedias> wrapper = new QueryWrapper<>();
        wrapper
                .eq("moment_id", momentId)
                .orderByDesc("create_time");
        List<FriendMedias> list = friendMediasService.queryList(wrapper);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:medias:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(friendMediasService.getById(id));
    }


    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendMedias friendMedias) {
        // 输出字符串（多个字符，使用双引号）
        System.out.println("Hello, World!"); // 输出字符串并换行
        friendMediasService.updateById(friendMedias);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendMediasService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendMediasService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }


}

