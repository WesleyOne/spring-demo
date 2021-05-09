package io.github.wesleyone.spring.core.c1.c8;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * BeanPostProcessor 用于bean对象初始化前后的自定义
 * 当前处理器功能是修改<code>NormalBean<code/>、<code>NotEligibleBean</code>的<code>name</code>属性值，
 * 在原有值后面追加<code>_after</code>
 *
 * 但是<code>NotEligibleBean</code>在当前处理器前实例化了，不会被当前处理器处理。
 *
 * @author http://wesleyone.github.io/
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

    /**
     * 当前注入不符合规范，会提示以下报错
     * Bean 'notEligibleBean' of type [io.github.wesleyone.spring.core.c1.c8.NotEligibleBean] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
     */
    @Resource
    private NotEligibleBean notEligibleBean;

    private boolean notEligibleBeanPostBefore = false;
    private boolean notEligibleBeanPostAfter = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.equals("notEligibleBean",beanName)) {
            notEligibleBeanPostBefore = true;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("Bean '" + beanName + "' created : " + bean.toString());
        if (bean instanceof NormalBean) {
            NormalBean normalBean = (NormalBean) bean;
            normalBean.setName(normalBean.getName()+"_after");
        } else if (bean instanceof NotEligibleBean) {
            NotEligibleBean notEligibleBean = (NotEligibleBean) bean;
            notEligibleBean.setName(notEligibleBean.getName()+"_after");
        }

        if (Objects.equals("notEligibleBean",beanName)) {
            notEligibleBeanPostAfter = true;
        }
        return bean;
    }
}
