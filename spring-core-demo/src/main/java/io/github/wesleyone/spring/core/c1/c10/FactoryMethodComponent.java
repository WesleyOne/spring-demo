package io.github.wesleyone.spring.core.c1.c10;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 通过组件定义bean元数据
 *
 * @author http://wesleyone.github.io/
 */
@Component
public class FactoryMethodComponent {

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
