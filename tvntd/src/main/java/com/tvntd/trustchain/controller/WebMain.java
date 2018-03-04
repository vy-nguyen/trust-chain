/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ethereum.config.SystemProperties;
import org.ethereum.facade.Ethereum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebMain
{
    @Autowired
    Ethereum ethereum;

    @Autowired
    SystemProperties sysProps;

    @MessageMapping("/query/machineInfo")
    public String getMachineInfo()
    {
        return "Hello";
    }

    @RequestMapping(value = "/",  method = RequestMethod.GET)
    public String index(HttpServletRequest reqt)
    {
        return "index";
    }

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    @ResponseBody
    public JsonOutput
    getName(HttpSession session, HttpServletRequest reqt, HttpServletResponse resp)
    {
        return new JsonOutput();
    }

    public static class JsonOutput
    {
        String lastName;
        String firstName;
        String nickName;

        public JsonOutput()
        {
            lastName = "Nguyen";
            firstName = "Vy";
            nickName = "Tom";
        }

        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @return the firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @return the nickName
         */
        public String getNickName() {
            return nickName;
        }
    }
}
