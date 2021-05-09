package io.github.wesleyone.spring.core.c1.c4;

import java.util.Date;

/**
 * @author http://wesleyone.github.io/
 */
public class LazyInitBean {

    private final Date createTime;

    public LazyInitBean() {
        createTime = new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }
}
