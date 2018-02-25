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

import com.tvntd.trustchain.ethereum.EthereumBean;

import java.util.concurrent.Executors;

// @Configuration
public class Config
{
    // @Bean
    EthereumBean ethereumBean() throws Exception
    {
        EthereumBean ethereumBean = new EthereumBean();
        Executors.newSingleThreadExecutor().submit(ethereumBean::start);

        System.out.println("Create ethereum bean " + ethereumBean);
        return ethereumBean;
    }
}
