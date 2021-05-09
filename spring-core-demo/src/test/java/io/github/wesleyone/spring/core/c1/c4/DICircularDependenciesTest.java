package io.github.wesleyone.spring.core.c1.c4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 循环依赖
 * @author http://wesleyone.github.io/
 */
public class DICircularDependenciesTest {


    @Test
    public void circularDependenciesTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("1_4_1_循环依赖.xml");
        // 两个单例的循环依赖
        CircularDISetterABean circularSingleA = context.getBean("circularDoubleSingleA", CircularDISetterABean.class);
        CircularDISetterBBean circularSingleB = context.getBean("circularDoubleSingleB", CircularDISetterBBean.class);
        Assert.assertEquals(circularSingleA.getB(), circularSingleB);
        Assert.assertEquals(circularSingleB.getA(), circularSingleA);

        // 一个单例，一个原型
        CircularDISetterABean singleOfCircularSingleAndPrototypeBean = context.getBean("singleOfCircularSingleAndPrototypeBean", CircularDISetterABean.class);
        CircularDISetterBBean prototypeOfCircularSingleAndPrototypeBean = context.getBean("prototypeOfCircularSingleAndPrototypeBean", CircularDISetterBBean.class);
        Assert.assertNotEquals(singleOfCircularSingleAndPrototypeBean.getB(), prototypeOfCircularSingleAndPrototypeBean);

    }

    /**
     * 传统代码的依赖循环设置流程
     */
    @Test
    public void normalCircularDependenciesTest() {
        // 实例化BeanA
        CircularDISetterABean a = new CircularDISetterABean();
        // 实例化BeanB
        CircularDISetterBBean b = new CircularDISetterBBean();
        // 实例b设置属性a，完成初始化
        b.setA(a);
        // 实例a设置属性b，完成初始化
        a.setB(b);
        Assert.assertEquals(a, b.getA());
        Assert.assertEquals(b, a.getB());
    }

    /**
     * 模仿Spring IoC源码处理依赖循环
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void normalCircularDependencies_like_spring() throws IllegalAccessException, InstantiationException {
        // 一级缓存，存单例名称-最终完整初始化后的对象
        Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
        // 二级缓存，存单例名称-仅完成实例化的对象
        Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
        // 三级缓存, 存单例名称-类的实例化方法
        Map<String, Supplier<?>> singletonFactories = new ConcurrentHashMap<>(16);
        // 缓存创建中标记，存单例名称
        Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap(16));

        /*
         * 实例化BeanA
         */

        // 获取名称为a的BeanA单例对象(getSingleton)
        // 查询一级缓存不存在"a"，但是没有创建中标记，则直接去创建
        Assert.assertFalse((singletonObjects.get("a") == null
                && singletonsCurrentlyInCreation.contains("a")));
        // 以上获取不到，所以要开始创建"a",先打个创建标记
        singletonsCurrentlyInCreation.add("a");
        // 实例化BeanA（instantiateBean）
        CircularDISetterABean a = CircularDISetterABean.class.newInstance();
        // 存入三级缓存，保存获取"a"实例的方法
        singletonFactories.put("a", () -> a);

        /*
         * 实例化BeanB
         */

        // 组装属性b（populateBean）
        // 获取名称为b的BeanB单例对象(getSingleton)
        // 查询一级缓存不存在"b"，但是没有创建中标记，则直接去创建
        Assert.assertFalse((singletonObjects.get("b") == null
                && singletonsCurrentlyInCreation.contains("b")));
        // 以上获取不到，所以要开始创建"b",先打个创建标记
        singletonsCurrentlyInCreation.add("b");
        // 实例化BeanB（instantiateBean）
        CircularDISetterBBean b = CircularDISetterBBean.class.newInstance();
        // 存入三级缓存，保存获取"b"实例的方法
        singletonFactories.put("b", () -> b);

        /*
         * 实例b设置属性a，完成b初始化
         */

        // 组装属性a（populateBean）
        // 获取名称为a的单例对象(getSingleton)
        // 查询一级缓存不存在"a"，并且有**创建中标记**，则继续查询下级缓存
        Assert.assertTrue((singletonObjects.get("a") == null && singletonsCurrentlyInCreation.contains("a")));
        // 继续查询二级缓存不存在"a"，则继续查询下级缓存
        Assert.assertTrue(earlySingletonObjects.get("a") == null);
        // 继续查询三级缓存，存在"a"，则使用缓存的方法获取"a"的实例化对象
        Assert.assertTrue(singletonFactories.get("a") != null);
        Object instantiateBeanOfA = singletonFactories.get("a").get();
        // 然后将"a"的实例添加到二级缓存，并从三级缓存中删除
        earlySingletonObjects.put("a",instantiateBeanOfA);
        singletonFactories.remove("a");
        // 设置b实例的属性a
        b.setA((CircularDISetterABean) instantiateBeanOfA);
        // 删除b创建中标记
        Assert.assertTrue(singletonsCurrentlyInCreation.remove("b"));
        // 然后将"b"的实例添加到一级缓存，并从二、三级缓存中删除
        singletonObjects.put("b", b);
        earlySingletonObjects.remove("b");// b实际没有二级缓存
        singletonFactories.remove("b");

        /*
         * 实例a设置属性b，完成a初始化
         */

        // 设置a实例的属性b
        a.setB((CircularDISetterBBean) singletonObjects.get("b"));
        // 删除a创建中标记
        Assert.assertTrue(singletonsCurrentlyInCreation.remove("a"));
        // 然后将"a"的实例添加到一级缓存，并从二级缓存中删除
        singletonObjects.put("a", a);
        earlySingletonObjects.remove("a");
        singletonFactories.remove("a");

        // 校验
        Assert.assertEquals(singletonObjects.get("a"), ((CircularDISetterBBean)singletonObjects.get("b")).getA());
        Assert.assertEquals(singletonObjects.get("b"), ((CircularDISetterABean)singletonObjects.get("a")).getB());
    }
}
