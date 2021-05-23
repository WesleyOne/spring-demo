package io.github.wesleyone.spring.core.c5.c4;

import org.springframework.stereotype.Component;

/**
 * 目标对象
 * @author http://wesleyone.github.io/
 */
@Component
public class MyTargetObjectImpl implements TargetObjectInterface {

    @Override
    public String say(String name) {
        return "Hello,"+name;
    }

    @Override
    public String say(Throwable t) throws Throwable {
        throw t;
    }
}
