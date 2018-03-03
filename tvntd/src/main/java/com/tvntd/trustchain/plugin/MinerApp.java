/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.plugin;

import org.ethereum.core.Block;
import org.ethereum.mine.MinerListener;

public class MinerApp extends BaseApp implements MinerListener
{
    public MinerApp() {
        super();
        m_config =
            "peer.discovery.enabled = false\n" +
            "peer.listen.port = 30300\n" +
            "peer.privatekey = \n" +
            "peer.networkId = 1973\n" +
            "sync.enabled = false\n";
    }

    @Override
    public void run()
    {
    }

    @Override
    public void miningStarted()
    {
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
    }

    @Override
    public void blockMiningCanceled(Block block)
    {
    }
}
