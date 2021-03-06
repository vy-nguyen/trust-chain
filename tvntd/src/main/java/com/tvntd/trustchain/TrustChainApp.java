/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tvntd.trustchain.config.EthereumConfig;

@SpringBootApplication
@EnableScheduling
public class TrustChainApp
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(new Object[] {
            TrustChainApp.class,
            EthereumConfig.class
        }, args);
    }
}
