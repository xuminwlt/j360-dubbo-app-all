package me.j360.dubbo.trace.brave;

import com.github.kristofa.brave.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.Span;
import zipkin.reporter.Reporter;

import java.util.Collections;


/**
 * Package: me.j360.dubbo.client
 * User: min_xu
 * Date: 2017/4/27 下午4:15
 * 说明：
 */
public class Slf4jLogReporter implements Reporter<Span> {

    final Logger logger;

    public Slf4jLogReporter() {
        this("com.github.kristofa.brave.LoggingSpanCollector");
    }

    public Slf4jLogReporter(String loggerName) {
        Util.checkNotBlank(loggerName, "Null or blank loggerName", new Object[0]);
        this.logger = LoggerFactory.getLogger(loggerName);
    }

    public void report(Span span) {
        Util.checkNotNull(span, "Null span", new Object[0]);
        this.logger.info(Collections.singletonList(span).toString());
    }
}