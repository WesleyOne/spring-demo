package io.github.wesleyone.spring.core.c5.c81;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 暴露代理到本地线程<br/>
 * 通过<code>AopContext.currentProxy()</code>获取本地线程中的代理对象
 *
 * @author http://wesleyone.github.io/
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class ExposeProxyAopConfig {


}
