/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.service;

import java.util.List;

import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tvntd.trustchain.dbase.access.IAccountKey;
import com.tvntd.trustchain.dbase.dao.AccountKeyRepo;
import com.tvntd.trustchain.dbase.models.AccountKey;
import com.tvntd.trustchain.trans.dao.TransactionRepo;
import com.tvntd.trustchain.trans.models.Transaction;

import static com.ethercamp.harmony.jsonrpc.TypeConverter.toJsonHex;

@Service
@Transactional
public class AccountKeySvc implements IAccountKey
{
    @Autowired
    protected AccountKeyRepo keyRepo;

    @Autowired
    protected TransactionRepo transRepo;

    @Override
    public AccountKey getAccount(String account)
    {
        return null;
    }

    @Override
    public AccountKey getAccountByOwner(String ownerUuid)
    {
        return null;
    }

    @Override
    public List<AccountKey> getAllAccountsByOwner(String ownerUuid)
    {
        return null;
    }

    @Override
    public List<AccountKey> getAccountByOwners(List<String> ownerUuids)
    {
        return null;
    }

    @Override
    public void saveAccount(String account, String privKey, String ownerUuid)
    {
        ECKey key = ECKey.fromPrivate(Hex.decode(privKey));
        byte[] pub = key.getAddress();
        String verif = toJsonHex(pub);

        System.out.println("Save account " + account + " uuid " + ownerUuid +
                ", verify " + verif);
        keyRepo.save(new AccountKey(account, ownerUuid, privKey));
        transRepo.save(new Transaction(ownerUuid, account,
                    account, 1000L, privKey, 1000L));
    }
}
