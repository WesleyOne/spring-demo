package io.github.wesleyone.spring.core.c5;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *
 * proxyTargetClass=true    强制使用CGLIB代理
 * exposeProxy=true         解决自调用时，不使用代理对象的问题。true则使用代理对象。
 *
 * @author http://wesleyone.github.io/
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true,exposeProxy=true)
public class AopConfig {

}

