package io.github.wesleyone.spring.core.c1.c8;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class BeanPostProcessorTest {

    @Test
    public void beanPostProcessorTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_8_1_BeanPostProcessor.xml");
        NormalBean normalBean = context.getBean("normalBean", NormalBean.class);
        System.out.println("normalBean.getName():"+normalBean.getName());
        Assert.assertEquals(normalBean.getName(),"wesleyOne_after");

        // Bean 'notEligibleBean' of type [xxx.NotEligibleBean] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
        // 由于notEligibleBean在后置处理器里被引用，提前实例化，当时后置处理器还没有完全加载到容器中，所以不会收到后置处理器处理
        NotEligibleBean notEligibleBean = context.getBean("notEligibleBean", NotEligibleBean.class);
        System.out.println("notEligibleBean.getName():"+notEligibleBean.getName());
        Assert.assertEquals(notEligibleBean.getName(),"wesleyOne");
    }
}
