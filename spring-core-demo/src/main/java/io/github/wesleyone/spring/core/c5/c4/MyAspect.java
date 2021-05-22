package io.github.wesleyone.spring.core.c5.c4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *  通过@Aspect注解声明切面<br/>
 *  <br/>
 *  AopConfig配置类启用@AspectJ支持后，Spring会自动检测上下文中使用@Aspect注解的bean，用于配置SpringAop。<br/>
 *  其中@Component作用是通过组件扫描添加到上下文中。<br/>
 *
 * @author http://wesleyone.github.io/
 */
@Component
@Aspect
public class MyAspect {

//    ====================切入点====================
    /**
     * 通过@Pointcut声明切入点<br/>
     * 通过切入点表达式匹配SpringBean上的方法（即连接点）<br/>
     * 更多表达式指示符见文档<br/>
     */
    @Pointcut("within(io.github.wesleyone.spring.core.c5.c4.MyTargetObjectImpl) && execution(* say(..))")
    private void myPointcut() {}

//    =====================通知Advice=================
    /**
     * 通过@Before注解声明【前置通知】<br/>
     * 注解内value属性是关联切入点，也可以直接写切入点表达式<br/>
     */
    @Before("io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut()")
    public void doBefore() {
        System.out.println("doBefore");
    }

    /**
     * 通过@Before注解声明【前置通知】,并获取方法参数<br/>
     */
    @Before("io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut() && args(name)")
    public void doBeforeWithParam(String name) {
        System.out.println("doBefore param:" + name);
    }

    /**
     * 通过@AfterReturning注解声明【正常返回后置通知】<br/>
     * 注解内pointcut属性是关联切入点，也可以直接写切入点表达式<br/>
     * 注解内returning属性声明返回对象名称<br/>
     */
    @AfterReturning(
            pointcut = "io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut()",
            returning = "retVal")
    public void doAfterReturning(Object retVal) {
        System.out.println("doAfterReturning return:" + retVal);
    }

    /**
     * 通过@AfterThrowing注解声明【异常返回后置通知】<br/>
     * 注解内pointcut属性是关联切入点，也可以直接写切入点表达式<br/>
     * 注解内throwing属性声明返回异常名称<br/>
     */
    @AfterThrowing(
            pointcut="io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut()",
            throwing="ex")
    public void doAfterThrowing(Throwable ex) {
        System.out.println("doAfterThrowing");
        ex.printStackTrace();
    }

    /**
     * 通过@Before注解声明【Finally后置通知】<br/>
     * 注解内value属性是关联切入点，也可以直接写切入点表达式<br/>
     */
    @After("io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut()")
    public void doAfterFinally() {
        System.out.println("doAfterFinally");
    }

    /**
     * 通过 @Around注解声明【环绕通知】<br/>
     * 注解内value属性是关联切入点，也可以直接写切入点表达式<br/>
     * @param pjp   第一个参数必须是ProceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("io.github.wesleyone.spring.core.c5.c4.MyAspect.myPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 返回方法参数
        Object[] args = pjp.getArgs();
        System.out.println("doAround Args:" + Arrays.toString(args));

        /*
         * getThis()高危操作：
         * 例如如果切入点匹配的方法包括Object的方法
         * 那么获取代理对象后进行`toString`操作会触发AOP
         * 导致死循环栈溢出
         *
         * 建议：
         * 1.切入点的匹配范围要最小可用原则
         * 2.通知方法内尽量不要调用连接点方法
         *
         * 为啥aopObject == targetObject？
         */
        // 返回代理对象
        Object aopObject = pjp.getThis();
        System.out.println("doAround aopObject:" + aopObject);
        // 返回目标对象。
        Object targetObject = pjp.getTarget();
        System.out.println("doAround targetObject:" + targetObject);

        // 执行拦截链的后续通知方法，直到执行目标对象方法
        // 另外proceed方法可以多次调用，使用场景比如异常重试
        Object retVal = pjp.proceed(args);
        System.out.println("doAround retVal:" + retVal);

        retVal += " bro.";
        System.out.println("doAround change retVal:" + retVal);
        return retVal;
    }

}
