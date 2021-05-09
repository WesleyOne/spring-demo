package io.github.wesleyone.spring.core.c1.c5;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author http://wesleyone.github.io/
 */
public class LifecycleCallbacksBean implements InitializingBean, DisposableBean {

    @PostConstruct
    public void postConstruct() {
        System.out.println("初始化1.`@PostConstruct`");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化2.`InitializingBean#afterPropertiesSet()`");
    }

    public void initMethod() {
        System.out.println("初始化3.xml配置`init-method`属性或者`@Bean`配置`init`属性（包括默认配置）");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("销毁1.`@PreDestroy`");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("销毁2.`DisposableBean#destroy()`");
    }

    public void destroyMethod() {
        System.out.println("销毁3.xml配置`destroy-method`属性或者`@Bean`配置`destroy`属性（包括默认配置）");
    }

}
