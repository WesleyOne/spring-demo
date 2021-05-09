package io.github.wesleyone.spring.core.c1.c5;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author http://wesleyone.github.io/
 */
public class LifecycleCallbacksTest {

    @Test
    public void lifecycleCallbacksTest() throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("1_6_1_生命周期回调顺序.xml");
        // 在非Web应用程序中正常关闭Spring IoC容器
        context.registerShutdownHook();
        LifecycleCallbacksBean lifecycleCallbacksBean = context.getBean("lifecycleCallbacksBean", LifecycleCallbacksBean.class);
//        TimeUnit.SECONDS.sleep(1);
    }
}
