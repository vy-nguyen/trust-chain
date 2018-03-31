/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(
    entityManagerFactoryRef = "vntdEntityMgrFactory",
    transactionManagerRef = "vntdTransMgr",
    basePackages = {
        "com.tvntd.trustchain.trans.dao"
    }
)
public class PersistenceJPAConfig
{
    @Bean(name = "vntdDataSource")
    @ConfigurationProperties(prefix = "vntd.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean vntdEntityMgrFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("vntdDataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource)
            .packages("com.tvntd.trustchain.trans.models")
            .persistenceUnit("account")
            .build();
    }

    @Bean
    public PlatformTransactionManager vntdTransMgr(
            @Qualifier("vntdEntityMgrFactory") EntityManagerFactory entityMgrFactory)
    {
        return new JpaTransactionManager(entityMgrFactory);
    }
}
