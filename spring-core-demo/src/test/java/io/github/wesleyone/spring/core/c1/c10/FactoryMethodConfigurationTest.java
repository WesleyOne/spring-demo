package io.github.wesleyone.spring.core.c1.c10;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class FactoryMethodConfigurationTest {

    @Test
    public void isSameTest() {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(FactoryMethodConfiguration.class);
        NormalBeanOne normalBeanOne = applicationContext.getBean(NormalBeanOne.class);
        NormalBeanTwo normalBeanTwo = applicationContext.getBean(NormalBeanTwo.class);
        System.out.println("normalBeanOne.getNormalBeanTwo()："+normalBeanOne.getNormalBeanTwo());
        System.out.println("normalBeanTwo："+normalBeanTwo);
        // `@Bean`和`@Configuration`一起使用，调用依赖对象`@Bean`方法会通过CGLIB代理在容器中查找该beanName；
        Assert.assertEquals(normalBeanTwo, normalBeanOne.getNormalBeanTwo());
    }
}