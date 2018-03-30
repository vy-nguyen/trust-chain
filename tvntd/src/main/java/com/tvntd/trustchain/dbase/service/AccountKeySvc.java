/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tvntd.trustchain.dbase.access.IAccountKey;
import com.tvntd.trustchain.dbase.dao.AccountKeyRepo;
import com.tvntd.trustchain.dbase.models.AccountKey;

@Service
@Transactional
public class AccountKeySvc implements IAccountKey
{
    @Autowired
    protected AccountKeyRepo keyRepo;

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
        System.out.println("Save account " + account + " uuid " + ownerUuid);
        keyRepo.save(new AccountKey(account, ownerUuid, privKey));
    }
}
