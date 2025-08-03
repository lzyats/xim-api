package com.platform.modules.friend.controller;

import javax.annotation.Resource;

import cn.hutool.core.util.StrUtil;
import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.impl.ChatVersionServiceImpl;
import com.platform.modules.friend.domain.FriendComments;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.*;
import com.platform.modules.friend.domain.FriendLikes;
import com.platform.common.web.controller.BaseController;
import com.platform.modules.friend.vo.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisUtils redisUtils;

    public static final String REDIS_CHAT_ROBOT = "friend:moments";

    // 初始化日志对象（注意：这里应使用当前类的字节码对象）
    private static final Logger logger = LoggerFactory.getLogger(ChatVersionServiceImpl.class);

    /**
     * 列表数据
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getlist/{userId}")
    public AjaxResult getlist(@PathVariable Long userId,
                              @RequestParam Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        //logger.info("查询朋友圈信息，userId:{},pageNum: {},pageSize:{}", userId,pageNum,pageSize);

        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        PageInfo<MomentVo01> pageInfo = friendMomentsService.getlist(userId);  // 直接获取分页结果



        //logger.info("返回数:{}", pageInfo);

        return AjaxResult.success(pageInfo);
    }

    /**
     * 列表数据
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getlistbyid/{userId}")
    public AjaxResult getlistbyid(@PathVariable Long userId,
                              @RequestParam Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        //logger.info("查询朋友圈信息，userId:{},pageNum: {},pageSize:{}", userId,pageNum,pageSize);

        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        PageInfo<MomentVo01> pageInfo = friendMomentsService.getlistbyid(userId);  // 直接获取分页结果



        //logger.info("返回数:{}", pageInfo);

        return AjaxResult.success(pageInfo);
    }

    /**
     * 点赞
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/addlike")
    public AjaxResult addlike(@Validated @RequestBody FriendLikes friendLikes) {
        logger.info("点赞朋友圈信息：{}", friendLikes);
        //验证缓存
        String redisKey = REDIS_CHAT_ROBOT+":likes:"+friendLikes.getMomentId()+"-"+friendLikes.getUserId();
        if (redisUtils.hasKey(redisKey)) {
            return AjaxResult.fail("你已经点赞过该动态");
        }
        Integer expired = 600;
        // 查询是否存在该数据
        boolean exists = friendLikesService.existsByMomentIdAndUserId(friendLikes.getMomentId(), friendLikes.getUserId());
        if (exists) {
            // 写缓存
            redisUtils.set(redisKey,"1",expired, TimeUnit.SECONDS);
            return AjaxResult.fail("你已经对该动态点过赞");
        }
        friendLikesService.add(friendLikes);
        // 写缓存
        redisUtils.set(redisKey,"1",expired, TimeUnit.SECONDS);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 发表评论
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/comment")
    public AjaxResult comment(@Validated @RequestBody FriendComments friendComments) {
        logger.info("评论朋友圈信息：{}", friendComments);
        //限制同一条信息同一个人只能评论3条
        String redisKey = REDIS_CHAT_ROBOT+":comment:"+friendComments.getMomentId()+"-"+friendComments.getUserId();
        Long count = redisUtils.increment(redisKey, 1, 1, TimeUnit.DAYS);
        if (count > 5) {
            return AjaxResult.fail("同一信息每天最多评论3条");
        }
        friendCommentsService.add(friendComments);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 发表朋友圈信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/admomnet")
    public AjaxResult admomnet(@Validated @RequestBody MomentVo02 momentVo02) {
        friendMomentsService.admomnet(momentVo02);
        return AjaxResult.successMsg("新增成功");
    }

}

