/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.trans.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction
{
    @Id
    @Column(length = 64, name = "owner_uuid")
    protected String ownerUuid;

    @Column(length = 64)
    protected String account;

    @Column(length = 64, name = "peer_account")
    protected String peerAccount;

    @Column(length = 64, name = "block_hash")
    protected String blockHash;

    @Column(name = "block_number")
    protected Long blockNumber;

    protected Long amount;

    public Transaction() {}
    public Transaction(String uuid, String owner,
            String peer, Long amount, String hash, Long block)
    {
        this.ownerUuid = uuid;
        this.account = owner;
        this.peerAccount = peer;
        this.blockHash = hash;
        this.blockNumber = block;
        this.amount = amount;
    }

    /**
     * @return the ownerUuid
     */
    public String getOwnerUuid() {
        return ownerUuid;
    }

    /**
     * @param ownerUuid the ownerUuid to set
     */
    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the blockHash
     */
    public String getBlockHash() {
        return blockHash;
    }

    /**
     * @param blockHash the blockHash to set
     */
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * @return the blockNumber
     */
    public Long getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param blockNumber the blockNumber to set
     */
    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }
}
