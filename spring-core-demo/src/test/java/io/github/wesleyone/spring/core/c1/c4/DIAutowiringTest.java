package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class DIAutowiringTest {

    /**
     * 首先自动装配模式是byType
     * 由于AutowireBean的属性NormalBean类型从自动装配中排除
     * 所以找不到则不设置
     */
    @Test
    public void excludingAutowiringTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_5_取消自动装配.xml");
        AutowireBean autowireBean = context.getBean("autowireBean", AutowireBean.class);
        Assert.assertNull(autowireBean.getNormalBean());
    }
}
