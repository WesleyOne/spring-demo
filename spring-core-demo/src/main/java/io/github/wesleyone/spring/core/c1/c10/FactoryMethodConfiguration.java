package io.github.wesleyone.spring.core.c1.c10;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author http://wesleyone.github.io/
 */
@Configuration
public class FactoryMethodConfiguration {

    @Bean
    public NormalBeanTwo normalBeanTwo() {
        NormalBeanTwo normalBeanTwo = new NormalBeanTwo();
        normalBeanTwo.setName("wesleyOne");
        return normalBeanTwo;
    }

    @Bean
    public NormalBeanOne normalBeanOne() {
        NormalBeanOne normalBeanOne = new NormalBeanOne();
        normalBeanOne.setNormalBeanTwo(normalBeanTwo());
        return normalBeanOne;
    }
}
