package io.github.wesleyone.spring.core.c5.c9;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 * 编程方式创建@AspectJ代理
 *
 * @author http://wesleyone.github.io/
 */
public class AspectJProxyFactoryTest {

    @Test
    public void aopTest() {
        MyTargetObject myTargetObject = new MyTargetObject();
        // 创建AspectJProxyFactory，用来创建目标对象的代理对象
        AspectJProxyFactory factory = new AspectJProxyFactory(myTargetObject);

        // 添加切面，这个切面类必须有@Aspect注解
        // 可以添加多个切面你
        factory.addAspect(MyAspect.class);

        // 获取代理对象
        MyTargetObject myTargetObjectProxy = factory.getProxy();

        String ret = myTargetObjectProxy.doBar();
        Assert.assertEquals("bar ok.", ret);
    }
}
