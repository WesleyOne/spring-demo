package io.github.wesleyone.spring.core.c1.c4;

/**
 * 循环依赖
 * @author http://wesleyone.github.io/
 */
public class CircularDISetterABean {

    private CircularDISetterBBean b;

    public CircularDISetterBBean getB() {
        return b;
    }

    public void setB(CircularDISetterBBean b) {
        this.b = b;
    }
}
