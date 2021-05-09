package io.github.wesleyone.spring.core.c1.c3;

import io.github.wesleyone.spring.core.c1.c2.SimpleUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class BeanTest {

    @Test
    public void registerBeanDefinitionTest() {

        GenericApplicationContext context = new GenericApplicationContext();

        // 手动注册Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(SimpleUser.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name","wesleyOne");
        beanDefinition.setPropertyValues(propertyValues);
        context.registerBeanDefinition("simpleUser", beanDefinition);

        context.refresh();

        // 检索配置的实例
        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);

        // 使用配置实例
        String name = simpleUser.getName();

        Assert.assertEquals(name, "wesleyOne");
    }

    @Test
    public void registerBeanDefinitionOverridingTest() {

        GenericApplicationContext context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions("1_2_2_实例化容器.xml");
        context.refresh();

        // 检索配置的实例
        SimpleUser simpleUser = context.getBean("simpleUser", SimpleUser.class);
        // 使用配置实例
        String name = simpleUser.getName();
        Assert.assertEquals(name, "wesleyOne");

        // 运行时手动注册Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(SimpleUser.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name","wesleyOneTest");
        beanDefinition.setPropertyValues(propertyValues);
        context.registerBeanDefinition("simpleUser", beanDefinition);

        // 检索配置的实例
        simpleUser = context.getBean("simpleUser", SimpleUser.class);
        // 使用配置实例
        name = simpleUser.getName();
        Assert.assertEquals(name, "wesleyOneTest");
    }
}
