/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rpc;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.tvntd.trustchain.rpc.RpcDTO.BlockElem;
import com.tvntd.trustchain.rpc.RpcDTO.BlockResult;
import com.tvntd.trustchain.rpc.RpcDTO.KeysDTO;
import com.tvntd.trustchain.rpc.RpcDTO.SyncReportDTO;
import com.tvntd.trustchain.util.Constants;

@JsonRpcService(Constants.EtherRpc)
public interface EthereumRpc
{
    class AccountDTO
    {
        public String owner;
        public String balance;
        public String addressHex;
        public String privateKeyHex;
    }

    AccountDTO eth_account(@JsonRpcParam(value="name") String name);
    AccountDTO eth_createAccount();

    String eth_getBalance(@JsonRpcParam(value="address") String address)
                throws Exception;

    ArrayList<String> eth_listAccount();

    BlockResult eth_getBlockByNumber(
            @JsonRpcParam(value = "id") String bnOrId,
            @JsonRpcParam(value = "full") Boolean full) throws Exception;

    BlockResult eth_getBlockByHash(
            @JsonRpcParam(value = "blockHash") String blockHash,
            @JsonRpcParam(value = "full") Boolean full) throws Exception;

    BlockResult eth_latestBlock();
    List<BlockElem> eth_getChain(@JsonRpcParam(value = "limit") int limit);

    SyncReportDTO eth_syncing();
    KeysDTO eth_genKey(@JsonRpcParam(value = "kind") String kind);

    boolean miner_start();
    boolean miner_isMining();
}
