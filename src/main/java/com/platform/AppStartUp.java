package com.platform;

import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.platform.modules.quartz.config.ScheduleConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * 启动程序
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class AppStartUp implements ApplicationRunner {

    public static void main(String[] args) {
        // 启动App
        SpringApplication.run(AppStartUp.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        // 定时任务开启
        CronUtil.setMatchSecond(true);
        CronUtil.start(true);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            ScheduleConfig scheduleConfig = SpringUtil.getBean(ScheduleConfig.class);
            scheduleConfig.init();
        };
    }

}
