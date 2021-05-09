package io.github.wesleyone.spring.core.c1.c4;

/**
 * 循环依赖
 * @author http://wesleyone.github.io/
 */
public class CircularDISetterBBean {

    private CircularDISetterABean a;

    public CircularDISetterABean getA() {
        return a;
    }

    public void setA(CircularDISetterABean a) {
        this.a = a;
    }
}
