package io.github.wesleyone.spring.core.c1.c7;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class BeanDefinitionInheritanceTest {

    @Test
    public void beanDefinitionInheritanceTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_7_Bean定义继承.xml");
        ChildBean childBean = context.getBean("childBean", ChildBean.class);
        Assert.assertEquals(childBean.getName(), "child");

        ChildAbstractBean childAbstractBean = context.getBean("childAbstractBean", ChildAbstractBean.class);
        Assert.assertEquals(childAbstractBean.getName(), "childAbstract");
    }
}
