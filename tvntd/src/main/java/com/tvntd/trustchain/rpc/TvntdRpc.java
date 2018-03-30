/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rpc;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/rpc/tvntd")
public interface TvntdRpc
{
    class VerifyOwner
    {
        public String owner;
        public String userUuid;
    }

    VerifyOwner tvntd_verify(@JsonRpcParam(value="name") String name);

    String tvntd_saveKey(
            @JsonRpcParam(value="account") String account,
            @JsonRpcParam(value="key") String key);

    String tvntd_trans(
            @JsonRpcParam(value="send") String send,
            @JsonRpcParam(value="recv") String recv,
            @JsonRpcParam(value="amount") int amount);
}
