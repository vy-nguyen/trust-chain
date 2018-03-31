/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.trans.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvntd.trustchain.trans.api.ITransaction;
import com.tvntd.trustchain.trans.dao.TransactionRepo;
import com.tvntd.trustchain.trans.models.Transaction;

@Service
public class TransactionSvc implements ITransaction
{
    @Autowired
    protected TransactionRepo transRepo;

    @Override
    public List<Transaction> findTransactions(String ownerUuid)
    {
        return null;
    }
}
