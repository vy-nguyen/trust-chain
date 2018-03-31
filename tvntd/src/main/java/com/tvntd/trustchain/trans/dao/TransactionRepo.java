/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.trans.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tvntd.trustchain.trans.models.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String>
{
    Transaction findByOwnerUuid(String ownerUuid);
    List<Transaction> findAllByOwnerUuid(String ownerUuid);
}
