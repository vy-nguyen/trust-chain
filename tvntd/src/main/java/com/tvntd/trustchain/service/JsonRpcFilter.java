/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.tvntd.trustchain.util.Constants;

@WebFilter(urlPatterns = {
    Constants.EtherRpc,
    Constants.TvntdRpc
})
public class JsonRpcFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        if ((request instanceof HttpServletRequest) ||
            (response instanceof HttpServletResponse)) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse)response;

            String uri = httpRequest.getRequestURI();
            System.out.println(">>>  Check uri " + uri);
            if (Constants.EtherRpc.equals(uri) || Constants.TvntdRpc.equals(uri)) {
                httpResponse.addHeader("content-type", "application/json");
                httpResponse.addHeader("accept", "application/json");
                chain.doFilter(new JsonHeader(httpRequest), response);

                System.out.println("Modify header...");
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    public static class JsonHeader extends HttpServletRequestWrapper
    {
        public JsonHeader(HttpServletRequest req) {
            super(req);
        }

        @Override
        public String getHeader(String name)
        {
            if (name != null && "content-type".equals(name.toLowerCase())) {
                return "application/json";
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames()
        {
            List<String> names = Collections.list(super.getHeaderNames());
            return Collections.enumeration(names);
        }
    }
}
