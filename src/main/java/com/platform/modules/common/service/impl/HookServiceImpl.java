package com.platform.modules.common.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.common.service.HookService;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.domain.WalletRecharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("hookService")
public class HookServiceImpl implements HookService {

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private PushService pushService;

    @Resource
    private ChatConfigService chatConfigService;

    /**
     * 计数器
     */
    private void doHandle(PushAuditEnum pushAudit) {
        // 存储
        Long count = redisUtils.hIncrement(AppConstants.REDIS_CHAT_ADMIN, pushAudit.getCode(), 1);
        // 推送1
        pushService.pushAudit(pushAudit);
        // 推送2
        this.sendHook(StrUtil.format("# 【{}】提醒：\n\n" +
                        "需要处理：<font color=\"warning\">{}例</font>\n\n" +
                        "推送时间：<font color=\"warning\">{}</font>"
                , pushAudit.getInfo()
                , count < 1 ? 1 : count
                , DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)));
    }

    @Override
    public void handle(PushAuditEnum pushAudit) {
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_SPECIAL, pushAudit.getType());
        switch (pushAudit) {
            case APPLY_AUTH:
            case APPLY_CASH:
            case INFORM_USER:
            case INFORM_GROUP:
            case USER_FEEDBACK:
                this.doHandle(pushAudit);
                break;
            case APPLY_BANNED:
            case APPLY_SPECIAL:
                Long count = redisUtils.sAdd(redisKey, NumberUtil.toStr(ShiroUtils.getUserId()), 90, TimeUnit.DAYS);
                if (count.intValue() == 1) {
                    this.doHandle(pushAudit);
                }
                break;
            case USER_REGISTER:
                // 计数
                redisUtils.sAdd(redisKey, IdWorker.getIdStr(), 7, TimeUnit.DAYS);
                // 续期
                redisUtils.expire(redisKey, DateUtil.endOfDay(DateUtil.date()));
                // 推送
                this.sendHook(StrUtil.format("# 【{}】提醒：\n\n" +
                                "今日新增：<font color=\"warning\">{}例</font>\n\n" +
                                "推送时间：<font color=\"warning\">{}</font>"
                        , pushAudit.getInfo()
                        , redisUtils.sSize(redisKey)
                        , DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)));
                break;
        }
    }

    @Override
    public void handle(WalletRecharge recharge) {
        // 发送消息
        this.sendHook(StrUtil.format("# 【{}】提醒：\n\n" +
                        "支付方式：<font color=\"warning\">{}</font>\n\n" +
                        "支付金额：<font color=\"warning\">{}元</font>\n\n" +
                        "推送时间：<font color=\"warning\">{}</font>"
                , PushAuditEnum.USER_RECHARGE.getInfo()
                , recharge.getPayType().getInfo()
                , recharge.getAmount()
                , DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)));
    }

    @Override
    public void doTask(Long userId) {
        // 解封
        Long count = redisUtils.hIncrement(AppConstants.REDIS_CHAT_ADMIN, PushAuditEnum.APPLY_BANNED.getCode(), -1);
        if (count > 0) {
            pushService.pushAudit(PushAuditEnum.APPLY_BANNED);
        }
        // 解除
        String redisValue = NumberUtil.toStr(userId);
        redisUtils.sRemove(StrUtil.format(AppConstants.REDIS_CHAT_SPECIAL, PushAuditEnum.APPLY_BANNED.getType()), redisValue);
        redisUtils.sRemove(StrUtil.format(AppConstants.REDIS_CHAT_SPECIAL, PushAuditEnum.APPLY_SPECIAL.getType()), redisValue);
    }

    /**
     * 发送hook
     */
    private void sendHook(String message) {
        String content = chatConfigService.queryConfig(ChatConfigEnum.SYS_HOOK).getStr();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        JSONObject jsonObject = new JSONObject()
                .set("msgtype", "markdown")
                .set("markdown", new JSONObject().set("content", message));
        String regex = "((http|https)://)([\\w\\-]+\\.)*[\\w\\-]+(:[0-9]+)?((/[\\w-]*)*(\\?[\\w-%:;&@#+=]*)?)";
        ThreadUtil.execAsync(() -> {
            List<String> dataList = ReUtil.findAllGroup0(regex, content);
            dataList.forEach(data -> {
                String result = HttpUtil.post(data, JSONUtil.toJsonStr(jsonObject));
                Console.log(result);
            });
        });
    }
}
