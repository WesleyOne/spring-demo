package io.github.wesleyone.spring.core.c5.c81;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class ExposeProxyAopTest {

    @Test
    public void aopTest() {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(ExposeProxyAopConfig.class, MyAspect.class,MyTargetObject.class);
        MyTargetObject myTargetObject = applicationContext.getBean(MyTargetObject.class);

        String ret = myTargetObject.doFoo();
        Assert.assertEquals("bar ok.foo ok.", ret);

        //
        String ret2 = myTargetObject.doFooWithoutExpose();
        Assert.assertEquals("barfoo ok.", ret2);
    }
}
