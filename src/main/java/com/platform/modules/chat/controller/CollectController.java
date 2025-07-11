package com.platform.modules.chat.controller;

import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.domain.ChatUserCollect;
import com.platform.modules.chat.service.ChatUserCollectService;
import com.platform.modules.chat.vo.CollectVo01;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 收藏
 */
@RestController
@Slf4j
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Resource
    private ChatUserCollectService chatUserCollectService;

    /**
     * 列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/list")
    public TableDataInfo list(ChatUserCollect collect) {
        startPage("collectId desc");
        PageInfo pageInfo = chatUserCollectService.queryDataList(collect);
        return getDataTable(pageInfo);
    }

    /**
     * 增加
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody CollectVo01 collectVo) {
        switch (collectVo.getMsgType()) {
            case TEXT:
            case IMAGE:
            case VIDEO:
            case FILE:
            case LOCATION:
            case CARD:
                break;
            default:
                throw new BaseException("不支持的消息类型");
        }
        chatUserCollectService.addCollect(collectVo);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/remove/{collectId}")
    public AjaxResult remove(@PathVariable Long collectId) {
        chatUserCollectService.deleteById(collectId);
        return AjaxResult.success();
    }

}
