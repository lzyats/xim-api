package com.platform.modules.pay.service;

import com.platform.modules.pay.vo.NotifyVo;
import com.platform.modules.pay.vo.PayVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 微信支付 服务层
 * </p>
 */
public interface PayWxService {

    /**
     * 生成订单
     */
    String createOrder(PayVo payVo, String notifyUrl);

    /**
     * 支付回调
     */
    NotifyVo notify(HttpServletRequest request);

}
