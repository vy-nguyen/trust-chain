/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.mine.config;

import org.ethereum.core.Block;
import org.ethereum.mine.MinerListener;

import com.tvntd.trustchain.plugin.BaseApp;

public class MinerApp extends BaseApp implements MinerListener
{
    public MinerApp()
    {
        super();
        m_config =
            "peer.networkId = 1973\n" +
            "genesis = private-genesis.json\n" +
            "cache.flush.blocks = 1\n" +
            "mine {\n" +
            "   start = true\n" +
            "   extraDataHex = 'cdcdcdcdcdcd'\n" +
            "   cpuMineThreads = 2\n" +
            "}\n";
    }

    @Override
    public void run()
    {
        System.out.println("Run minner app...");
    }

    @Override
    public void miningStarted()
    {
        System.out.println("Started minner app...");
    }

    @Override
    public void miningStopped()
    {
        System.out.println("Stop minner app...");
    }

    @Override
    public void blockMiningStarted(Block block)
    {
        System.out.println("Start mining block minner app...");
    }

    @Override
    public void blockMined(Block block)
    {
        System.out.println("Block mined " + block);
    }

    @Override
    public void blockMiningCanceled(Block block)
    {
    }
}
