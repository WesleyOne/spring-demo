package io.github.wesleyone.spring.core.c1.c4;

/**
 * @author http://wesleyone.github.io/
 */
public class AutowireBean {

    private NormalBean normalBean;

    public void setNormalBean(NormalBean normalBean) {
        this.normalBean = normalBean;
    }

    public NormalBean getNormalBean() {
        return normalBean;
    }
}
