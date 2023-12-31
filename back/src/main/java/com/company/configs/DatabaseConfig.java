package com.company.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean("DB-Snaper")
    @ConfigurationProperties(prefix = "spring.datasource.snaper")
    public DataSource productDb() {
        return DataSourceBuilder.create().build();
    }

}
