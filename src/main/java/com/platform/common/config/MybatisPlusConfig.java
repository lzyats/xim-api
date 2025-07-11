package com.platform.common.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private RedisUtils redisUtils;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        // 初始化
        Long redisValue = redisUtils.increment(AppConstants.REDIS_CHAT_SNOWFLAKE, 1, 9999, TimeUnit.DAYS);
        if (redisValue == null || redisValue < 0L) {
            throw new RuntimeException(String.format("程序启动失败, 使用redis获取到的雪花算法索引值异常, %s", redisValue));
        }
        //从redis索引值中取出dataCenterId、workerId
        MutablePair<Long, Long> longLongMutablePair = generateIndex(redisValue);
        Long dataCenterId = longLongMutablePair.getLeft();
        Long workerId = longLongMutablePair.getRight();
        //再次检查dataCenterId、workerId值
        if (dataCenterId == null || dataCenterId < 0L || dataCenterId > 31L
                || workerId == null || workerId < 0L || workerId > 31L
        ) {
            throw new RuntimeException(String.format("程序启动失败, 使用雪花算法索引值异常, %s, %s, %s", redisValue, dataCenterId, workerId));
        }
        return new DefaultIdentifierGenerator(workerId, dataCenterId);
    }

    /**
     * 从index值中生成雪花算法的datacenterId(5bit)、workerId(5bit)
     *
     * @param index 索引值，如：1。必须为大于等于0的值
     * @return 返回生成好的用MutablePair包装的datacenterId(left)、workerId(right)，其中datacenterId值范围为0~31，workerId值范围也为0~31，如：{0,0}、{1、1}、{0,1}、{0,31}、{31,31}
     * @throws IllegalArgumentException 当参数索引值小于0时会抛出异常
     */
    private static MutablePair<Long, Long> generateIndex(long index) {
        if (index < 0) {
            throw new IllegalArgumentException("索引值不能为负数");
        }
        long indexBucket = index % 1024L;
        long dataCenterId = indexBucket / 32L;
        long workerId = indexBucket % 32L;
        return MutablePair.of(dataCenterId, workerId);
    }

    /**
     * 批量操作增强
     */
    @Bean
    public DefaultSqlInjector mybatisSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
                //防止父类方法不可用
                List<AbstractMethod> methods = super.getMethodList(mapperClass);
                methods.add(new InsertBatchSomeColumn());
                return methods;
            }
        };
    }

}
