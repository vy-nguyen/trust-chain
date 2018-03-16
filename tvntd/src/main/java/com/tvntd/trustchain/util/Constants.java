/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.util;

public class Constants
{
    public static final String User        = "User";
    public static final String Admin       = "Admin";
    public static final String Dba         = "Dba";
    public static final String Auditor     = "Auditor";
    public static final String AuthUser    = "ROLE_" + User;
    public static final String AuthAdmin   = "ROLE_" + Admin;
    public static final String AuthDba     = "ROLE_" + Dba;
    public static final String AuthAuditor = "ROLE_" + Auditor;

    public static final String EtherRpc    = "/rpc/ether";
    public static final String TvntdRpc    = "/rpc/tvntd";
}
