package io.github.wesleyone.spring.core.c1.c8;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;

/**
 * BeanFactoryPostProcessor 用于修改bean定义元数据
 *
 * 当前处理器是修改<code>normalBean</code>的元数据中的属性值<code>name</code>，追加<code>_after</code>
 *
 * @author http://wesleyone.github.io/
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition normalBeanDefinition = beanFactory.getBeanDefinition("normalBean");
        System.out.println("获取normalBean的所有属性配置:" + normalBeanDefinition.getPropertyValues());
        MutablePropertyValues propertyValues = normalBeanDefinition.getPropertyValues();
        PropertyValue name = propertyValues.getPropertyValue("name");
        TypedStringValue typedStringValue = (TypedStringValue) name.getValue();
        String value = typedStringValue.getValue();
        typedStringValue.setValue(value + "_after");
    }
}
