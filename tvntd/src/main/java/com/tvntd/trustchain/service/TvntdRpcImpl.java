/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import java.util.UUID;

import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.Repository;
import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.tvntd.trustchain.dbase.access.IAccountKey;
import com.tvntd.trustchain.rpc.TvntdRpc;

@Service
@AutoJsonRpcServiceImpl
public class TvntdRpcImpl implements TvntdRpc
{
    @Autowired
    protected Ethereum ethereum;

    @Autowired
    IAccountKey acctKey;

    @Override
    public VerifyOwner tvntd_verify(String name)
    {
        VerifyOwner ver = new VerifyOwner();

        ver.owner = name;
        ver.userUuid = UUID.randomUUID().toString();
        System.out.println("Invoke verify..." + name);
        return ver;
    }

    @Override
    public String tvntd_saveKey(String key)
    {
        acctKey.saveAccount(key, "abc123", UUID.randomUUID().toString());
        return "ok";
    }

    @Override
    public String tvntd_trans(String send, String recv, int amount)
    {
        ECKey sendKey = ECKey.fromPrivate(Hex.decode(send));
        byte[] recvAddr = Hex.decode(recv);

        Repository repo = ethereum.getRepository();
        int val = repo.getNonce(sendKey.getAddress()).intValue();

        Transaction tx = new Transaction(ByteUtil.intToBytesNoLeadZeroes(val),
                ByteUtil.longToBytesNoLeadZeroes(50_000_000_000L),
                ByteUtil.longToBytesNoLeadZeroes(0xfffff),
                recvAddr, new byte[]{100}, new byte[0],
                ethereum.getChainIdForNextBlock());

        tx.sign(sendKey);
        ethereum.submitTransaction(tx);

        System.out.println("Submit tx " + tx);
        return "Hello";
    }
}
