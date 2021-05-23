package io.github.wesleyone.spring.core.c1.c2;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

/**
 * groovy相关代码需要添加maven依赖：
 * <code>
 *     <dependency>
 *       <groupId>org.codehaus.groovy</groupId>
 *       <artifactId>groovy-all</artifactId>
 *       <version>3.0.7</version>
 *     </dependency>
 * <code/>
 *
 * @author http://wesleyone.github.io/
 */
public class ContainerTest {

    @Test
    public void usingContainerWithXmlTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_2_2_实例化容器.xml");

        // 检索配置的实例
        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);

        // 使用配置实例
        String name = simpleUser.getName();

        Assert.assertEquals(name, "wesleyOne");
    }

//    @Test
//    public void usingContainerWithGroovyTest() {
//        // 创建和配置bean
//        ApplicationContext context = new GenericGroovyApplicationContext("1_2_2_实例化容器.groovy");
//
//        // 检索配置的实例
//        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);
//
//        // 使用配置实例
//        String name = simpleUser.getName();
//
//        Assert.assertEquals(name, "wesleyOne");
//    }

    @Test
    public void usingContainerWithXmlReaderTest() {

        GenericApplicationContext context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions("1_2_2_实例化容器.xml");
        context.refresh();

        // 检索配置的实例
        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);

        // 使用配置实例
        String name = simpleUser.getName();

        Assert.assertEquals(name, "wesleyOne");
    }

//    @Test
//    public void usingContainerWithGroovyReaderTest() {
//        // 创建和配置bean
//        GenericApplicationContext context = new GenericApplicationContext();
//        new GroovyBeanDefinitionReader(context).loadBeanDefinitions("1_2_2_实例化容器.groovy");
//        context.refresh();
//        // 检索配置的实例
//        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);
//
//        // 使用配置实例
//        String name = simpleUser.getName();
//
//        Assert.assertEquals(name, "wesleyOne");
//    }

}
