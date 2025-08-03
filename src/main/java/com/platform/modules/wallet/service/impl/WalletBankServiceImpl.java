package com.platform.modules.wallet.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatUserInfoService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.service.impl.ChatNoticeServiceImpl;
import com.platform.modules.wallet.dao.WalletBankDao;
import com.platform.modules.wallet.domain.WalletBank;
import com.platform.modules.wallet.service.WalletBankService;
import com.platform.modules.wallet.vo.TradeVo07;
import com.platform.modules.wallet.vo.TradeVo08;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 钱包卡包 服务层实现
 * </p>
 */
@Service("walletBankService")
public class WalletBankServiceImpl extends BaseServiceImpl<WalletBank> implements WalletBankService {

    @Resource
    private WalletBankDao walletBankDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private ChatConfigService chatConfigService;

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletBankDao);
    }

    @Override
    public List<WalletBank> queryList(WalletBank t) {
        List<WalletBank> dataList = walletBankDao.queryList(t);
        return dataList;
    }

    @Override
    public List<TradeVo07> queryDataList() {
        Long current = ShiroUtils.getUserId();
        logger.info("查询钱包列表，userId: {}", current);
        WalletBank query = new WalletBank().setUserId(current);
        List<WalletBank> dataList = queryList(query);
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(BeanUtil.toBean(y, TradeVo07.class));
        }, ArrayList::addAll);
    }

    @Override
    public void addBank(TradeVo08 tradeVo) {
        Long current = ShiroUtils.getUserId();
        String name = tradeVo.getName();
        // 校验实名
        verifyAuth(current, name);
        // 校验钱包
        //verifyWallet(tradeVo.getWallet());
        // 绑卡数
        Integer count = 1;
        // 查询
        if (this.queryCount(new WalletBank().setUserId(current)) >= count) {
            throw new BaseException(StrUtil.format("钱包数量不能超过{}张", count));
        }
        WalletBank bank = BeanUtil.toBean(tradeVo, WalletBank.class)
                .setUserId(current);
        this.add(bank);
    }

    /**
     * 校验认证
     */
    private void verifyAuth(Long userId, String name) {
        // 实名开关
        YesOrNoEnum auth = chatConfigService.queryConfig(ChatConfigEnum.WALLET_CASH_AUTH).getYesOrNo();
        if (YesOrNoEnum.NO.equals(auth)) {
            return;
        }
        // 查询实名
        ChatUser chatUser = chatUserService.getById(userId);
        if (!ApproveEnum.PASS.equals(chatUser.getAuth())) {
            throw new BaseException("请先进行实名认证");
        }
        // 校验姓名
        ChatUserInfo userInfo = chatUserInfoService.getById(userId);
        if (!name.equalsIgnoreCase(userInfo.getName())) {
            throw new BaseException("验证姓名与账号实名信息不符，绑定钱包地址失败");
        }
    }

    /**
     * 校验钱包
     */
    private void verifyWallet(String wallet) {
        // 手机号
        if (Validator.isMobile(wallet)) {
            return;
        }
        // 邮箱
        if (Validator.isEmail(wallet)) {
            return;
        }
        throw new BaseException("钱包账户不合法");
    }

    @Override
    public void deleteBank(Long bankId) {
        Long current = ShiroUtils.getUserId();
        WalletBank bank = getById(bankId);
        if (bank == null) {
            return;
        }
        if (!current.equals(bank.getUserId())) {
            return;
        }
        this.deleteById(bankId);
    }

}
