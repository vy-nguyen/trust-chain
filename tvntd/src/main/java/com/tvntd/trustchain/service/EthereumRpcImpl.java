/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ethereum.config.CommonConfig;
import org.ethereum.config.SystemProperties;
import org.ethereum.core.Block;
import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.Repository;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.SyncStatus;
import org.ethereum.mine.BlockMiner;
import org.ethereum.sync.SyncManager;
import org.ethereum.util.Utils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ethercamp.harmony.model.Account;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.tvntd.trustchain.rpc.EthereumRpc;
import com.tvntd.trustchain.rpc.RpcDTO.AccountStateTrieDTO;
import com.tvntd.trustchain.rpc.RpcDTO.BlockElem;
import com.tvntd.trustchain.rpc.RpcDTO.BlockResult;
import com.tvntd.trustchain.rpc.RpcDTO.KeysDTO;
import com.tvntd.trustchain.rpc.RpcDTO.SyncReportDTO;
import com.tvntd.trustchain.rpc.RpcDTO.SyncStatusDTO;
import com.ethercamp.harmony.jsonrpc.TypeConverter;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AutoJsonRpcServiceImpl
public class EthereumRpcImpl implements EthereumRpc
{
    private static final Log log = LogFactory.getLog(EthereumRpcImpl.class);

    @Autowired
    Ethereum eth;

    @Autowired
    BlockchainImpl blockchain;

    @Autowired
    Repository repository;

    @Autowired
    SystemProperties config;

    @Autowired
    BlockMiner blockMiner;

    @Autowired
    EthereumService etherSvc;

    @Autowired
    SyncManager syncManager;

    @Autowired
    CommonConfig commonConfig; 

    protected long startSyncBlock;

    @Autowired
    public EthereumRpcImpl(BlockchainImpl blockchain)
    {
        this.blockchain = blockchain;
        this.startSyncBlock = blockchain.getBestBlock().getNumber();
    }

    @Override
    public AccountDTO eth_account(String name)
    {
        AccountDTO account = new AccountDTO();

        account.owner = name;
        account.balance = "$1000.33";
        return account;
    }

    @Override
    public AccountDTO eth_createAccount()
    {
        Account acct = new Account();
        AccountDTO acctDto = new AccountDTO();

        acct.init();
        acctDto.addressHex = Hex.toHexString(acct.getAddress());
        acctDto.privateKeyHex = Hex.toHexString(acct.getEcKey().getPrivKeyBytes());
        return acctDto;
    }

    @Override
    public AccountStateTrieDTO eth_listAccounts() {
        return etherSvc.getAllAccounts(eth, commonConfig);
    }

    @Override
    public String eth_getBalance(String address) throws Exception
    {
        byte[] addrByte = TypeConverter.StringHexToByteArray(address); 
        BigInteger balance = repository.getBalance(addrByte);

        if (balance != null) {
            return TypeConverter.toJsonHex(balance);
        }
        return "0";
    }

    @Override
    public BlockResult eth_getBlockByNumber(String bnOrId, Boolean full)
        throws Exception
    {
        long block = Long.parseLong(bnOrId);
        Block b = blockchain.getBlockByNumber(block);
        return etherSvc.getBlockResult(b, full);
    }

    @Override
    public BlockResult eth_getBlockByHash(String blockHash, Boolean full)
        throws Exception
    {
        byte[] hash = TypeConverter.StringHexToByteArray(blockHash);
        Block b = blockchain.getBlockByHash(hash);
        return etherSvc.getBlockResult(b, full);
    }

    @Override
    public BlockResult eth_latestBlock()
    {
        Block block = blockchain.getBestBlock();
        return etherSvc.getBlockResult(block, true);
    }

    @Override
    public String eth_debug()
    {
        Block block = blockchain.getBestBlock();
        Repository repo = repository.getSnapshotTo(block.getStateRoot());

        AtomicInteger errors = etherSvc.checkNodes(eth, commonConfig);
        return errors.toString() + repo.toString();
    }

    @Override
    public List<BlockElem> eth_getChain(int limit)
    {
        Block block = blockchain.getBestBlock();
        return etherSvc.getBlockChain(block, limit);
    }

    @Override
    public SyncReportDTO eth_syncing()
    {
        SyncReportDTO r = new SyncReportDTO();

        r.startingBlock = TypeConverter.toJsonHex(startSyncBlock);
        r.currentBlock = TypeConverter.toJsonHex(blockchain.getBestBlock().getNumber());
        r.highestBlock = TypeConverter.toJsonHex(syncManager.getLastKnownBlockNumber());

        SyncStatus status = syncManager.getSyncStatus();

        r.downloaded = syncManager.isSyncDone();
        r.status = new SyncStatusDTO();
        r.status.state = status.toString();
        r.status.curCnt = status.getCurCnt();
        r.status.blockLastImported = status.getBlockLastImported();
        r.status.blockBestKnown = status.getBlockBestKnown();
        return r;
    }

    @Override
    public KeysDTO eth_genKey(String kind)
    {
        ECKey key = new ECKey(Utils.getRandom());
        KeysDTO result = new KeysDTO();

        result.privateKey = Hex.toHexString(key.getPrivKeyBytes());
        result.publicKey = Hex.toHexString(key.getNodeId());
        return result;
    }

    @Override
    public boolean miner_start()
    {
        long maxMem = Runtime.getRuntime().maxMemory();

        log.info("Request to start mining: " + maxMem);
        if (config.isMineFullDataset()) {
            log.info("Miner has full dataset");
            long requiredBytes = 2000L << 20;
            if (maxMem < requiredBytes) {
                String error = "Set -Xmx3G to JVM option";

                log.error(error);
                throw new RuntimeException(error);
            }
        }
        blockMiner.startMining();
        return true;
    }

    @Override
    public boolean miner_isMining() {
        return blockMiner.isMining();
    }
}
