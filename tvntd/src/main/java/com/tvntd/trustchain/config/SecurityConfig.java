/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation
    .web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation
    .web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.tvntd.trustchain.util.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private AuthenticationSuccessHandler urlAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/client/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // @formatter:off
        http.authorizeRequests()
            .antMatchers(
                    "/",
                    "/rs/**",
                    "/login/**",
                    "/register/**",
                    "/public/**",
                    "/api/**",
                    "/help/**"
            ).permitAll()
            .antMatchers("/user/**").hasRole(Constants.User)
            .antMatchers("/admin/**").hasRole(Constants.Admin)
            .antMatchers("/sec-api/**").hasRole(Constants.User)
            .antMatchers("/db/***").hasAnyRole(Constants.Dba)
            .anyRequest().fullyAuthenticated()
            .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(urlAuthenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .usernameParameter("email").passwordParameter("password")
                .permitAll()
            .and()
                .sessionManagement()
                    .invalidSessionUrl("/")
                    .sessionFixation()
                .none()
            .and()
                .logout()
                    .invalidateHttpSession(false)
                    .logoutSuccessUrl("/login?logout")
                    .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
                .exceptionHandling().accessDeniedPage("/403")
            .and()
                .rememberMe()
            .and()
                .csrf();
        // @formatter:on
    }
}
