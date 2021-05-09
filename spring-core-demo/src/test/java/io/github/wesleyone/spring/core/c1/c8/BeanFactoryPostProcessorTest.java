package io.github.wesleyone.spring.core.c1.c8;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;

/**
 * @author http://wesleyone.github.io/
 */
public class BeanFactoryPostProcessorTest {

    @Test
    public void beanFactoryPostProcessorTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_8_2_BeanFactoryPostProcessor.xml");
        NormalBean normalBean = context.getBean("normalBean", NormalBean.class);
        System.out.println("normalBean.getName():"+normalBean.getName());
        Assert.assertEquals("wesleyOne_after",normalBean.getName());
    }
}
