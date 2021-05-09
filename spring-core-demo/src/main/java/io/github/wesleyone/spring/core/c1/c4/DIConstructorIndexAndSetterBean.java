package io.github.wesleyone.spring.core.c1.c4;

/**
 * 构造函数参数和Setter混合
 * @author http://wesleyone.github.io/
 */
public class DIConstructorIndexAndSetterBean {

    private final int years;

    private String ultimateAnswer;

    public DIConstructorIndexAndSetterBean(int years) {
        this.years = years;
    }

    public void setUltimateAnswer(String ultimateAnswer) {
        this.ultimateAnswer = ultimateAnswer;
    }

    public int getYears() {
        return years;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }
}
