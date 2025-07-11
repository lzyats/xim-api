package com.platform.modules.pay.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.alipay.AliPayApi;
import com.platform.common.exception.BaseException;
import com.platform.modules.pay.config.PayAliConfig;
import com.platform.modules.pay.service.PayAliService;
import com.platform.modules.pay.vo.NotifyVo;
import com.platform.modules.pay.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 阿里支付 服务层实现
 * </p>
 */
@Slf4j
@Service("payAliService")
public class PayAliServiceImpl implements PayAliService {

    @Autowired
    private PayAliConfig payAliConfig;

    @Override
    public String createOrder(PayVo payVo, String notifyUrl) {
        BigDecimal amount = payVo.getAmount();
        try {
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setSubject(payVo.getBody());
            model.setBody(payVo.getBody());
            model.setOutTradeNo(payVo.getTradeNo());
            model.setTotalAmount(NumberUtil.toStr(amount));
            model.setProductCode("QUICK_MSECURITY_PAY");
            model.setTimeoutExpress("30s");
            return AliPayApi.appPayToResponse(model, payAliConfig.getNotifyUrl() + notifyUrl).getBody();
        } catch (AlipayApiException e) {
            log.error("appPay:", e);
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public NotifyVo notify(HttpServletRequest request) {
        NotifyVo notifyVo = new NotifyVo("success");
        try {
            Map<String, String> params = AliPayApi.toMap(request);
            // Console.log(params.get("auth_app_id"));
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, payAliConfig.getAliPayPublicKey(), "UTF-8", "RSA2");
            if (verifyResult && "TRADE_SUCCESS".equals(params.get("trade_status"))) {
                String tradeNo = params.get("out_trade_no");
                String orderNo = params.get("trade_no");
                String totalAmount = params.get("total_amount");
                BigDecimal amount = NumberUtil.toBigDecimal(totalAmount);
                notifyVo = new NotifyVo()
                        .setTradeNo(tradeNo)
                        .setOrderNo(orderNo)
                        .setAmount(amount)
                        .setResult(true)
                        .setResultLabel("success");
            } else {
                log.error("支付签名校验失败");
            }
        } catch (Exception e) {
            log.error("支付回调校验失败");
        }
        return notifyVo;
    }

}
