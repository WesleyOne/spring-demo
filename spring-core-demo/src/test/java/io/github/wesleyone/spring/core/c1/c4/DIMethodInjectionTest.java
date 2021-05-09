package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 方法注入
 * @author http://wesleyone.github.io/
 */
public class DIMethodInjectionTest {

    @Test
    public void methodInjectionTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_6_方法注入.xml");
        NormalBeanManager normalBeanManager = context.getBean("normalBeanManager", NormalBeanManager.class);
        Object normalBean1 = normalBeanManager.process("wesleyOne");
        Object normalBean2 = normalBeanManager.process("wesleyOne");
        // 不同对象
        Assert.assertNotEquals(normalBean1, normalBean2);
    }
}
