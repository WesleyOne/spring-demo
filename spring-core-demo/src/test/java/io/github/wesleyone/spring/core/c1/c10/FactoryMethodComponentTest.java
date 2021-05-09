package io.github.wesleyone.spring.core.c1.c10;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author http://wesleyone.github.io/
 */
public class FactoryMethodComponentTest {

    @Test
    public void notSameTest() {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(FactoryMethodComponent.class);
        NormalBeanOne normalBeanOne = applicationContext.getBean(NormalBeanOne.class);
        NormalBeanTwo normalBeanTwo = applicationContext.getBean(NormalBeanTwo.class);
        System.out.println("normalBeanOne.getNormalBeanTwo()："+normalBeanOne.getNormalBeanTwo());
        System.out.println("normalBeanTwo："+normalBeanTwo);
        // `@Bean`和`@Component`一起使用，调用依赖对象`@Bean方法`就是普通JAVA语义方法调用；
        Assert.assertNotEquals(normalBeanTwo, normalBeanOne.getNormalBeanTwo());
    }

}