package io.github.wesleyone.spring.core.c1.c4;

/**
 * 基于Setter的依赖注入
 * @author http://wesleyone.github.io/
 */
public class DISetterBean {

    private int years;

    private String ultimateAnswer;

    public DISetterBean() {
    }

    public void setYears(int years) {
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
