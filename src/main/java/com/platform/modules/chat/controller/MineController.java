package com.platform.modules.chat.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.GenderEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatUserInfoService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.*;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.common.service.MessageService;
import com.platform.modules.chat.service.ChatUserSignService;
import com.platform.modules.chat.service.ChatPortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 我的
 */
@RestController
@Slf4j
@RequestMapping("/mine")
public class MineController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private  ChatUserSignService chatUserSignService;

    @Resource
    private  ChatPortraitService chatPortraitService;

    @Resource
    private MessageService messageService;

    /**
     * 设置密码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setPass")
    public AjaxResult setPass(@Validated @RequestBody MineVo04 mineVo) {
        chatUserService.setPass(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改密码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPass")
    public AjaxResult editPass(@Validated @RequestBody MineVo01 mineVo) {
        // 执行重置
        chatUserService.editPass(mineVo.getOldPwd(), mineVo.getNewPwd());
        return AjaxResult.success();
    }

    /**
     * 找回密码（根据账号）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/forget")
    public AjaxResult forget(@Validated @RequestBody MineVo21 mineVo) {
        // 验证
        String phone = ShiroUtils.getPhone();
        messageService.verifySms(phone, mineVo.getCode(), MessageTypeEnum.RETRIEVE);
        // 重置
        chatUserService.resetPass(phone, mineVo.getPassword());
        return AjaxResult.success();
    }

    /**
     * 获取基本信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(chatUserService.getInfo(),PlatformConfig.SECRET);
    }

    /**
     * 获取基本信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getAva")
    public AjaxResult getAva() {
        List<String> data=chatPortraitService.queryPortraitList(ChatTalkEnum.FRIEND);
        //return AjaxResult.success(data);
        return AjaxResult.success(data,PlatformConfig.SECRET);
    }

    /**
     * 修改头像
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPortrait")
    public AjaxResult editPortrait(@Validated @RequestBody MineVo02 mineVo) {
        chatUserService.editPortrait(mineVo.getPortrait());
        return AjaxResult.success();
    }

    /**
     * 修改昵称
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editNickname")
    public AjaxResult editNickname(@Validated @RequestBody MineVo03 mineVo) {
        chatUserService.editNickname(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改性别
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editGender")
    public AjaxResult editGender(@Validated @RequestBody MineVo05 mineVo) {
        GenderEnum gender = mineVo.getGender();
        switch (gender) {
            case UNKNOWN:
                throw new BaseException("性别不能为空");
        }
        chatUserService.editGender(gender);
        return AjaxResult.success();
    }

    /**
     * 修改个性签名
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editIntro")
    public AjaxResult editIntro(@Validated @RequestBody MineVo07 mineVo) {
        chatUserService.editIntro(mineVo.getIntro());
        return AjaxResult.success();
    }

    /**
     * 修改省市
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editCity")
    public AjaxResult editCity(@Validated @RequestBody MineVo08 mineVo) {
        chatUserService.editCity(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改生日
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editBirthday")
    public AjaxResult editBirthday(@Validated @RequestBody MineVo12 mineVo) {
        chatUserService.editBirthday(mineVo.getBirthday());
        return AjaxResult.success();
    }

    /**
     * 用户注销
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/deleted")
    public AjaxResult deleted() {
        chatUserService.deleted();
        return AjaxResult.success();
    }

    /**
     * 刷新
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/refresh")
    public AjaxResult refresh() {
        chatUserService.refresh();
        return AjaxResult.success();
    }

    /**
     * 修改隐私no
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPrivacyNo")
    public AjaxResult editPrivacyNo(@Validated @RequestBody MineVo15 mineVo) {
        chatUserService.editPrivacyNo(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改隐私账号
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPrivacyPhone")
    public AjaxResult editPrivacyPhone(@Validated @RequestBody MineVo14 mineVo) {
        chatUserService.editPrivacyPhone(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改隐私扫码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPrivacyScan")
    public AjaxResult editPrivacyScan(@Validated @RequestBody MineVo13 mineVo) {
        chatUserService.editPrivacyScan(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改隐私名片
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPrivacyCard")
    public AjaxResult editPrivacyCard(@Validated @RequestBody MineVo11 mineVo) {
        chatUserService.editPrivacyCard(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改隐私群组
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editPrivacyGroup")
    public AjaxResult editPrivacyGroup(@Validated @RequestBody MineVo10 mineVo) {
        chatUserService.editPrivacyGroup(mineVo);
        return AjaxResult.success();
    }

    /**
     * 查询认证
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getAuth")
    public AjaxResult getAuth() {
        MineVo18 data = chatUserInfoService.getAuthInfo();
        return AjaxResult.success(data);
    }

    /**
     * 修改认证
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editAuth")
    public AjaxResult editAuth(@Validated @RequestBody MineVo09 mineVo) {
        chatUserInfoService.editAuth(mineVo);
        return AjaxResult.success();
    }

    /**
     * 修改邮箱
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/editEmail")
    public AjaxResult editEmail(@Validated @RequestBody MineVo19 mineVo) {
        // 验证
        String phone = ShiroUtils.getPhone();
        String code = mineVo.getCode();
        messageService.verifySms(phone, code, MessageTypeEnum.EMAIL);
        // 验证
        String email = mineVo.getEmail();
        if (!Validator.isEmail(email)) {
            throw new BaseException("请输入正确的邮箱");
        }
        // 修改
        chatUserService.editEmail(email);
        return AjaxResult.success();
    }

    /**
     * 发送短信（钱包/邮箱/绑定）
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping(value = "/sendCode")
    @SubmitRepeat
    public AjaxResult sendCode(@Validated @RequestBody MineVo20 mineVo) {
        // 账号
        String phone = ShiroUtils.getPhone();
        // 类型
        MessageTypeEnum messageType = mineVo.getType();
        // 分发
        switch (messageType) {
            case WALLET:
            case EMAIL:
            case RETRIEVE:
            case BINDING:
                break;
            default:
                throw new BaseException("短信类型不正确");
        }
        Dict data = chatUserService.sendCode(phone, null,null, messageType);
        return AjaxResult.success(data);
    }

    /**
     * 获取指定用户签到记录
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getSignInfo")
    public AjaxResult getSignInfo() {
        return AjaxResult.success(chatUserSignService.getSignStats());
    }

    /**
    * 用户签到
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/sign")
    public AjaxResult sign() {
        return AjaxResult.success(chatUserSignService.sign());
    }

    /**
     * 用户签到列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getSignList")
    public TableDataInfo getSignList() {
        startPage("createTime desc");
        return getDataTable(chatUserSignService.getSignList());
    }
}
