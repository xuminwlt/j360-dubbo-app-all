package me.j360.dubbo.common.api.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;


@Slf4j
public class MessageSourceBundler implements MessageSourceAware, InitializingBean {

    private final static Locale DefaultLocale = Locale.CHINA;

    private static MessageSource staticMessageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        if (staticMessageSource == null) {
            staticMessageSource = messageSource;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (staticMessageSource == null) {
            throw new NullPointerException("MessageSource不能为null");
        }
    }

    public static String getMessage(String code) {
        return getMessage(code, new Object[0]);
    }

    public static String getMessage(String code, Object[] args) throws NoSuchMessageException {
        return getMessage(code, args, null);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        return staticMessageSource.getMessage(code, args, defaultMessage, getLocale());
    }

    public static String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return staticMessageSource.getMessage(resolvable, getLocale());
    }

    private static Locale getLocale() {
//        Locale locale;
//        try {
//            locale = LocaleContextHolder.getLocale();
//            if (locale == null) {
//                log.info("参数Accept-Language为空,只用默认值.");
//                locale = DefaultLocale;
//            }
//        } catch (Exception e) {
//            log.debug("获取Accept-Language请求头失败,使用默认值.", e);
//            locale = DefaultLocale;
//        }
//        log.info("参数Accept-Language值为{}", locale);
//        return locale;
        // TODO: 17/2/28 暂只支持中文
        return DefaultLocale;
    }
}
