package io.github.wesleyone.spring.core.c1.c4;

/**
 * 构造函数参数索引
 * @author http://wesleyone.github.io/
 */
public class DIConstructorIndexBean {

    private final int years;

    private final String ultimateAnswer;

    public DIConstructorIndexBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }

    public int getYears() {
        return years;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }
}
