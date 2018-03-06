/*
 *--------1---------2---------3---------4---------5---------6---------7---------8--------
 * Copyright (c) 2018 by Vy Nguyen
 * BSD License
 *
 * @author vynguyen
 */
package com.tvntd.trustchain.plugin;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.ethereum.config.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.typesafe.config.ConfigFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class BaseApp implements Runnable
{
    public static final Logger s_log = LoggerFactory.getLogger("tvntd");
    private static CustomFilter s_filter;

    protected String m_config;

    @Bean
    public SystemProperties systemProperties()
    {
        SystemProperties props = new SystemProperties();

        if (m_config != null) {
            props.overrideParams(ConfigFactory
                    .parseString(m_config.replaceAll("'", "\"")));
        }
        return props;
    }

    @PostConstruct
    private void springInit()
    {
        setupLogging();
    }

    public void run()
    {
    }

    protected void setupLogging()
    {
        if (s_filter == null) {
            s_filter = new CustomFilter();
            LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
            Appender<ILoggingEvent> ca = ctx.getLogger("ROOT").getAppender("STDOUT");

            ca.clearAllFilters();
            ca.addFilter(s_filter);
        }
        s_filter.addVisibleLogger("tvntd");
    }

    private static class CustomFilter extends Filter<ILoggingEvent>
    {
        private Set<String> visibleLoggers = new HashSet<>();

        @Override
        public synchronized FilterReply decide(ILoggingEvent event)
        {
            Level level = event.getLevel();

            return (
                visibleLoggers.contains(event.getLoggerName()) &&
                level.isGreaterOrEqual(Level.INFO) ||
                level.isGreaterOrEqual(Level.ERROR
            ) ? FilterReply.NEUTRAL : FilterReply.DENY);
        }

        public synchronized void addVisibleLogger(String name) {
            visibleLoggers.add(name);
        }
    }
}
