/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
    "com.tvntd.trustchain.controller",
    "com.tvntd.trustchain.service",
    "com.tvntd.trustchain.dbase.service",
    "com.tvntd.trustchain.trans.service",
    "com.tvntd.trustchain.security"
})
public class ApplicationConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        registry.addResourceHandler("/rs/img/**")
            .addResourceLocations("file://var/www/static/img/");
        registry.addResourceHandler("/rs/upload/**")
            .addResourceLocations("file:///var/www/static/upload/");
        registry.addResourceHandler("/rs/user/**")
            .addResourceLocations("file:///var/www/static/user/");
        registry.addResourceHandler("/rs/obj/**")
            .addResourceLocations("file:///var/www/static/obj/");
        registry.addResourceHandler("/rs/js/**")
            .addResourceLocations("file:///var/www/static/js/");

        registry.addResourceHandler("/rs/images/*").addResourceLocations("/images/");
        registry.addResourceHandler("/rs/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/rs/client/**").addResourceLocations("/client/");
        registry.addResourceHandler("/rs/fonts/**").addResourceLocations("/fonts/");
        registry.addResourceHandler("/views/**").addResourceLocations("/views/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        LocaleChangeInterceptor lc = new LocaleChangeInterceptor();
        lc.setParamName("lang");
        registry.addInterceptor(lc);
    }

    @Bean
    public InternalResourceViewResolver viewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        super.addViewControllers(registry);
    }
}
