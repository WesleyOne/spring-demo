package io.github.wesleyone.spring.core.c1.c4;

/**
 * 静态工厂方法构造函数参数名称
 * @author http://wesleyone.github.io/
 */
public class DIStaticMethodConstructorTypeBean {

    private final int years;

    private final String ultimateAnswer;

    private DIStaticMethodConstructorTypeBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }

    public static DIStaticMethodConstructorTypeBean createInstance(int years, String ultimateAnswer) {
        return new DIStaticMethodConstructorTypeBean(years, ultimateAnswer);
    }

    public int getYears() {
        return years;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }
}
