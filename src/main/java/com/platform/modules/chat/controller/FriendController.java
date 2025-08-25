package com.platform.modules.chat.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatFriendApplyService;
import com.platform.modules.chat.service.ChatFriendInformService;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 好友
 */
@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController extends BaseController {

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatFriendApplyService chatFriendApplyService;

    @Resource
    private ChatFriendInformService chatFriendInformService;

    /**
     * 搜索好友
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/search/{param}")
    public AjaxResult searchFriend(@PathVariable String param) {
        FriendVo01 data = chatFriendService.searchFriend(StrUtil.trim(param));
        return AjaxResult.success(data);
    }

    /**
     * 好友详情
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getInfo/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        FriendVo09 data = chatFriendService.getInfo(userId);
        return AjaxResult.success(data);
    }

    /**
     * 好友列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getFriendList")
    public AjaxResult getFriendList() {
        List<FriendVo09> dataList = chatFriendService.getFriendList();
        return AjaxResult.success(dataList, PlatformConfig.SECRET);
    }

    /**
     * 设置黑名单
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setBlack")
    public AjaxResult setBlack(@Validated @RequestBody FriendVo03 friendVo) {
        chatFriendService.setBlack(friendVo);
        return AjaxResult.success();
    }

    /**
     * 删除好友
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/delFriend")
    public AjaxResult delFriend(@Validated @RequestBody FriendVo04 friendVo) {
        chatFriendService.delFriend(friendVo.getUserId());
        return AjaxResult.success();
    }

    /**
     * 设置备注
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setRemark")
    public AjaxResult setRemark(@Validated @RequestBody FriendVo05 friendVo) {
        chatFriendService.setRemark(friendVo);
        return AjaxResult.success();
    }

    /**
     * 设置置顶
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setTop")
    public AjaxResult setTop(@Validated @RequestBody FriendVo06 friendVo) {
        chatFriendService.setTop(friendVo);
        return AjaxResult.success();
    }

    /**
     * 设置静默
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setDisturb")
    public AjaxResult setDisturb(@Validated @RequestBody FriendVo08 friendVo) {
        chatFriendService.setDisturb(friendVo);
        return AjaxResult.success();
    }

    /**
     * 申请好友
     */
    @SubmitRepeat
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/apply")
    public AjaxResult applyFriend(@Validated @RequestBody FriendVo02 friendVo) {
        chatFriendApplyService.applyFriend(friendVo);
        return AjaxResult.success();
    }

    /**
     * 申请记录
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/applyList")
    public TableDataInfo applyList() {
        startPage("case status when 1 then 1 else 2 end, createTime desc");
        PageInfo pageInfo = chatFriendApplyService.queryDataList();
        return getDataTable(pageInfo);
    }

    /**
     * 申请同意
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/applyAgree")
    public AjaxResult applyAgree(@Validated @RequestBody FriendVo11 friendVo) {
        chatFriendApplyService.agree(friendVo);
        return AjaxResult.success();
    }

    /**
     * 忽略申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/applyReject/{applyId}")
    public AjaxResult applyReject(@PathVariable Long applyId) {
        chatFriendApplyService.reject(applyId);
        return AjaxResult.success();
    }

    /**
     * 删除申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/applyDelete/{applyId}")
    public AjaxResult applyDelete(@PathVariable Long applyId) {
        chatFriendApplyService.applyDelete(applyId);
        return AjaxResult.success();
    }

    /**
     * 申请举报
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/inform")
    public AjaxResult inform(@Validated @RequestBody FriendVo07 friendVo) {
        chatFriendInformService.inform(friendVo);
        return AjaxResult.success();
    }

}
