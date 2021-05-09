package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 显示依赖设置
 * @author http://wesleyone.github.io/
 */
public class DIDependsOnTest {

    @Test
    public void dependsOnTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_3_DI_depends_on.xml");
        DependsOnBean dependsOn_0 = context.getBean("dependsOn_0", DependsOnBean.class);
        DependsOnBean dependsOn_1 = context.getBean("dependsOn_1", DependsOnBean.class);
        Assert.assertEquals(dependsOn_0.getIndex(), 0);
        Assert.assertEquals(dependsOn_1.getIndex(), 1);
    }
}
