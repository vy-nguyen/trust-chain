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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
/*
@Import({
    EthereumConfig.class
})
*/
public class Application
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext ctx =
            SpringApplication.run(new Object[]{Application.class}, args);

    }
}
