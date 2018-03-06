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
            "peer.discovery.enabled = false\n" +
            "peer.listen.port = 30301\n" +
            // "peer.privatekey = \n" +
            "peer.networkId = 1973\n" +
            "sync.enabled = false\n" +
            "genesis = private-genesis.json\n" +
            "database.dir = '/Users/work/ws/data/mine/1'\n" +
            "cache.flush.blocks = 1\n" +
            "mine {\n" +
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
    }

    @Override
    public void blockMiningStarted(Block block)
    {
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
