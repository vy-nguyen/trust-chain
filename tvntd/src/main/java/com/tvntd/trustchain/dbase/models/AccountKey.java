/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.Charsets;

@Entity
@Table(name = "account_key", indexes = {
    @Index(columnList = "owner_uuid", unique = false)
})
public class AccountKey
{
    @Id
    @Column(length = 64)
    private String account;

    @Column(length = 64, name = "owner_uuid")
    private String ownerUuid;

    @Column(length = 512, name = "priv_key")
    private byte[] privKey;

    public AccountKey() {}
    public AccountKey(String account, String ownerUuid, String key) {
        this(account, ownerUuid, key.getBytes(Charsets.UTF_8));
    }

    public AccountKey(String account, String ownerUuid, byte[] key)
    {
        this.account = account;
        this.ownerUuid = ownerUuid;
        this.privKey = key;
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
     * @return the privKey
     */
    public byte[] getPrivKey() {
        return privKey;
    }

    /**
     * @param privKey the privKey to set
     */
    public void setPrivKey(byte[] privKey) {
        this.privKey = privKey;
    }
}
