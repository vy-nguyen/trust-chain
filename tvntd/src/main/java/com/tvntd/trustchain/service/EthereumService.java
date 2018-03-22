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
import com.tvntd.trustchain.rpc.RpcDTO.AccountStateDTO;
import com.tvntd.trustchain.rpc.RpcDTO.AccountStateTrieDTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ethereum.config.CommonConfig;
import org.ethereum.core.AccountState;
import org.ethereum.core.Block;
import org.ethereum.core.BlockHeader;
import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.Repository;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.HashUtil;
import org.ethereum.datasource.Source;
import org.ethereum.facade.Ethereum;
import org.ethereum.trie.SecureTrie;
import org.ethereum.trie.TrieImpl;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.FastByteComparisons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Integer getReferencedTrieNodes(Source<byte[], byte[]> stateDS,
            boolean includeAcct, byte[] ... roots)
    {
        final AtomicInteger ret = new AtomicInteger(0);
        for (byte[] root : roots) {
            SecureTrie trie = new SecureTrie(stateDS, root);
            trie.scanTree(new TrieImpl.ScanAction() {
                @Override
                public void doOnNode(byte[] hash, TrieImpl.Node node) {
                    ret.incrementAndGet();
                }

                @Override
                public void doOnValue(byte[] nodeHash,
                        TrieImpl.Node node, byte[] key, byte[] value)
                {
                    if (includeAcct) {
                        AccountState accountState = new AccountState(value);
                        if (!FastByteComparisons.equal(
                                    accountState.getCodeHash(),
                                    HashUtil.EMPTY_DATA_HASH)) {
                            ret.incrementAndGet();
                        }
                        if (!FastByteComparisons.equal(
                                    accountState.getStateRoot(),
                                    HashUtil.EMPTY_TRIE_HASH)) {
                            ret.addAndGet(getReferencedTrieNodes(
                                        stateDS, false,
                                        accountState.getStateRoot()));
                        }
                    }
                }
            });

        }
        return ret.get();
    }

    public List<AccountStateDTO> getAccountState(Source<byte[], byte[]> stateDS,
            List<String> trieOut, byte[] ...roots)
    {
        List<AccountStateDTO> out = new LinkedList<>();

        for (byte[] root : roots) {
            SecureTrie trie = new SecureTrie(stateDS, root);
            trieOut.add(trie.dumpTrie(false));

            trie.scanTree(new TrieImpl.ScanAction() {
                @Override
                public void doOnNode(byte[] hash, TrieImpl.Node node) {
                }

                @Override
                public void doOnValue(byte[] nodeHash,
                        TrieImpl.Node node, byte[] key, byte[] value)
                {
                    AccountState acct = new AccountState(value);
                    byte[] stRoot = acct.getStateRoot();

                    out.add(new AccountStateDTO(acct, key));
                    if (!FastByteComparisons.equal(stRoot, HashUtil.EMPTY_TRIE_HASH)) {
                        out.addAll(getAccountState(stateDS, trieOut, stRoot));
                    }
                }
            });
        }
        return out;
    }

    public AtomicInteger checkNodes(Ethereum ether, CommonConfig config)
    {
        AtomicInteger errors = new AtomicInteger(0);

        try {
            Source<byte[], byte[]> stateDS = config.stateSource();
            byte[] stateRoot = ether.getBlockchain()
                .getBestBlock().getHeader().getStateRoot();

            Integer rootSize = getReferencedTrieNodes(stateDS, true, stateRoot);
            System.out.println("Non unique node size " + rootSize);

        } catch(Exception e) {
            s_log.info("Exception " + e.getMessage());
            errors.incrementAndGet();
        }
        return errors;
    }

    public AccountStateTrieDTO getAllAccounts(Ethereum ether, CommonConfig config)
    {
        try {
            List<String> trieOut = new LinkedList<>();
            AccountStateTrieDTO out = new AccountStateTrieDTO();
            Source<byte[], byte[]> stateDS = config.stateSource();
            byte[] stateRoot = ether.getBlockchain()
                .getBestBlock().getHeader().getStateRoot();

            out.accountState = getAccountState(stateDS, trieOut, stateRoot);
            out.trieDump = trieOut.get(0);
            return out;

        } catch(Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
        return null;
    }
}
