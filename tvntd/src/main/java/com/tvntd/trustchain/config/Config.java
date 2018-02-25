/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;

@Configuration
public class Config
{
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static AutoJsonRpcServiceImplExporter exporter()
    {
        AutoJsonRpcServiceImplExporter jsonRpc = new AutoJsonRpcServiceImplExporter();

        return jsonRpc;
    }
}
