package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author http://wesleyone.github.io/
 */
public class DITest {

    @Test
    public void DIConstructorTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_1_基于构造函数DI.xml");

        DIConstructorTypeBean constructorTypeBean = context.getBean("DIConstructorTypeBean", DIConstructorTypeBean.class);
        Assert.assertEquals(constructorTypeBean.getUltimateAnswer(), "42");

        DIConstructorIndexBean constructorIndexBean = context.getBean("DIConstructorIndexBean", DIConstructorIndexBean.class);
        Assert.assertEquals(constructorIndexBean.getUltimateAnswer(), "43");

        DIConstructorNameBean constructorNameBean = context.getBean("DIConstructorNameBean", DIConstructorNameBean.class);
        Assert.assertEquals(constructorNameBean.getUltimateAnswer(), "44");

        DIStaticMethodConstructorTypeBean staticMethodConstructorTypeBean = context.getBean("DIStaticMethodConstructorTypeBean", DIStaticMethodConstructorTypeBean.class);
        Assert.assertEquals(staticMethodConstructorTypeBean.getUltimateAnswer(), "45");

        DIStaticMethodConstructorIndexBean staticMethodConstructorIndexBean = context.getBean("DIStaticMethodConstructorIndexBean", DIStaticMethodConstructorIndexBean.class);
        Assert.assertEquals(staticMethodConstructorIndexBean.getUltimateAnswer(), "46");

        DIStaticMethodConstructorNameBean staticMethodConstructorNameBean = context.getBean("DIStaticMethodConstructorNameBean", DIStaticMethodConstructorNameBean.class);
        Assert.assertEquals(staticMethodConstructorNameBean.getUltimateAnswer(), "47");
    }

    @Test
    public void DISetterTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_1_基于Setter的DI.xml");

        DISetterBean setterBean = context.getBean("DISetterBean", DISetterBean.class);
        Assert.assertEquals(setterBean.getYears(), 7500000);
        Assert.assertEquals(setterBean.getUltimateAnswer(), "42");
    }


    @Test
    public void DIConstructorAndSetterTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_1_基于Setter的DI.xml");

        DIConstructorIndexAndSetterBean constructorIndexAndSetterBean = context.getBean("DIConstructorIndexAndSetterBean", DIConstructorIndexAndSetterBean.class);
        Assert.assertEquals(constructorIndexAndSetterBean.getYears(), 7500000);
        Assert.assertEquals(constructorIndexAndSetterBean.getUltimateAnswer(), "42");
    }

}
