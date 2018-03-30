/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.access;

import java.util.List;

import com.tvntd.trustchain.dbase.models.AccountKey;

public interface IAccountKey
{
    AccountKey getAccount(String account);
    AccountKey getAccountByOwner(String ownerUuid);

    List<AccountKey> getAllAccountsByOwner(String ownerUuid);
    List<AccountKey> getAccountByOwners(List<String> ownerUuids);

    void saveAccount(String account, String privKey, String ownerUuid);

    public static class AccountKeyDTO
    {
        protected AccountKey acctKey;

        public AccountKeyDTO(AccountKey key) {
            acctKey = key;
        }
    }
}
