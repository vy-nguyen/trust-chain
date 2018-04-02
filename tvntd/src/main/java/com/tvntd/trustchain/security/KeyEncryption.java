/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * Code copied from Ethereum Harmony KeystoreFormat.java
 * Serialize private key to protobuf format to store in SQL database.
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.crypto.generators.SCrypt;
import org.spongycastle.jcajce.provider.digest.Keccak;
import org.spongycastle.util.encoders.Hex;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tvntd.crypto.proto.KS;

public class KeyEncryption
{
    protected static Logger s_log = LoggerFactory.getLogger("tvntd");

    protected static final int s_scryptN = 4096; // 262144;
    protected static final int s_scryptR = 8;
    protected static final int s_scryptP = 1;
    protected static final int s_scryptDklen = 32;
    protected static final int s_encodeVer = 1;

    public static KS.KeystoreItem encryptKey(ECKey key, String uuid, String password)
    {
        byte[] passwd = (password == null) ?
            sha3(key.getAddress(), uuid.getBytes(StandardCharsets.UTF_8)) :
            password.getBytes(StandardCharsets.UTF_8);

        byte[] salt = genRandomBytes(s_scryptDklen);
        byte[] derivedKey = SCrypt.generate(passwd, salt,
                s_scryptN, s_scryptR, s_scryptP, s_scryptDklen);

        byte[] iv = genRandomBytes(16);
        byte[] privKey = key.getPrivKeyBytes();
        byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        byte[] cipherText = processAES(iv, encryptKey, privKey, Cipher.ENCRYPT_MODE);
        byte[] mac = HashUtil.sha3(concat(
                    Arrays.copyOfRange(derivedKey, 16, 32), cipherText));

        KS.KeystoreItem.Builder itemb = KS.KeystoreItem.newBuilder();
        KS.KeystoreCrypto.Builder crypb = KS.KeystoreCrypto.newBuilder();
        KS.CipherParams.Builder cparamb = KS.CipherParams.newBuilder();
        KS.KdfParams.Builder kdfpb = KS.KdfParams.newBuilder();

        crypb.setCipher("aes-128-ctr");
        crypb.setKdf("scrypt");
        crypb.setMac(ByteString.copyFrom(mac));
        crypb.setCiphertext(ByteString.copyFrom(cipherText));

        cparamb.setIv(ByteString.copyFrom(iv)); 
        kdfpb.setDklen(s_scryptDklen);
        kdfpb.setN(s_scryptN);
        kdfpb.setR(s_scryptR);
        kdfpb.setP(s_scryptP);
        kdfpb.setSalt(ByteString.copyFrom(salt));

        crypb.setCipherparams(cparamb.build());
        crypb.setKdfparams(kdfpb.build());

        itemb.setVersion(s_encodeVer);
        itemb.setCrypto(crypb.build());
        return itemb.build();
    }

    public static ECKey
    decryptKey(byte[] content, byte[] address, String uuid, String password)
    {
        byte[] passwd = (password == null) ?
            sha3(address, uuid.getBytes(StandardCharsets.UTF_8)) :
            password.getBytes(StandardCharsets.UTF_8);

        try {
            KS.KeystoreItem item = KS.KeystoreItem.parser().parseFrom(content);
            return decryptKey(item, passwd);

        } catch(InvalidProtocolBufferException e) {
            s_log.info("Failed to parse " + e.getMessage());
            return null;
        }
    }

    protected static ECKey decryptKey(KS.KeystoreItem item, byte[] password)
    {
        KS.KeystoreCrypto crypto = item.getCrypto();
        byte[] cipherKey = null;

        switch (crypto.getKdf()) {
            case "scrypt":
                cipherKey = checkMacScrypt(crypto, password);
                break;

            default:
                return null;
        }
        if (cipherKey != null) {
            byte[] privKey = processAES(
                    crypto.getCipherparams().getIv().toByteArray(), cipherKey,
                    crypto.getCiphertext().toByteArray(),
                    Cipher.DECRYPT_MODE);
            return ECKey.fromPrivate(privKey);
        }
        return null;
    }

    public static String toHexString(KS.KeystoreItem item) {
        return Hex.toHexString(item.toByteArray());
    }

    public static KS.KeystoreItem fromHexString(String content)
    {
        byte[] raw = Hex.decode(content);
        try {
            return KS.KeystoreItem.parser().parseFrom(raw);

        } catch(InvalidProtocolBufferException e) {
            s_log.info("Failed to parse " + e.getMessage());
        }
        return null;
    }

    public static byte[] concat(byte[] a, byte[] b)
    {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];

        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static byte[]
    processAES(byte[] iv, byte[] keyBytes, byte[] cipherText, int mode)
    {
        SecretKeySpec kspec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(mode, kspec, ivSpec);
            return cipher.doFinal(cipherText);

        } catch(InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException | InvalidAlgorithmParameterException |
                NoSuchPaddingException | NoSuchAlgorithmException e) {
            s_log.info("ProcessAES exception: " + e.getMessage());
        }
        return null;
    }

    public static byte[] genRandomBytes(int size)
    {
        byte[] bytes = new byte[size];
        (new SecureRandom()).nextBytes(bytes);
        return bytes;
    }

    protected static byte[] checkMacScrypt(KS.KeystoreCrypto crypto, byte[] password)
    {
        KS.KdfParams kdfparam = crypto.getKdfparams();
        byte[] h = SCrypt.generate(password,
                kdfparam.getSalt().toByteArray(),
                kdfparam.getN(), kdfparam.getR(),
                kdfparam.getP(), kdfparam.getDklen());
        byte[] part = new byte[16];
        System.arraycopy(h, 16, part, 0, 16);

        byte[] actual = sha3(concat(part, crypto.getCiphertext().toByteArray()));
        if (Arrays.equals(actual, crypto.getMac().toByteArray())) {
            System.arraycopy(h, 0, part, 0, 16);
            return part;
        }
        return null;
    }

    protected static byte[] sha3(byte[] h)
    {
        MessageDigest keccak = new Keccak.Digest256();
        keccak.reset();
        keccak.update(h);
        return keccak.digest();
    }

    public static byte[] sha3(byte[] ...data)
    {
        MessageDigest keccak = new Keccak.Digest256();
        keccak.reset();
        for (byte[] s : data) {
            keccak.update(s);
        }
        return keccak.digest();
    }
}
