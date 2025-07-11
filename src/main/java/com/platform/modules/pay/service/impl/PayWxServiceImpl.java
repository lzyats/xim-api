package com.platform.modules.pay.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.platform.common.exception.BaseException;
import com.platform.modules.pay.config.PayWxConfig;
import com.platform.modules.pay.service.PayWxService;
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
 * 微信支付 服务层实现
 * </p>
 */
@Slf4j
@Service("payWxService")
public class PayWxServiceImpl implements PayWxService {

    @Autowired
    private PayWxConfig payWxConfig;

    @Override
    public String createOrder(PayVo payVo, String notifyUrl) {
        try {
            Map<String, String> params = UnifiedOrderModel.builder()
                    .appid(payWxConfig.getAppId())
                    .mch_id(payWxConfig.getMchId())
                    .out_trade_no(payVo.getTradeNo())
                    .total_fee(yuanToFen(payVo.getAmount()))
                    .body(payVo.getBody())
                    .spbill_create_ip("127.0.0.1")
                    .notify_url(payWxConfig.getNotifyUrl() + notifyUrl)
                    .trade_type(TradeType.APP.getTradeType())
                    .nonce_str(WxPayKit.generateStr())
                    .build()
                    .createSign(payWxConfig.getPartnerKey(), SignType.HMACSHA256);
            String xmlResult = WxPayApi.pushOrder(false, params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            String returnMsg = result.get("return_msg");
            if (!WxPayKit.codeIsOk(result.get("return_code"))) {
                throw new BaseException(returnMsg);
            }
            if (!WxPayKit.codeIsOk(result.get("result_code"))) {
                throw new BaseException(returnMsg);
            }
            // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
            String prepayId = result.get("prepay_id");
            Map<String, String> sign = WxPayKit.appPrepayIdCreateSign(
                    payWxConfig.getAppId(),
                    payWxConfig.getMchId(),
                    prepayId,
                    payWxConfig.getPartnerKey(),
                    SignType.HMACSHA256
            );
            sign.put("universalLink", payWxConfig.getUniversalLink());
            return JSONUtil.toJsonStr(sign);
        } catch (Exception e) {
            log.error("wxPay:", e);
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public NotifyVo notify(HttpServletRequest request) {
        // 发送通知
        String resultLabel = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
        NotifyVo notifyVo = new NotifyVo(resultLabel);
        String xmlMsg = HttpKit.readData(request);
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        // Console.log(params.get("appid"));
        String returnCode = params.get("return_code");
        String resultCode = params.get("result_code");
        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, payWxConfig.getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                String tradeNo = params.get("out_trade_no");
                String orderNo = params.get("transaction_id");
                String amount = params.get("total_fee");
                notifyVo = new NotifyVo()
                        .setTradeNo(tradeNo)
                        .setOrderNo(orderNo)
                        .setAmount(fenToYuan(amount))
                        .setResult(true)
                        .setResultLabel(resultLabel);
            }
        }
        return notifyVo;
    }

    /**
     * 元转分
     */
    private static String yuanToFen(BigDecimal amount) {
        return NumberUtil.toStr(NumberUtil.mul(amount, 100).intValue());
    }

    /**
     * 分转元
     */
    private static BigDecimal fenToYuan(String amount) {
        return NumberUtil.div(amount, "100").setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

}
