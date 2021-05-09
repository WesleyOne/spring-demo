package io.github.wesleyone.spring.core.c1.c4;

/**
 * @author http://wesleyone.github.io/
 */
public abstract class NormalBeanManager {

    public Object process(String name) {
        NormalBean normalBean = createNormalBean();
        normalBean.setName(name);
        return normalBean;
    }

    protected abstract NormalBean createNormalBean();
}
