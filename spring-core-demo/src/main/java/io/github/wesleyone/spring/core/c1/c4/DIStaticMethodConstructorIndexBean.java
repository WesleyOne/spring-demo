package io.github.wesleyone.spring.core.c1.c4;

/**
 * 静态工厂方法构造函数参数索引
 * @author http://wesleyone.github.io/
 */
public class DIStaticMethodConstructorIndexBean {

    private final int years;

    private final String ultimateAnswer;

    private DIStaticMethodConstructorIndexBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }

    public static DIStaticMethodConstructorIndexBean createInstance(int years, String ultimateAnswer) {
        return new DIStaticMethodConstructorIndexBean(years, ultimateAnswer);
    }

    public int getYears() {
        return years;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }
}
