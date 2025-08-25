package com.platform.modules.chat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.alibaba.fastjson.TypeReference;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisJsonUtil; // 替换RedisUtils为RedisJsonUtil
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatNoticeDao;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import com.platform.modules.common.vo.CommonVo04;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("chatNoticeService")
public class ChatNoticeServiceImpl extends BaseServiceImpl<ChatNotice> implements ChatNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

    @Resource
    private ChatNoticeDao chatNoticeDao;

    // 1. 替换RedisUtils为RedisJsonUtil（FastJSON封装工具）
    @Autowired
    private RedisJsonUtil redisJsonUtil;

    // 2. 删除Gson相关初始化代码（无需保留）

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatNoticeDao);
    }

    @Override
    public List<ChatNotice> queryList(ChatNotice t) {
        return chatNoticeDao.queryList(t);
    }

    @Override
    public PageInfo<CommonVo04> queryDataList() {
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        int pageNum=pageDomain.getPageNum();
        // 1. 定义分页参数（可根据业务调整pageSize）
        int pageSize = 10; // 每页显示10条数据
        // 缓存键按页码区分：原键 + 页码（如 "notice:list:1", "notice:list:2"）
        String redisKey = AppConstants.REDIS_COMMON_NOTIC + "list:" + pageNum;
        long cacheTimeout = 120;
        TimeUnit cacheTimeUnit = TimeUnit.SECONDS;

        try {
            // 2. 读取当前页码的缓存
            PageInfo<CommonVo04> cachedPageInfo = redisJsonUtil.get(
                    redisKey,
                    new TypeReference<PageInfo<CommonVo04>>() {}
            );

            if (cachedPageInfo != null) {
                logger.info("从Redis缓存获取Notice第{}页数据成功，key: {}", pageNum, redisKey);
                return cachedPageInfo;
            }
        } catch (Exception e) {
            logger.error("Redis缓存读取/解析失败，清除异常缓存，key: {}", redisKey, e);
            redisJsonUtil.delete(redisKey); // 清除当前页码的异常缓存
        }

        // 3. 数据库分页查询
        try {
            // 3.1 设置分页参数（使用MyBatis分页插件，需提前配置）
            PageHelper.startPage(pageNum, pageSize,"create_time desc");
            // 3.2 查询当前页数据（带分页条件）
            ChatNotice query = new ChatNotice().setStatus(YesOrNoEnum.YES);
            List<ChatNotice> dataList = queryList(query); // 数据库查询方法（自动分页）

            // 3.3 转换为VO
            List<CommonVo04> resultList = dataList.stream()
                    .filter(Objects::nonNull)
                    .map(CommonVo04::new)
                    .collect(Collectors.toList());

            // 3.4 构建分页信息（PageHelper自动填充总记录数等）
            PageInfo<CommonVo04> pageInfo = new PageInfo<>(resultList);
            // 补充分页元数据（PageHelper已自动处理，这里仅作明确展示）
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);

            // 4. 写入当前页码的缓存
            try {
                redisJsonUtil.set(redisKey, pageInfo, cacheTimeout, cacheTimeUnit);
                logger.info("写入Redis缓存成功，key: {}, 页码: {}, 过期时间: {}秒",
                        redisKey, pageNum, cacheTimeout);
            } catch (Exception e) {
                logger.error("写入Redis缓存失败，key: {}, 页码: {}", redisKey, pageNum, e);
            }

            return pageInfo;
        } finally {
            // 清除分页插件的线程变量（避免内存泄漏）
            PageHelper.clearPage();
        }
    }

}