package com.platform.modules.sys.service.impl;

import javax.annotation.Resource;

import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.Collections;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.service.SysHtmlService;
import com.platform.modules.sys.dao.SysHtmlDao;
import com.platform.modules.sys.domain.SysHtml;

/**
 * <p>
 * APP网页定制 服务层实现
 * </p>
 */
@Slf4j
@Service("sysHtmlService")
public class SysHtmlServiceImpl extends BaseServiceImpl<SysHtml> implements SysHtmlService {

    @Resource
    private SysHtmlDao sysHtmlDao;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    String redisKey = AppConstants.REDIS_COMMON_SYS + ":html";
    Long CACHE_TIMEOUT=300L;
    TimeUnit CACHE_TIME_UNIT=TimeUnit.SECONDS;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysHtmlDao);
    }


    @Override
    public List<SysHtml> queryList() {
        Map<String, SysHtml> cacheSysHtmlMap = null;
        try {
            // 尝试查询缓存（捕获Redis相关异常）
            cacheSysHtmlMap = redisJsonUtil.hgetAll(redisKey, SysHtml.class);
        } catch (Exception e) {
            // 日志记录：Redis查询失败，降级查库
            log.error("读取缓存失败", e);
        }

        // 缓存命中（或Redis异常时跳过缓存）
        if (cacheSysHtmlMap != null && !cacheSysHtmlMap.isEmpty()) {
            return cacheSysHtmlMap.values().stream().collect(Collectors.toList());
        }

        // 查库逻辑（不变）
        List<SysHtml> dataList = sysHtmlDao.queryList();

        // 写入缓存（同样捕获异常，避免缓存写入失败影响主流程）
        try {
            if (dataList == null || dataList.isEmpty()) {
                // 写入空的不可变Map，避免缓存穿透
                redisJsonUtil.hmset(redisKey, Collections.emptyMap(), CACHE_TIMEOUT, CACHE_TIME_UNIT);
                return dataList;
            } else {
                Map<String, SysHtml> sysHtmlMap = dataList.stream()
                        .collect(Collectors.toMap(
                                sysHtml -> String.valueOf(sysHtml.getRoulekey()),
                                sysHtml -> sysHtml
                        ));
                redisJsonUtil.hmset(redisKey, sysHtmlMap, CACHE_TIMEOUT, CACHE_TIME_UNIT);
            }
        } catch (Exception e) {
            log.error("写入缓存失败", e);
        }
        return dataList;
    }
    @Override
    public SysHtml getInfo(String roulekey) {
        // 先获取所有记录
        List<SysHtml> sysHtmls = this.queryList();

        // 从列表中筛选出roulekey匹配的记录
        return sysHtmls.stream()
                // 过滤出与目标roulekey相等的记录（注意处理null情况）
                .filter(sysHtml -> roulekey.equals(sysHtml.getRoulekey()))
                // 获取第一个匹配的结果，无匹配则返回null
                .findFirst()
                .orElse(null);
    }
}
