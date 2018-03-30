/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({
    "classpath:persistence.properties"
})
@ComponentScan({
    "com.tvntd.trustchain.dbase.models"
})
@EnableJpaRepositories(
    basePackages = "com.tvntd.trustchain.dbase.dao",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
public class PersistenceJPAConfig
{
    @Autowired
    private Environment env;

    public PersistenceJPAConfig() {
        super();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean em =
            new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter =
            new HibernateJpaVendorAdapter();

        em.setJpaVendorAdapter(vendorAdapter);
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] {
            "com.tvntd.trustchain.dbase.models"
        });
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    @Primary
    public DataSource dataSource()
    {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        dataSource.
        return dataSource;
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager()
    {
        final JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory().getObject());
        return tm;
    }

    @Bean
    @Primary
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties additionalProperties()
    {
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.connection.CharSet", "utf-8");
        props.put("hibernate.connection.useUnicode", true);
        props.put("hibernate.connection.characterEncoding", "utf-8");

        return props;
    }
}
