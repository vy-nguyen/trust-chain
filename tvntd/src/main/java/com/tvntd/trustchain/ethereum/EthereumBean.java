/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.ethereum;

import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;


public class EthereumBean
{
    Ethereum ethereum;

    public void start()
    {
        this.ethereum = EthereumFactory.createEthereum();
        this.ethereum.addListener(new EthereumListener(ethereum));
        System.out.println(">>> start ether " + this.ethereum);
    }

    public String getBestBlock()
    {
        return "" + ethereum.getBlockchain().getBestBlock().getNumber();
    }
}
