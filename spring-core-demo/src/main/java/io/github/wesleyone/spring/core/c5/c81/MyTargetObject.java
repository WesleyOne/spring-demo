package io.github.wesleyone.spring.core.c5.c81;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

/**
 * @author http://wesleyone.github.io/
 */
@Component
public class MyTargetObject {

    public String doFoo() {
        // 获取代理对象调用方法，如果被调用方法匹配切入点则也会被拦截处理
        String ret = ((MyTargetObject) AopContext.currentProxy()).doBar();
        return ret + "foo";
    }

    public String doBar() {
        return "bar";
    }

    public String doFooWithoutExpose() {
        // 是目标对象直接调用方法，不受拦截处理
        String ret = this.doBar();
        return ret + "foo";
    }
}
