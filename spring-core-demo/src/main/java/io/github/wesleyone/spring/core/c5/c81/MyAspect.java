package io.github.wesleyone.spring.core.c5.c81;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
    @Pointcut("within(io.github.wesleyone.spring.core.c5.c81.MyTargetObject) && execution(* do*(..))")
    private void myPointcut() {}

    /**
     * 通过 @Around注解声明【环绕通知】<br/>
     * 注解内value属性是关联切入点，也可以直接写切入点表达式<br/>
     * @param pjp   第一个参数必须是ProceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("myPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 返回方法参数
        Object[] args = pjp.getArgs();

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

        retVal += " ok.";
        System.out.println("doAround change retVal:" + retVal);
        return retVal;
    }

}
