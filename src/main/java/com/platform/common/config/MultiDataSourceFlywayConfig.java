package com.platform.common.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiDataSourceFlywayConfig {

    // 主库数据源配置（从 ShardingSphere 的 master 数据源中获取）
    @Value("${spring.shardingsphere.datasource.master.jdbc-url}")
    private String masterJdbcUrl;
    @Value("${spring.shardingsphere.datasource.master.username}")
    private String masterUsername;
    @Value("${spring.shardingsphere.datasource.master.password}")
    private String masterPassword;

    // 从库数据源配置（从 ShardingSphere 的 slave 数据源中获取）
    @Value("${spring.shardingsphere.datasource.secondary.jdbc-url}")
    private String slaveJdbcUrl;
    @Value("${spring.shardingsphere.datasource.secondary.username}")
    private String slaveUsername;
    @Value("${spring.shardingsphere.datasource.secondary.password}")
    private String slavePassword;

    // 主库 Flyway 实例（对应 flyway 目录脚本）
    @Bean
    public Flyway masterFlyway(
            @Value("${spring.flyway.locations}") String masterLocations,
            @Value("${spring.flyway.baseline-on-migrate}") boolean masterBaseline,
            @Value("${spring.flyway.clean-disabled}") boolean masterCleanDisabled) {
        Flyway flyway = Flyway.configure()
                .dataSource(masterJdbcUrl, masterUsername, masterPassword)  // 绑定主库数据源
                .locations(masterLocations)  // 主库脚本路径：classpath:flyway
                .baselineOnMigrate(masterBaseline)
                .cleanDisabled(masterCleanDisabled)
                .load();
        flyway.migrate();  // 执行主库迁移
        return flyway;
    }

    // 从库 Flyway 实例（对应 flywayb 目录脚本）
    @Bean
    public Flyway slaveFlyway(
            @Value("${spring.flyways.locations}") String slaveLocations,
            @Value("${spring.flyways.baseline-on-migrate}") boolean slaveBaseline,
            @Value("${spring.flyways.clean-disabled}") boolean slaveCleanDisabled) {
        Flyway flyway = Flyway.configure()
                .dataSource(slaveJdbcUrl, slaveUsername, slavePassword)  // 绑定从库数据源
                .locations(slaveLocations)  // 从库脚本路径：classpath:flywayb
                .baselineOnMigrate(slaveBaseline)
                .cleanDisabled(slaveCleanDisabled)
                .load();
        flyway.migrate();  // 执行从库迁移
        return flyway;
    }
}