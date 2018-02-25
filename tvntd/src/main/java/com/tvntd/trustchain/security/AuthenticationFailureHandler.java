/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web
    .authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("authenticationFailureHandler")
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException
    {
    }
}
