/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import org.ethereum.core.Block;
import org.ethereum.core.BlockHeader;
import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.jsonrpc.JsonRpc.BlockResult;
import org.ethereum.jsonrpc.TransactionResultDTO;
import org.ethereum.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.tvntd.trustchain.rpc.EthereumRpc;

import static com.tvntd.trustchain.util.TypeConverter.*;

import java.util.ArrayList;
import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class EthereumRpcImpl implements EthereumRpc
{
    @Autowired
    Ethereum eth;

    @Autowired
    BlockchainImpl blockchain;

    @Override
    public Account ether_account(String name)
    {
        Account account = new Account();

        account.owner = name;
        account.balance = "$1000.33";
        return account;
    }

    public BlockResult eth_getBlockByNumber(String bnOrId, Boolean full)
        throws Exception
    {
        long block = Long.parseLong(bnOrId);
        Block b = blockchain.getBlockByNumber(block);
        return getBlockResult(b, full);
    }

    protected BlockResult getBlockResult(Block block, boolean fullTx)
    {
        if (block == null) {
            return null;
        }
        boolean isPending = ByteUtil.byteArrayToLong(block.getNonce()) == 0;
        BlockResult br = new BlockResult();
        br.number = isPending ? null : toJsonHex(block.getNumber());
        br.hash = isPending ? null : toJsonHex(block.getHash());
        br.parentHash = toJsonHex(block.getParentHash());
        br.nonce = isPending ? null : toJsonHex(block.getNonce());
        br.sha3Uncles= toJsonHex(block.getUnclesHash());
        br.logsBloom = isPending ? null : toJsonHex(block.getLogBloom());
        br.transactionsRoot = toJsonHex(block.getTxTrieRoot());
        br.stateRoot = toJsonHex(block.getStateRoot());
        br.miner = isPending ? null : toJsonHex(block.getCoinbase());
        br.difficulty = toJsonHex(block.getDifficultyBI());
        if (block.getExtraData() != null) {
            br.extraData = toJsonHex(block.getExtraData());
        }
        br.size = toJsonHex(block.getEncoded().length);
        br.gasLimit = toJsonHex(block.getGasLimit());
        br.gasUsed = toJsonHex(block.getGasUsed());
        br.timestamp = toJsonHex(block.getTimestamp());

        List<Object> txes = new ArrayList<>();
        if (fullTx) {
            for (int i = 0; i < block.getTransactionsList().size(); i++) {
                txes.add(new TransactionResultDTO(block, i,
                            block.getTransactionsList().get(i)));
            }
        } else {
            for (Transaction tx : block.getTransactionsList()) {
                txes.add(toJsonHex(tx.getHash()));
            }
        }
        br.transactions = txes.toArray();

        List<String> ul = new ArrayList<>();
        for (BlockHeader header : block.getUncleList()) {
            ul.add(toJsonHex(header.getHash()));
        }
        br.uncles = ul.toArray(new String[ul.size()]);
        return br;
    }
}

