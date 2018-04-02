/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.unit;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.ethereum.crypto.ECKey;
import org.ethereum.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tvntd.config.TestKeyStoreJPAConfig;
import com.tvntd.config.TestPersistenceJPAConfig;
import com.tvntd.trustchain.config.ApplicationConfig;
import com.tvntd.trustchain.config.Config;
import com.tvntd.trustchain.config.EthereumConfig;
import com.tvntd.trustchain.config.SecurityConfig;
import com.tvntd.trustchain.config.WebConfig;
import com.tvntd.trustchain.dbase.access.IAccountKey;
import com.tvntd.trustchain.trans.api.ITransaction;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.properties")
@WebAppConfiguration
@ContextConfiguration(classes = {
    ApplicationConfig.class,
    Config.class,
    SecurityConfig.class,
    EthereumConfig.class,
    WebConfig.class,
    TestKeyStoreJPAConfig.class,
    TestPersistenceJPAConfig.class
})
public class TestKeyStore
{
    protected List<KeyRecord> checkKeys;

    @Autowired
    protected IAccountKey acctSvc;

    @Autowired
    protected ITransaction transSvc;

    @Before
    public void setupStreams()
    {
    }

    @After
    public void cleanupStreams()
    {
    }

    @Test
    public void testKeyStore()
    {
        System.out.println("Test save 1000 keys");
        checkKeys = new LinkedList<>();

        for (int i = 0; i < 1000; i++) {
            saveKeyTest();
        }
        System.out.println("Verify decoding of encrypted keys");
        for (KeyRecord r : checkKeys) {
            verifyKeyTest(r);
        }
    }

    static class KeyRecord
    {
        public ECKey  key;
        public String uuid;

        public KeyRecord(String uuid, ECKey key)
        {
            this.key = key;
            this.uuid = uuid;
        }
    }

    protected void saveKeyTest()
    {
        ECKey key = new ECKey(Utils.getRandom());
        String uuid = UUID.randomUUID().toString();

        String privKey = Hex.toHexString(key.getPrivKeyBytes());
        acctSvc.saveAccount(privKey, null, uuid);
        checkKeys.add(new KeyRecord(uuid, key));
    }

    protected void verifyKeyTest(KeyRecord r)
    {
        ECKey key = r.key;
        ECKey dbKey = acctSvc.getPrivateKey(r.uuid, key.getAddress());

        assertEquals(key, dbKey);
        acctSvc.deleteAccount(key.getAddress());
    }
}
