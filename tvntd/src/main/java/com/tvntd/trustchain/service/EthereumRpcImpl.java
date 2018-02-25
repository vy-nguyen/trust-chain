/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.tvntd.trustchain.rpc.EthereumRpc;

@Service
@AutoJsonRpcServiceImpl
public class EthereumRpcImpl implements EthereumRpc
{
    @Override
    public Account ether_account(String name)
    {
        Account account = new Account();

        account.owner = name;
        account.balance = "$1000.33";
        return account;
    }
}

