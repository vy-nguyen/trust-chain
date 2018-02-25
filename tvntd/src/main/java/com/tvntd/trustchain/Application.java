/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain;

import org.ethereum.facade.Ethereum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tvntd.trustchain.config.EthereumConfig;


@SpringBootApplication
@EnableScheduling
@Import({
    EthereumConfig.class
})
public class Application
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext ctx =
            SpringApplication.run(new Object[]{Application.class}, args);

        Ethereum eth = ctx.getBean(Ethereum.class);
        System.out.println("Eth bean " + eth);
    }
}
