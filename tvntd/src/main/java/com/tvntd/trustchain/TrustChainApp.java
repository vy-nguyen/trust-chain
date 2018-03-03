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
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tvntd.trustchain.config.EthereumConfig;
import com.tvntd.trustchain.plugin.BaseApp;

@SpringBootApplication
@EnableScheduling
@Import({
    EthereumConfig.class
})
public class TrustChainApp extends BaseApp
{
    public TrustChainApp()
    {
        m_config =
            "trust {\n" +
            "   block = 1024\n" +
            "   uri = 'http://abc.com/abc/def'\n" +
            "}\n" +
            "peer {" +
            "   listen.port = 30301" +
            "}";
    }

    public static void main(String[] args) throws Exception
    {
        s_log.info("Starting private Ethereum network");
        SpringApplication.run(new Object[] {
            TrustChainApp.class
        }, args);
    }
}
