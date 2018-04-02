/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.service;

import java.util.List;

import org.ethereum.core.Transaction;
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
    public AccountKey getAccount(String account) {
        return keyRepo.findByAccount(account);
    }

    @Override
    public AccountKey getAccountByOwner(String ownerUuid) {
        return keyRepo.findByOwnerUuid(ownerUuid);
    }

    @Override
    public List<AccountKey> getAllAccountsByOwner(String ownerUuid) {
        return keyRepo.findAllByOwnerUuid(ownerUuid);
    }

    @Override
    public List<AccountKey> getAccountByOwners(List<String> ownerUuids) {
        return keyRepo.findByOwnerUuidIn(ownerUuids);
    }

    /**
     * TODO: find ways to encode password.
     */
    @Override
    public ECKey getPrivateKey(String ownerUuid, String accountHex)
    {
        AccountKey key = keyRepo.findByAccount(accountHex);
        if (key == null) {
            return null;
        }
        byte[] account = Hex.decode(accountHex);
        return KeyEncryption.decryptKey(key.getPrivKey(), account, ownerUuid, null);
    }

    @Override
    public ECKey getPrivateKey(String ownerUuid, byte[] account)
    {
        AccountKey key = keyRepo.findByAccount(toJsonHex(account));
        if (key == null) {
            return null;
        }
        return KeyEncryption.decryptKey(key.getPrivKey(), account, ownerUuid, null);
    }

    @Override
    public Transaction signTransaction(int nonce, int txFee, int maxFee, int amount,
            String fromUuid, String fromAcct, String recvAcct, byte[] data)
    {
        return null;
    }

    @Override
    public void saveAccount(String privKey, String password, String ownerUuid)
    {
        ECKey key = ECKey.fromPrivate(Hex.decode(privKey));
        byte[] pubKey = key.getAddress();
        String account = toJsonHex(pubKey);
        KS.KeystoreItem item = KeyEncryption.encryptKey(key, ownerUuid, null);

        keyRepo.save(new AccountKey(account, ownerUuid, item.toByteArray()));
    }

    @Override
    public void deleteAccount(byte[] account)
    {
        String dbkey = toJsonHex(account);
        keyRepo.delete(dbkey);
    }
}
