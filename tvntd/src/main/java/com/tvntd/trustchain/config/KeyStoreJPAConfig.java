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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "keyStoreEntityMgrFactory",
    transactionManagerRef = "keyStoreTransMgr",
    basePackages = {
        "com.tvntd.trustchain.dbase.dao"
    }
)
public class KeyStoreJPAConfig
{
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "keyStoreEntityMgrFactory")
    public LocalContainerEntityManagerFactoryBean keyStoreEntityMgrFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource)
    {
        return builder.dataSource(dataSource)
            .packages("com.tvntd.trustchain.dbase.models")
            .persistenceUnit("keystore")
            .build();
    }

    @Primary
    @Bean(name = "keyStoreTransMgr")
    public PlatformTransactionManager keyStoreTransMgr(
            @Qualifier("keyStoreEntityMgrFactory") EntityManagerFactory entityMgrFactory)
    {
        return new JpaTransactionManager(entityMgrFactory);
    }
}
