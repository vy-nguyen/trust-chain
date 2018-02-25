/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rpc;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/rpc/ether")
public interface EthereumRpc
{
    class Account
    {
        public String owner;
        public String balance;
    }

    Account ether_account(@JsonRpcParam(value="name") String name);
}
