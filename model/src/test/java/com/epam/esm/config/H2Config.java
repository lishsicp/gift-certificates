package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:h2.config.properties")
@Profile("test")
public class H2Config {

    @Bean
    public HikariConfig hikariConfig(
            @Value("${h2.datasource.url}") String url,
            @Value("${h2.datasource.username}") String username,
            @Value("${h2.datasource.password}") String password,
            @Value("${h2.datasource.driver}") String driver,
            @Value("${h2.datasource.maximumPoolSize}") int maxPoolSize,
            @Value("${h2.datasource.idleTimeout}") long idleTimeOut
    ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setIdleTimeout(idleTimeOut);
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource(HikariConfig config) {
        HikariDataSource dataSource = new HikariDataSource(config);
        Resource initData = new ClassPathResource("schema.sql");
        Resource fillData = new ClassPathResource("test_data.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initData, fillData);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
