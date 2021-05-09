package io.github.wesleyone.spring.core.c1.c4;

/**
 * 静态工厂方法构造函数参数名称
 * @author http://wesleyone.github.io/
 */
public class DIStaticMethodConstructorNameBean {

    private final int years;

    private final String ultimateAnswer;

    private DIStaticMethodConstructorNameBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }

    public static DIStaticMethodConstructorNameBean createInstance(int years, String ultimateAnswer) {
        return new DIStaticMethodConstructorNameBean(years, ultimateAnswer);
    }

    public int getYears() {
        return years;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }
}
