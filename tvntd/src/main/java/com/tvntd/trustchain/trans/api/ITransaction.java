/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.trans.api;

import java.util.List;

import com.tvntd.trustchain.trans.models.Transaction;

public interface ITransaction
{
    List<Transaction> findTransactions(String ownerUuid);

    public static class TransactionDTO
    {
    }
}
