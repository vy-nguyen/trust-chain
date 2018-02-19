/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import org.ethereum.config.CommonConfig;
import org.ethereum.config.NoAutoscan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackages = "org.ethereum",
    excludeFilters = @ComponentScan.Filter(NoAutoscan.class)
)
public class EthereumConfig extends CommonConfig
{
    public EthereumConfig() {
        super();
    }
}
