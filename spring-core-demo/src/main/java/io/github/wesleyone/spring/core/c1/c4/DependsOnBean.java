package io.github.wesleyone.spring.core.c1.c4;

/**
 * @author http://wesleyone.github.io/
 */
public class DependsOnBean {

    private final int index;

    public DependsOnBean() {
        String dependsOnIndex = System.getProperty("DependsOnIndexTag");
        if (dependsOnIndex == null) {
            index = 0;
            System.setProperty("DependsOnIndexTag","");
        } else {
            index = 1;
        }
    }

    public int getIndex() {
        return index;
    }

}
