package io.github.wesleyone.spring.core.c5.c8;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用Cglib给目标对象创建代理<br/>
 * 使用CGLIB，final方法无法被通知，因为无法在运行时生成的子类中覆盖方法<br/>
 *
 * @author http://wesleyone.github.io/
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CglibAopConfig {
}
