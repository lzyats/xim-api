package com.platform.modules.chat.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.utils.ServletUtils;
import com.platform.common.utils.VersionUtils;
import com.platform.common.web.filter.version.VersionConfig;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVersionDao;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.service.ChatVersionService;
import com.platform.modules.common.vo.CommonVo05;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 版本 服务层实现
 * </p>
 */
@Service("chatVersionService")
public class ChatVersionServiceImpl extends BaseServiceImpl<ChatVersion> implements ChatVersionService {

    // 初始化日志对象（注意：这里应使用当前类的字节码对象）
    private static final Logger logger = LoggerFactory.getLogger(ChatVersionServiceImpl.class);

    @Resource
    private ChatVersionDao chatVersionDao;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatVersionDao);
    }

    @Override
    public List<ChatVersion> queryList(ChatVersion t) {
        // 保持原有实现不变
        List<ChatVersion> dataList = chatVersionDao.queryList(t);
        return dataList;
    }

    @Override
    public CommonVo05 upgrade() {
        HttpServletRequest request = ServletUtils.getRequest();
        // 设备
        String device = request.getHeader(HeadConstant.DEVICE);
        // 版本
        String version = request.getHeader(HeadConstant.VERSION);
        String redisKey = AppConstants.REDIS_COMMON_CONFIG + "version:upgrade:" + device ;

        try {
            // 1. 先查询Redis缓存
            CommonVo05 cachedResult = redisJsonUtil.get(redisKey, CommonVo05.class);
            if (cachedResult != null) {
                logger.info("从Redis缓存获取版本升级信息成功，key: {}", redisKey);
                return cachedResult;
            }
        } catch (Exception e) {
            // 缓存查询异常不中断流程，仅记录日志
            logger.error("Redis缓存查询版本升级信息异常，key: {}", redisKey, e);
        }

        // 2. 缓存不存在或异常，查询数据库并构建结果
        ChatVersion chatVersion = this.queryOne(new ChatVersion().setDevice(device));
        String lowest = VersionConfig.LOWEST;
        CommonVo05 result = new CommonVo05()
                .setVersion(chatVersion != null ? chatVersion.getVersion() : null)
                .setUrl(chatVersion != null ? chatVersion.getUrl() : null)
                .setContent(chatVersion != null ? chatVersion.getContent() : null)
                .setUpgrade(YesOrNoEnum.transform(chatVersion != null && VersionUtils.compareTo(version, chatVersion.getVersion()) < 0))
                .setForce(YesOrNoEnum.transform(VersionUtils.compareTo(version, lowest) < 0));

        // 3. 将结果存入Redis缓存（设置过期时间，如5分钟）
        try {
            redisJsonUtil.set(redisKey, result, 300L, TimeUnit.SECONDS);
            logger.info("版本升级信息写入Redis缓存成功，key: {}", redisKey);
        } catch (Exception e) {
            // 缓存写入失败不影响返回结果
            logger.error("Redis缓存写入版本升级信息异常，key: {}", redisKey, e);
        }

        return result;
    }
}