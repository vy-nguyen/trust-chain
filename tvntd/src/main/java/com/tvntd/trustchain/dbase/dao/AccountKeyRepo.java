/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tvntd.trustchain.dbase.models.AccountKey;

public interface AccountKeyRepo extends JpaRepository<AccountKey, String>
{
    AccountKey findByAccount(String account);
    AccountKey findByOwnerUuid(String ownerUuid);

    List<AccountKey> findAllByOwnerUuid(String ownerUuid);
    List<AccountKey> findByOwnerUuidIn(List<String> ownerUuids);

    @Override
    void delete(AccountKey acct);
}
