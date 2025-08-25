package com.platform.modules.chat.controller;

import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatGroupApplyService;
import com.platform.modules.chat.service.ChatGroupInformService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.service.ChatGroupSolitaireService;
import com.platform.modules.chat.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 群组
 */
@RestController
@Slf4j
@RequestMapping("/group")
public class GroupController extends BaseController {

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatGroupApplyService chatGroupApplyService;

    @Resource
    private ChatGroupSolitaireService chatGroupSolitaireService;

    @Resource
    private ChatGroupInformService chatGroupInformService;

    /**
     * 搜索
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/search")
    public TableDataInfo search(String param) {
        if (StringUtils.isEmpty(param)) {
            throw new BaseException("搜索参数不能为空");
        }
        PageInfo pageInfo = chatGroupService.searchGroup(param);
        return getDataTable(pageInfo);
    }

    /**
     * 扫码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/scan/{groupId}")
    public AjaxResult scan(@PathVariable Long groupId) {
        GroupVo39 data = chatGroupService.scan(groupId);
        return AjaxResult.success(data);
    }

    /**
     * 建群
     */
    @SubmitRepeat
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/create")
    public AjaxResult create(@Validated @RequestBody GroupVo16 groupVo) {
        chatGroupService.create(groupVo);
        return AjaxResult.success();
    }

