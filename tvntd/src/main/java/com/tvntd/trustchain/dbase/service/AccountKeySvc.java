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

import com.tvntd.crypto.proto.KS;
import com.tvntd.trustchain.dbase.access.IAccountKey;
import com.tvntd.trustchain.dbase.dao.AccountKeyRepo;
import com.tvntd.trustchain.dbase.models.AccountKey;
import com.tvntd.trustchain.security.KeyEncryption;
import com.tvntd.trustchain.trans.dao.TransactionRepo;

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
    public void saveAccount(String privKey, String password, String ownerUuid)
    {
        ECKey key = ECKey.fromPrivate(Hex.decode(privKey));
        byte[] pubKey = key.getAddress();
        String account = toJsonHex(pubKey);
        KS.KeystoreItem item = KeyEncryption.encryptKey(key, ownerUuid, password);

        keyRepo.save(new AccountKey(account, ownerUuid, item.toByteArray()));
        AccountKey actKey = keyRepo.findByAccount(account);
        if (actKey != null) {
            ECKey dup = KeyEncryption.decryptKey(actKey.getPrivKey(), password);
            if (!dup.equals(key)) {
                System.out.println("BUG!");
            }
        }
    }
}
