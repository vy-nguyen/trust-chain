/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.tvntd.trustchain.rpc.TvntdRpc;

@Service
@AutoJsonRpcServiceImpl
public class TvntdRpcImpl implements TvntdRpc
{
    @Override
    public VerifyOwner tvntd_verify(String name)
    {
        VerifyOwner ver = new VerifyOwner();

        ver.owner = name;
        ver.userUuid = UUID.randomUUID().toString();
        System.out.println("Invoke verify..." + name);
        return ver;
    }
}

