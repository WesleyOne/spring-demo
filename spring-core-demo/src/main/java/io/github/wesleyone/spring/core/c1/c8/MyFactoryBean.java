package io.github.wesleyone.spring.core.c1.c8;

import org.springframework.beans.factory.FactoryBean;

/**
 * FactoryBean用于初始化比较复杂的对象的实例化（使用xml写比较冗长，java代码方式更好表达）
 *
 * @author http://wesleyone.github.io/
 */
public class MyFactoryBean implements FactoryBean<NormalBean> {

    @Override
    public NormalBean getObject() throws Exception {
        NormalBean normalBean = new NormalBean();
        normalBean.setName("wesleyOne");
        return normalBean;
    }

    @Override
    public Class<?> getObjectType() {
        return NormalBean.class;
    }
}
