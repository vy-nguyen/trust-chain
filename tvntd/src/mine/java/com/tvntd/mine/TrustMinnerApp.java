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
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tvntd.mine.config.MinerApp;

@SpringBootApplication
@EnableScheduling
public class TrustMinnerApp
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(new Object[] {
            TrustMinnerApp.class,
            MinerApp.class
        }, args);
    }
}
