package com.platform.modules.chat.controller;

import cn.hutool.json.JSONObject;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.vo.*;
import com.platform.modules.push.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 聊天
 */
@RestController
@Slf4j
@RequestMapping("/msg")
public class MessageController extends BaseController {

    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private PushService pushService;

    /**
     * 发送信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/sendFriendMsg")
    public AjaxResult sendFriendMsg(@Validated @RequestBody ChatVo01 chatVo) {
        Long userId = chatVo.getUserId();
        Long current = ShiroUtils.getUserId();
        // 自己消息
        if (current.equals(userId)) {
            switch (chatVo.getMsgType()) {
                case TEXT:
                case IMAGE:
                case VIDEO:
                case VOICE:
                case FILE:
                case LOCATION:
                case CARD:
                case RECALL:
                case REPLY:
                case FORWARD:
                    break;
                default:
                    throw new BaseException("不支持的消息类型");
            }
        }
        // 好友消息
        else {
            switch (chatVo.getMsgType()) {
                case TEXT:
                case IMAGE:
                case VIDEO:
                case VOICE:
                case FILE:
                case LOCATION:
                case CARD:
                case CALL:
                case TRANSFER:
                case PACKET:
//                case SNAP:
                case RECALL:
                case REPLY:
                case FORWARD:
                    break;
                default:
                    throw new BaseException("不支持的消息类型");
            }
        }
        ChatVo00 data;
        // 发送消息
        if (current.equals(userId)) {
            data = chatMsgService.sendSelfMsg(chatVo);
        } else {
            data = chatMsgService.sendFriendMsg(chatVo);
        }
        return AjaxResult.success(data);
    }

    /**
     * 发送信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/sendGroupMsg")
    public AjaxResult sendGroupMsg(@Validated @RequestBody ChatVo02 chatVo) {
        switch (chatVo.getMsgType()) {
            default:
                throw new BaseException("消息类型错误");
            case TEXT:
            case IMAGE:
            case VOICE:
            case VIDEO:
            case FILE:
            case LOCATION:
            case CARD:
            case GROUP_ASSIGN:
            case GROUP_LUCK:
            case GROUP_PACKET:
            case GROUP_TRANSFER:
            case AT:
            case RECALL:
            case FORWARD:
            case REPLY:
//            case SOLITAIRE:
                break;
        }
        ChatVo00 data = chatMsgService.sendGroupMsg(chatVo);
        return AjaxResult.success(data);
    }

    /**
     * 发送信息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/sendRobotMsg")
    public AjaxResult sendRobotMsg(@Validated @RequestBody ChatVo03 chatVo) {
        switch (chatVo.getMsgType()) {
            case TEXT:
            case IMAGE:
            case VOICE:
            case VIDEO:
            case FILE:
            case LOCATION:
            case CARD:
            case RECALL:
            case REPLY:
            case EVEN:
                break;
            default:
                throw new BaseException("消息类型不正确");
        }
        ChatVo00 data = chatMsgService.sendRobotMsg(chatVo);
        return AjaxResult.success(data);
    }

    /**
     * 获取消息ID
     */
    @Deprecated
    @VersionRepeat(value = VersionEnum.V1_0_0)
    @GetMapping("/getMsgId")
    public AjaxResult getMsgId() {
        ChatVo05 data = chatMsgService.getMsgId();
        return AjaxResult.success(data);
    }

    /**
     * 拉取消息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/pullMsg")
    public AjaxResult pullMsg() {
        List<JSONObject> dataList = chatMsgService.pullMsg();
        return AjaxResult.success(dataList);
    }

    /**
     * 拉取朋友圈消息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/pullMomentMsg")
    public AjaxResult pullMomentMsg() {
        List<JSONObject> dataList = chatMsgService.pullMsg();
        return AjaxResult.success(dataList);
    }

    /**
     * 清空消息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/clearMsg/{groupId}")
    public AjaxResult clearMsg(@PathVariable Long groupId) {
        Long current = ShiroUtils.getUserId();
        pushService.clearMsg(current, groupId);
        return AjaxResult.success();
    }

    /**
     * 清空消息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/deleteMsg")
    public AjaxResult deleteMsg() {
        Long current = ShiroUtils.getUserId();
        pushService.removeMsg(current);
        return AjaxResult.success();
    }

    /**
     * 删除消息
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/removeMsg")
    public AjaxResult removeMsg(@RequestBody ChatVo04 chatVo) {
        Long current = ShiroUtils.getUserId();
        chatMsgService.removeMsg(current, chatVo.getDataList());
        return AjaxResult.success();
    }

    /**
     * 更新音视频
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/callKit")
    public AjaxResult callKit(@Validated @RequestBody ChatVo06 chatVo) {
        String data = chatMsgService.callKit(chatVo);
        return AjaxResult.success(data);
    }

}
