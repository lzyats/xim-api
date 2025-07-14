package com.platform.modules.friend.controller;

import javax.annotation.Resource;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.*;
import com.platform.common.web.controller.BaseController;
import com.platform.modules.friend.vo.*;

import java.util.List;

/**
 * <p>
 * 朋友圈动态表 控制层
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/friend/moments")
public class FriendMomentsController extends BaseController {

    private final static String title = "朋友圈动态表";

    @Resource
    private FriendMomentsService friendMomentsService;

    @Resource
    private FriendCommentsService friendCommentsService;

    @Resource
    private FriendMediasService friendMediasService;

    @Resource
    private FriendLikesService friendLikesService;

    /**
     * 列表数据 TODO
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getlist/{userId}")
    public AjaxResult getlist(@PathVariable Long userId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        // 创建 Pageable 对象
        Pageable pageable = PageRequest.of(page, size);
        // 调用支持分页的服务方法
        Page<MomentVo01> momentVo01Page = friendMomentsService.getlist(userId, pageable);
        return AjaxResult.success(momentVo01Page);
    }




}

