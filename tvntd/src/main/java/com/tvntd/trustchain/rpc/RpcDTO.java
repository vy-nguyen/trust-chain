/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rpc;

import java.util.Arrays;

import org.ethereum.core.Block;
import org.ethereum.core.CallTransaction;
import org.ethereum.core.Transaction;
import org.ethereum.vm.LogInfo;

import static com.ethercamp.harmony.jsonrpc.TypeConverter.*;

public interface RpcDTO
{
    public static class BlockResult
    {
        public String number;
        public String hash;
        public String parentHash;
        public String nonce;
        public String sha3Uncles;
        public String logsBloom;
        public String transactionsRoot;
        public String stateRoot;
        public String receiptRoot;
        public String miner;
        public String difficulty;
        public String totalDifficulty;
        public String extraData;
        public String size;
        public String gasLimit;
        public String gasUsed;
        public String timestamp;
        public Object[] transactions;
        public String[] uncles;

        @Override
        public String toString()
        {
            return "BlockResult{" +
                    "number='" + number + '\'' +
                    ", hash='" + hash + '\'' +
                    ", parentHash='" + parentHash + '\'' +
                    ", nonce='" + nonce + '\'' +
                    ", sha3Uncles='" + sha3Uncles + '\'' +
                    ", logsBloom='" + logsBloom + '\'' +
                    ", transactionsRoot='" + transactionsRoot + '\'' +
                    ", stateRoot='" + stateRoot + '\'' +
                    ", receiptRoot='" + receiptRoot + '\'' +
                    ", miner='" + miner + '\'' +
                    ", difficulty='" + difficulty + '\'' +
                    ", totalDifficulty='" + totalDifficulty + '\'' +
                    ", extraData='" + extraData + '\'' +
                    ", size='" + size + '\'' +
                    ", gas='" + gasLimit + '\'' +
                    ", gasUsed='" + gasUsed + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", transactions=" + Arrays.toString(transactions) +
                    ", uncles=" + Arrays.toString(uncles) +
                    '}';
        }
    }

    public static class BlockElem
    {
        public String number;
        public String hash;
        public String parentHash;
        public String miner;

        @Override
        public String toString()
        {
            return (new StringBuilder())
                .append("Block [").append(number).append("] ")
                .append(hash).append("\n\t<- ").append(parentHash)
                .append("[").append(miner).append("]\n").toString();
        }
    }

    public static class CompilationResult
    {
        public String code;
        public CompilationInfo info;

        @Override
        public String toString() {
            return "CompilationResult{" +
                    "code='" + code + '\'' +
                    ", info=" + info +
                    '}';
        }
    }

    public static class CompilationInfo
    {
        public String source;
        public String language;
        public String languageVersion;
        public String compilerVersion;
        public CallTransaction.Function[] abiDefinition;
        public String userDoc;
        public String developerDoc;

        @Override
        public String toString() {
            return "CompilationInfo{" +
                    "source='" + source + '\'' +
                    ", language='" + language + '\'' +
                    ", languageVersion='" + languageVersion + '\'' +
                    ", compilerVersion='" + compilerVersion + '\'' +
                    ", abiDefinition=" + abiDefinition +
                    ", userDoc='" + userDoc + '\'' +
                    ", developerDoc='" + developerDoc + '\'' +
                    '}';
        }
    }

    public static class FilterRequest
    {
        public String fromBlock;
        public String toBlock;
        public Object address;
        public Object[] topics;

        @Override
        public String toString() {
            return "FilterRequest{" +
                    "fromBlock='" + fromBlock + '\'' +
                    ", toBlock='" + toBlock + '\'' +
                    ", address=" + address +
                    ", topics=" + Arrays.toString(topics) +
                    '}';
        }
    }

    public static class LogFilterElement
    {
        public String logIndex;
        public String blockNumber;
        public String blockHash;
        public String transactionHash;
        public String transactionIndex;
        public String address;
        public String data;
        public String[] topics;

        public LogFilterElement(LogInfo logInfo,
                Block b, int txIndex, Transaction tx, int logIdx)
        {
            logIndex = toJsonHex(logIdx);
            blockNumber = b == null ? null : toJsonHex(b.getNumber());
            blockHash = b == null ? null : toJsonHex(b.getHash());
            transactionIndex = b == null ? null : toJsonHex(txIndex);
            transactionHash = toJsonHex(tx.getHash());
            address = tx.getReceiveAddress() == null ?
                null : toJsonHex(tx.getReceiveAddress());

            data = toJsonHex(logInfo.getData());
            topics = new String[logInfo.getTopics().size()];
            for (int i = 0; i < topics.length; i++) {
                topics[i] = toJsonHex(logInfo.getTopics().get(i).getData());
            }
        }

        @Override
        public String toString()
        {
            return "LogFilterElement{" +
                    "logIndex='" + logIndex + '\'' +
                    ", blockNumber='" + blockNumber + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    ", transactionHash='" + transactionHash + '\'' +
                    ", transactionIndex='" + transactionIndex + '\'' +
                    ", address='" + address + '\'' +
                    ", data='" + data + '\'' +
                    ", topics=" + Arrays.toString(topics) +
                    '}';
        }
    }

    public static class SyncStatusDTO
    {
        public String state;
        public long curCnt;
        public long knownCnt;
        public long blockLastImported;
        public long blockBestKnown;
    }

    public static class SyncReportDTO
    {
        public String startingBlock;
        public String currentBlock;
        public String highestBlock;
        public boolean downloaded;
        public SyncStatusDTO status;
    }

    public static class KeysDTO
    {
        public String kind;
        public String privateKey;
        public String publicKey;
    }
}
