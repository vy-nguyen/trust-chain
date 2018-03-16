/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.mine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tvntd.mine.config.EthereumConfig;
import com.tvntd.mine.config.MinerApp;

@SpringBootApplication
@EnableScheduling
@Import({
    EthereumConfig.class
})
public class TrustMinnerApp
{
    static class Miner extends MinerApp
    {
        @Bean
        public Miner getMiner() {
            return new Miner();
        }
    }

    public static void main(String[] args) throws Exception
    {
        ConfigurableApplicationContext ctx =
            SpringApplication.run(new Object[] {
                TrustMinnerApp.class,
                Miner.class
            }, args);

    }
}
