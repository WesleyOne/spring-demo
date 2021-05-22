package io.github.wesleyone.spring.core.c5.c8;

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
    public final String sayFinal(String name) {
        return "Hello,"+name;
    }
}
