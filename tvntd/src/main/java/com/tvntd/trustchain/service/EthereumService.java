/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import com.ethercamp.harmony.jsonrpc.TransactionResultDTO;
import com.tvntd.trustchain.rpc.RpcDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ethereum.core.Block;
import org.ethereum.core.BlockHeader;
import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.Repository;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ethercamp.harmony.jsonrpc.TypeConverter.toJsonHex;

@Service
public class EthereumService
{
    private static final Log s_log = LogFactory.getLog(EthereumService.class);

    @Autowired
    Ethereum eth;

    @Autowired
    BlockchainImpl blockchain;

    @Autowired
    Repository repository;

    public RpcDTO.BlockResult getBlockResult(Block block, boolean fullTx)
    {
        if (block == null) {
            return null;
        }
        boolean isPending = ByteUtil.byteArrayToLong(block.getNonce()) == 0;
        RpcDTO.BlockResult br = new RpcDTO.BlockResult();

        if (isPending) {
            br.number = null;
            br.hash = null;
            br.nonce = null;
            br.logsBloom = null;
            br.miner = null;
        } else {
            br.number = toJsonHex(block.getNumber());
            br.hash = toJsonHex(block.getHash());
            br.nonce = toJsonHex(block.getNonce());
            br.logsBloom = toJsonHex(block.getLogBloom());
            br.miner = toJsonHex(block.getCoinbase());
        }
        if (block.getExtraData() != null) {
            br.extraData = toJsonHex(block.getExtraData());
        }
        br.parentHash = toJsonHex(block.getParentHash());
        br.sha3Uncles= toJsonHex(block.getUnclesHash());
        br.transactionsRoot = toJsonHex(block.getTxTrieRoot());
        br.stateRoot = toJsonHex(block.getStateRoot());
        br.difficulty = toJsonHex(block.getDifficultyBI());
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

    public List<RpcDTO.BlockElem> getBlockChain(Block block, int limit)
    {
        List<RpcDTO.BlockElem> out = new ArrayList<>();

        if (limit < 0) {
            limit = Integer.MAX_VALUE;
        }
        s_log.info("Get block chain " + block.getNumber() + " max " + limit);

        for (int i = 0; i < limit && block != null; i++) {
            RpcDTO.BlockElem elm = new RpcDTO.BlockElem();
            boolean isPending = ByteUtil.byteArrayToLong(block.getNonce()) == 0;

            if (isPending == false) {
                elm.number = toJsonHex(block.getNumber());
                elm.hash = toJsonHex(block.getHash());
                elm.miner = toJsonHex(block.getCoinbase());
            } else {
                elm.number = null;
                elm.hash = null;
                elm.miner = null;
            }
            byte[] parentHash = block.getParentHash();
            if (parentHash != null) {
                elm.parentHash = toJsonHex(parentHash);
                block = blockchain.getBlockByHash(parentHash);
                out.add(elm);
            } else {
                block = null;
            }
        }
        return out;
    }
}
