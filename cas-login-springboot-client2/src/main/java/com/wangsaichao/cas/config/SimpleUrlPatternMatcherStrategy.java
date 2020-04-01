package com.wangsaichao.cas.config;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @author: wangsaichao
 * @date: 2018/8/7
 * @description: 自定义鉴权路径
 */
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Pattern pattern;

    // 在matches方法内,可以进行扩展,可以进行查库操作
    @Override
    public boolean matches(String url) {
        logger.info("url地址是：" + url);
        //在此可以做额外的扩展,比如判断是insert也放行 可以通过查询数据库来进行动态判断
        if (url.contains("/insert")) {
            return true;
        }
        //默认是根据cas.ignore-pattern来判断是否否满足过滤
        return this.pattern.matcher(url).find();
    }

    /**
     * 这个pattern就是 在application.properties中配置的cas.ignore-pattern
     *
     * @param pattern
     */
    @Override
    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
}
