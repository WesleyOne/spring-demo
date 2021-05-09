package io.github.wesleyone.spring.core.c1.c4;

/**
 * @author http://wesleyone.github.io/
 */
public class OuterBean {

    private InnerBean target;

    public InnerBean getTarget() {
        return target;
    }

    public void setTarget(InnerBean target) {
        this.target = target;
    }

    /**
     * 内部类
     */
    static class InnerBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
