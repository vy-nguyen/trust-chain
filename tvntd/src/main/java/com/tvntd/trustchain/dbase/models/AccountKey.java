/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.dbase.models;

import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
@Table(indexes = {
    @Index(columnList = "ownerUuid", unique = false)
})
public class AccountKey
{
    @Id
    @Column(length = 64)
    private String account;

    @Column(length = 64)
    private String ownerUuid;

    @Column(length = 64)
    private byte[] keyPassword;

    @Column(length = 512)
    private byte[] privKey;

    public AccountKey() {}
    public AccountKey(String account, String ownerUuid, String key)
    {
        this.account = account;
        this.ownerUuid = ownerUuid;
        this.keyPassword = null;
        this.privKey = key.getBytes(Charset.forName("UTF-8"));
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
     * @return the keyPassword
     */
    public byte[] getKeyPassword() {
        return keyPassword;
    }

    /**
     * @param keyPassword the keyPassword to set
     */
    public void setKeyPassword(byte[] keyPassword) {
        this.keyPassword = keyPassword;
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
