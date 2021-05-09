package io.github.wesleyone.spring.core.c1.c8;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class FactoryBeanTest {

    @Test
    public void factoryBeanTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_8_3_FactoryBean.xml");
        NormalBean normalBean = context.getBean("myFactoryBean", NormalBean.class);
        MyFactoryBean myFactoryBean = context.getBean("&myFactoryBean", MyFactoryBean.class);
        System.out.println(normalBean);
        System.out.println(myFactoryBean);
        Assert.assertEquals(myFactoryBean.getObjectType(), normalBean.getClass());
    }
}
