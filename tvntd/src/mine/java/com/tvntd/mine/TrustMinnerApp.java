/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.mine;

import org.ethereum.facade.EthereumFactory;
import org.springframework.context.annotation.Bean;

import com.tvntd.mine.config.MinerApp;

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
        EthereumFactory.createEthereum(Miner.class);
    }
}
