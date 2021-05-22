package io.github.wesleyone.spring.core.c5.c4;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 通过Java配置启用@AspectJ支持
 *
 * proxyTargetClass=true    强制使用CGLIB代理
 * exposeProxy=true         将代理对象保存到本地线程。
 *
 * @author http://wesleyone.github.io/
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

}

