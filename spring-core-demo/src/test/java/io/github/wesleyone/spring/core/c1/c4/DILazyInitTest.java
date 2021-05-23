package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 懒加载测试
 * @author http://wesleyone.github.io/
 */
public class DILazyInitTest {

    @Test
    public void lazyInitTest() throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_4_DI_lazy_init.xml");
        TimeUnit.SECONDS.sleep(1);
        Date currentTime = new Date();
        TimeUnit.SECONDS.sleep(1);
        LazyInitBean noLazyInit = context.getBean("noLazyInit", LazyInitBean.class);
        LazyInitBean lazyInit = context.getBean("lazyInit", LazyInitBean.class);
        LazyInitBean lazyInitButDependsOnByNoLazyInit = context.getBean("lazyInitButDependsOnByNoLazyInit", LazyInitBean.class);
        Assert.assertTrue(currentTime.after(noLazyInit.getCreateTime()));
        Assert.assertTrue(currentTime.before(lazyInit.getCreateTime()));
        Assert.assertTrue(currentTime.after(lazyInitButDependsOnByNoLazyInit.getCreateTime()));
    }
}
