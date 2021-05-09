package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class DIConfigurationTest {

    /**
     * 内部类
     */
    @Test
    public void innerBeanTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_2_DI配置细节.xml");

        OuterBean outerBean = context.getBean("outerBean", OuterBean.class);
        Assert.assertEquals(outerBean.getTarget().getName(),"wesleyOne");
    }
}
