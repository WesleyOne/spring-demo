package io.github.wesleyone.spring.core.c5.c8;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class MyCglibAopTest {

    @Test
    public void aopTest() {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext("io.github.wesleyone.spring.core.c5.c8");
        MyTargetObjectImpl myTargetObjectImpl = applicationContext.getBean(MyTargetObjectImpl.class);
        String sayReturn = myTargetObjectImpl.say("wesleyOne");
        Assert.assertEquals("Hello,wesleyOne bro.", sayReturn);
        // 使用CGLIB，final无法建议方法，因为无法在运行时生成的子类中覆盖方法
        String sayFinalReturn = myTargetObjectImpl.sayFinal("wesleyOne");
        Assert.assertEquals("Hello,wesleyOne", sayFinalReturn);
    }
}
