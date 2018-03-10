/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rpc;

import java.util.ArrayList;

import org.ethereum.jsonrpc.JsonRpc.BlockResult;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/rpc/ether")
public interface EthereumRpc
{
    class AccountDTO
    {
        public String owner;
        public String balance;
        public String addressHex;
        public String privateKeyHex;
    }

    AccountDTO ether_account(@JsonRpcParam(value="name") String name);
    AccountDTO ether_createAccount();

    String ether_getBalance(@JsonRpcParam(value="address") String address)
                throws Exception;

    ArrayList<String> eth_listAccount();

    BlockResult eth_getBlockByNumber(
            @JsonRpcParam(value = "id") String bnOrId,
            @JsonRpcParam(value = "full") Boolean full) throws Exception;
}
