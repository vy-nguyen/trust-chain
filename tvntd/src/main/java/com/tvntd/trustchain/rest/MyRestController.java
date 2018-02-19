/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tvntd.trustchain.ethereum.EthereumBean;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MyRestController
{
    @Autowired
    EthereumBean ethereumBean;

    @RequestMapping(
        value = "/bestBlock",
        method = GET, produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String getBestBlock() throws IOException
    {
        System.out.println("Get best block...");
        return ethereumBean.getBestBlock();
    }
}