    /**
     * 查询群组列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/groupList")
    public AjaxResult groupList() {
        return AjaxResult.success(chatGroupService.groupList(), PlatformConfig.SECRET);
    }

    /**
     * 详情
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getInfo/{groupId}")
    public AjaxResult getInfo(@PathVariable Long groupId) {
        GroupVo11 data = chatGroupService.getInfo(groupId);
        return AjaxResult.success(data);
    }

    /**
     * 加群
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/join")
    public AjaxResult join(@Validated @RequestBody GroupVo23 groupVo) {
        chatGroupService.join(groupVo);
        return AjaxResult.success();
    }

    /**
     * 成员
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getMemberList/{groupId}")
    public AjaxResult getMemberList(@PathVariable Long groupId) {
        return AjaxResult.success(chatGroupService.getMemberList(groupId));
    }

    /**
     * 邀请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/invite")
    public AjaxResult invite(@Validated @RequestBody GroupVo01 groupVo) {
        chatGroupService.invite(groupVo);
        return AjaxResult.success();
    }

    /**
     * 设置置顶
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setTop")
    public AjaxResult setTop(@Validated @RequestBody GroupVo04 groupVo) {
        chatGroupService.setTop(groupVo);
        return AjaxResult.success();
    }

    /**
     * 设置免打扰
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setDisturb")
    public AjaxResult setDisturb(@Validated @RequestBody GroupVo05 groupVo) {
        chatGroupService.setDisturb(groupVo);
        return AjaxResult.success();
    }

    /**
     * 设置群昵称
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setRemark")
    public AjaxResult setRemark(@Validated @RequestBody GroupVo10 groupVo) {
        chatGroupService.setRemark(groupVo);
        return AjaxResult.success();
    }

    /**
     * 退出
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/logout/{groupId}")
    public AjaxResult logout(@PathVariable Long groupId) {
        chatGroupService.logout(groupId);
        return AjaxResult.success();
    }

    /**
     * 踢人
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/kicked")
    public AjaxResult kicked(@Validated @RequestBody GroupVo24 groupVo) {
        chatGroupService.kicked(groupVo);
        return AjaxResult.success();
    }

    /**
     * 修改群名
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editGroupName")
    public AjaxResult editGroupName(@Validated @RequestBody GroupVo02 groupVo) {
        chatGroupService.editGroupName(groupVo);
        return AjaxResult.success();
    }

    /**
     * 修改头像
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editPortrait")
    public AjaxResult editPortrait(@Validated @RequestBody GroupVo09 groupVo) {
        chatGroupService.editPortrait(groupVo);
        return AjaxResult.success();
    }

    /**
     * 群组公告
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editNotice")
    public AjaxResult editNotice(@Validated @RequestBody GroupVo03 groupVo) {
        chatGroupService.editNotice(groupVo);
        return AjaxResult.success();
    }

    /**
     * 设置公告置顶
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editNoticeTop")
    public AjaxResult editNoticeTop(@Validated @RequestBody GroupVo07 groupVo) {
        chatGroupService.editNoticeTop(groupVo);
        return AjaxResult.success();
    }

    /**
     * 成员昵称
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/setNickname")
    public AjaxResult setNickname(@Validated @RequestBody GroupVo21 groupVo) {
        chatGroupService.setNickname(groupVo);
        return AjaxResult.success();
    }

    /**
     * 成员保护
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigMember")
    public AjaxResult editConfigMember(@Validated @RequestBody GroupVo06 groupVo) {
        chatGroupService.editConfigMember(groupVo);
        return AjaxResult.success();
    }

    /**
     * 成员邀请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigInvite")
    public AjaxResult editConfigInvite(@Validated @RequestBody GroupVo12 groupVo) {
        chatGroupService.editConfigInvite(groupVo);
        return AjaxResult.success();
    }

    /**
     * 群组头衔
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigTitle")
    public AjaxResult editConfigTitle(@Validated @RequestBody GroupVo17 groupVo) {
        chatGroupService.editConfigTitle(groupVo);
        return AjaxResult.success();
    }

    /**
     * 红包开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigPacket")
    public AjaxResult editConfigPacket(@Validated @RequestBody GroupVo18 groupVo) {
        chatGroupService.editConfigPacket(groupVo);
        return AjaxResult.success();
    }

    /**
     * 显示金额
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigAmount")
    public AjaxResult editConfigAmount(@Validated @RequestBody GroupVo44 groupVo) {
        chatGroupService.editConfigAmount(groupVo);
        return AjaxResult.success();
    }

    /**
     * 接收开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigReceive")
    public AjaxResult editConfigReceive(@Validated @RequestBody GroupVo36 groupVo) {
        chatGroupService.editConfigReceive(groupVo);
        return AjaxResult.success();
    }

    /**
     * 二维码开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigScan")
    public AjaxResult editConfigScan(@Validated @RequestBody GroupVo46 groupVo) {
        chatGroupService.editConfigScan(groupVo);
        return AjaxResult.success();
    }

    /**
     * 专属可见
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigAssign")
    public AjaxResult editConfigAssign(@Validated @RequestBody GroupVo19 groupVo) {
        chatGroupService.editConfigAssign(groupVo);
        return AjaxResult.success();
    }

    /**
     * 资源开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigMedia")
    public AjaxResult editConfigMedia(@Validated @RequestBody GroupVo20 groupVo) {
        chatGroupService.editConfigMedia(groupVo);
        return AjaxResult.success();
    }

    /**
     * 全员禁言
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigSpeak")
    public AjaxResult editConfigSpeak(@Validated @RequestBody GroupVo13 groupVo) {
        chatGroupService.editConfigSpeak(groupVo);
        return AjaxResult.success();
    }

    /**
     * 解散
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/dissolve/{groupId}")
    public AjaxResult dissolve(@PathVariable Long groupId) {
        chatGroupService.dissolve(groupId);
        return AjaxResult.success();
    }

    /**
     * 转让
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/transfer")
    public AjaxResult transfer(@Validated @RequestBody GroupVo14 groupVo) {
        chatGroupService.transfer(groupVo);
        return AjaxResult.success();
    }

    /**
     * 设置管理员
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/setManager")
    public AjaxResult setManager(@Validated @RequestBody GroupVo15 groupVo) {
        chatGroupService.setManager(groupVo);
        return AjaxResult.success();
    }

    /**
     * 审核开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigAudit")
    public AjaxResult editConfigAudit(@Validated @RequestBody GroupVo22 groupVo) {
        chatGroupService.editConfigAudit(groupVo);
        return AjaxResult.success();
    }

    /**
     * 群内昵称
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editConfigNickname")
    public AjaxResult editConfigNickname(@Validated @RequestBody GroupVo35 groupVo) {
        chatGroupService.editConfigNickname(groupVo);
        return AjaxResult.success();
    }

    /**
     * 隐私开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editPrivacyNo")
    public AjaxResult editPrivacyNo(@Validated @RequestBody GroupVo34 groupVo) {
        chatGroupService.editPrivacyNo(groupVo);
        return AjaxResult.success();
    }

    /**
     * 隐私开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editPrivacyScan")
    public AjaxResult editPrivacyScan(@Validated @RequestBody GroupVo38 groupVo) {
        chatGroupService.editPrivacyScan(groupVo);
        return AjaxResult.success();
    }

    /**
     * 隐私开关
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editPrivacyName")
    public AjaxResult editPrivacyScan(@Validated @RequestBody GroupVo45 groupVo) {
        chatGroupService.editPrivacyName(groupVo);
        return AjaxResult.success();
    }

    /**
     * 成员禁言
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/speak")
    public AjaxResult speak(@Validated @RequestBody GroupVo26 groupVo) {
        chatGroupService.speak(groupVo);
        return AjaxResult.success();
    }

    /**
     * 扩容价格
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/groupLevelPrice/{groupId}")
    public AjaxResult groupLevelPrice(@PathVariable Long groupId) {
        List<GroupVo27> dataList = chatGroupService.groupLevelPrice(groupId);
        return AjaxResult.success(dataList);
    }

    /**
     * 扩容支付
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/groupLevelPay")
    public AjaxResult groupLevelPay(@Validated @RequestBody GroupVo28 groupVo) {
        chatGroupService.groupLevelPay(groupVo);
        return AjaxResult.success();
    }

    /**
     * 查询白名单
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/queryPacketWhite/{groupId}")
    public AjaxResult queryPacketWhite(@PathVariable Long groupId) {
        List<Long> userList = chatGroupService.queryPacketWhite(groupId);
        return AjaxResult.success(userList);
    }

    /**
     * 设置白名单
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/manager/editPacketWhite")
    public AjaxResult editPacketWhite(@Validated @RequestBody GroupVo37 groupVo) {
        chatGroupService.editPacketWhite(groupVo);
        return AjaxResult.success();
    }

    /**
     * 申请记录
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/applyList")
    public TableDataInfo applyList() {
        return getDataTable(chatGroupApplyService.queryDataList());
    }

    /**
     * 同意申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/applyAgree/{applyId}")
    public AjaxResult applyAgree(@PathVariable Long applyId) {
        chatGroupApplyService.agree(applyId);
        return AjaxResult.success();
    }

    /**
     * 忽略申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/applyReject/{applyId}")
    public AjaxResult applyReject(@PathVariable Long applyId) {
        chatGroupApplyService.ignore(applyId);
        return AjaxResult.success();
    }

    /**
     * 删除申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/manager/applyDelete/{applyId}")
    public AjaxResult applyDelete(@PathVariable Long applyId) {
        chatGroupApplyService.applyDelete(applyId);
        return AjaxResult.success();
    }

    /**
     * 申请举报
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/inform")
    public AjaxResult inform(@Validated @RequestBody GroupVo40 groupVo) {
        chatGroupInformService.inform(groupVo);
        return AjaxResult.success();
    }

    /**
     * 创建接龙
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/solitaire/create")
    public AjaxResult createSolitaire(@Validated @RequestBody GroupVo29 groupVo) {
        GroupVo41 data = chatGroupSolitaireService.createSolitaire(groupVo);
        return AjaxResult.success(data);
    }

    /**
     * 查询接龙
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/solitaire/query")
    public AjaxResult querySolitaire(@Validated @RequestBody GroupVo31 groupVo) {
        GroupVo43 data = chatGroupSolitaireService.querySolitaire(groupVo);
        return AjaxResult.success(data);
    }

    /**
     * 提交接龙
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/solitaire/submit")
    public AjaxResult submitSolitaire(@Validated @RequestBody GroupVo32 groupVo) {
        GroupVo42 data = chatGroupSolitaireService.submitSolitaire(groupVo);
        return AjaxResult.success(data);
    }

}
