/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.access;

import java.util.List;

import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;

import com.tvntd.trustchain.dbase.models.AccountKey;

public interface IAccountKey
{
    AccountKey getAccount(String account);
    AccountKey getAccountByOwner(String ownerUuid);

    List<AccountKey> getAllAccountsByOwner(String ownerUuid);
    List<AccountKey> getAccountByOwners(List<String> ownerUuids);

    ECKey getPrivateKey(String ownerUuid, byte[] account);
    ECKey getPrivateKey(String ownerUuid, String accountHex);

    Transaction signTransaction(int nonce, int txFee, int maxFee, int amount,
            String fromUuid, String fromAcct, String recvAcct, byte[] data);

    void saveAccount(String privKey, String password, String ownerUuid);
    void deleteAccount(byte[] account);

    public static class AccountKeyDTO
    {
        protected AccountKey acctKey;

        public AccountKeyDTO(AccountKey key) {
            acctKey = key;
        }
    }
}
