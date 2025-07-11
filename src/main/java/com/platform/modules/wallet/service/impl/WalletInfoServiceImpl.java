package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.common.service.MessageService;
import com.platform.modules.wallet.dao.WalletInfoDao;
import com.platform.modules.wallet.domain.WalletInfo;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.vo.WalletVo05;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 钱包 服务层实现
 * </p>
 */
@Slf4j
@Service("walletInfoService")
public class WalletInfoServiceImpl extends BaseServiceImpl<WalletInfo> implements WalletInfoService {

    @Resource
    private WalletInfoDao walletInfoDao;

    @Resource
    private MessageService messageService;

    @Resource
    private ChatUserService chatUserService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletInfoDao);
    }

    @Override
    public List<WalletInfo> queryList(WalletInfo t) {
        List<WalletInfo> dataList = walletInfoDao.queryList(t);
        return dataList;
    }

    @Override
    public void addWallet(Long userId) {
        String salt = CodeUtils.salt();
        String pass = RandomUtil.randomNumbers(6);
        String password = CodeUtils.md5(pass);
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setBalance(BigDecimal.ZERO)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(password, salt));
        this.add(wallet);
    }

    @Override
    public BigDecimal getInfo(Long userId) {
        WalletInfo wallet = getById(userId);
        return wallet.getBalance();
    }

    @Transactional
    @Override
    public void setPass(WalletVo05 walletVo) {
        String phone = ShiroUtils.getPhone();
        // 验证
        messageService.verifySms(phone, walletVo.getCode(), MessageTypeEnum.WALLET);
        // 设置密码
        resetPass(walletVo.getPassword());
        // 设置支付
        chatUserService.setPayment();
    }

    /**
     * 设置密码
     */
    private void resetPass(String password) {
        Long current = ShiroUtils.getUserId();
        String salt = CodeUtils.salt();
        WalletInfo wallet = new WalletInfo()
                .setUserId(current)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(password, salt));
        this.updateById(wallet);
        // 验证次数
        redisUtils.delete(StrUtil.format(AppConstants.REDIS_CHAT_WALLET, DateUtil.dayOfMonth(DateUtil.date()), current));
    }

    @Override
    public BigDecimal addBalance(Long userId, BigDecimal amount) {
        // 查询数据库
        WalletInfo entity = this.getById(userId);
        if (AppConstants.WALLET_BALANCE.compareTo(entity.getBalance()) == 0) {
            throw new BaseException(StrUtil.format("账户余额不能超过{}", AppConstants.WALLET_BALANCE));
        }
        BigDecimal balance = NumberUtil.add(entity.getBalance(), amount);
        // 更新余额
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setBalance(balance)
                .setVersion(entity.getVersion());
        if (this.updateById(wallet) == 0) {
            throw new BaseException("余额增加失败，请稍后再试");
        }
        return balance;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public BigDecimal addTransactional(Long userId, BigDecimal amount) {
        return this.addBalance(userId, amount);
    }

    @Override
    public BigDecimal subtractBalance(Long userId, BigDecimal amount, String password) {
        // 验证次数
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_WALLET, DateUtil.dayOfMonth(DateUtil.date()), userId);
        Long count = redisUtils.increment(redisKey, 0, 1, TimeUnit.DAYS);
        if (count >
                2) {
            throw new BaseException("支付密码错误超过3次，请明日再试");
        }
        // 查询钱包
        WalletInfo walletInfo = this.getById(userId);
        // 密码错误
        if (!walletInfo.getPassword().equalsIgnoreCase(CodeUtils.credentials(password, walletInfo.getSalt()))) {
            redisUtils.increment(redisKey, 1, 1, TimeUnit.DAYS);
            throw new BaseException("支付密码验证失败");
        }
        redisUtils.delete(redisKey);
        // 余额不足
        if (walletInfo.getBalance().compareTo(amount) < 0) {
            throw new BaseException("账户余额不足");
        }
        BigDecimal balance = NumberUtil.sub(walletInfo.getBalance(), amount);
        // 扣减余额
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setBalance(balance)
                .setVersion(walletInfo.getVersion());
        if (this.updateById(wallet) == 0) {
            throw new BaseException("余额扣减失败，请稍后再试");
        }
        return balance;
    }


}
