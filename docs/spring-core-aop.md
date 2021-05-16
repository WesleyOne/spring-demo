# Core Technologies

参考[Spring Core文档](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html)
> 版本：5.3.5

该文档主要介绍了**Spring的面向切面的编程（AOP）技术**。

## 5. 使用Spring进行AOP

AOP是面向切面编程。也是Spring的关键你能组件之一，提供强大的中间件解决方案。

> 使用AspectJ切入点的SpringAOP
> Spring可通过 基于Schema的方法 或者 @AspectJ注解方式，自定义编写切面的方法。

AOP在Spring框架中的用途：
- 提供声明式企业服务。例如声明式事务管理。
- 自定义切面，用AOP补充OOP的使用。

### 5.1. AOP概念

Spring AOP模型的基本概念：

- 切面（Aspect）：切面是由 _切入点_ 和 _通知_ 组成。可以认为`@Aspect`注解的类就是切面
- 切入点（Pointcut）：切入点是对 _连接点_ 进行拦截的条件定义。切入点表达式如何和连接点匹配是AOP的核心，Spring缺省使用AspectJ切入点语法。
- 通知（Advice）：通知是指拦截到连接点之后要执行的代码，包括了`around`、`before`和`after`等不同类型的通知。Spring AOP框架以拦截器来实现通知模型，并维护一个以连接点为中心的拦截器链。
- 连接点（Join point）：程序执行过程中明确的点，如方法的调用或特定的异常被抛出。
- 引入（Introduction）：允许向现有的类添加新方法新属性等。
- 目标对象（Target object）：被切面通知的对象，也就是真正的业务逻辑。
- AOP代理（AOP proxy）：使用的AOP框架实现，JDK动态代理或者CGLIB代理。
- 编织（Weaving）：链接切面和目标对象的过程。可以在编译期（例如使用AspectJ编译器）、类加载期或者运行时处理，SpringAOP采用的是运行时处理。


Spring AOP包含的通知类型:

- Before advice：在连接点之前运行的通知，但是它不能阻止流程继续执行（除非它引发异常）。
- After returning advice: 在连接点 _正常完成后_ 要运行的通知（方法返回而没有引发异常）。
- After throwing advice: 如果方法因引发异常而退出，则要运行的通知。
- After (finally) advice: 无论连接点退出的方式如何（正常或特殊返回），都应运行通知。
- Around advice: 环绕连接点的通知。这是最有力的建议。例如方法调用，环绕通知可以在方法调用之前和之后执行自定义行为。它还负责选择是返回连接点、返回其自身的返回值、引发异常来简化通知的方法执行。

### 5.2. Spring AOP能力和目标

Spring AOP是用纯Java实现的。不需要特殊的编译过程。Spring AOP不需要控制类加载器的层次结构，因此适合在Servlet容器或应用程序服务器中使用。

Spring AOP当前仅支持方法调用的连接点（通知在Spring Bean上执行方法）。

Spring AOP的AOP方法不同于大多数其他AOP框架。目的不是提供最完整的AOP实现（尽管Spring AOP相当强大）。相反，其目的是在AOP实现和Spring IoC之间提供紧密的集成，以帮助解决企业应用程序中的常见问题。

### 5.3. AOP代理

Spring AOP默认将标准JDK动态代理用于AOP代理。这使得可以代理任何接口（或一组接口）。

Spring AOP也可以使用CGLIB代理。它是代理类的，而不是接口。默认情况下，如果业务对象未实现接口，则使用CGLIB。由于对接口而不是对类进行编程是一种好习惯，因此业务类通常实现一个或多个业务接口。在那些需要通知在接口上未声明的方法或需要将代理对象作为具体类型传递给方法的情况下（在极少数情况下），可以强制使用CGLIB。

### 5.4. @AspectJ支持

@AspectJ是使用注解方式声明切面。@AspectJ注解是AspectJ项目在AspectJ 5发行版中引入的。Spring使用AspectJ提供的用于切入点解析和匹配的库来解释与AspectJ 5相同的注释。但是，AOP运行时仍然是纯Spring AOP，并且不依赖于AspectJ编译器或编织器。

#### 5.4.1 启用@AspectJ支持

需要添加`aspectjweaver.jar`依赖。

##### 通过Java配置启用@AspectJ支持
```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

}
```

##### 通过XML配置启用@AspectJ支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <aop:aspectj-autoproxy/>

</beans>
```

#### 5.4.2. 声明切面

启用@AspectJ支持后，Spring会自动检测在应用程序上下文中使用@AspectJ注解的bean，并用于配置Spring AOP。

```dtd
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
public class NotVeryUsefulAspect {

}
```

> 在Spring AOP中，切面本身不能做其他切面的目标对象。


#### 5.4.3. 声明切入点

切入点用于确认感兴趣的连接点。Spring AOP仅支持Spring Bean的方法执行连接点，因此您可以将切入点视为与Spring Bean上的方法执行相匹配的切入点。切入点声明有两个部分：一个切入点方法声明，以及一个切入点表达式，该切入点表达式精确地确定我们感兴趣的方法。

```dtd
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
public class NotVeryUsefulAspect {

    @Pointcut("execution(* transfer(..))") // 切入点表达式
    private void anyOldTransfer() {} // 切入点方法声明
}
```

##### SpringAOP支持的切入点指示符：

- execution：用于匹配方法执行的连接点。
- within：用于匹配指定类型内的方法执行。
- this：用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；
- target：用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配；
- args：用于匹配当前执行的方法传入的参数为指定类型的执行方法；
- @target：用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解；
- @args：用于匹配当前执行的方法传入的参数持有指定注解的执行；
- @within：用于匹配所以持有指定注解类型内的方法；
- @annotation：用于匹配当前执行方法持有指定注解的方法；

> AspectJ支持的其他切入点指示符，SpringAop不支持，会抛异常。

SpringAOP特有的指示符，用于匹配beanName符合的bean的方法执行：
```dtd
bean(idOrNameOfBean)
```

##### 组合切入点表达式

可以使用`&&`、`||`、`!`表达式

```dtd
@Pointcut("execution(public * *(..))") // 定义匹配的方法
private void anyPublicOperation() {} 

@Pointcut("within(com.xyz.myapp.trading..*)") // 定义匹配的类型中所有方法
private void inTrading() {} 

@Pointcut("anyPublicOperation() && inTrading()") // 定义以上两种规则同时匹配的所有方法
private void tradingOperation() {} 
```

##### 共享通用切入点定义

定义通用切面类来定义通用切入点

##### 例子

所有公共方法:
```dtd
    execution(public * *(..))
```

方法名称以set开头的所有方法
```dtd
    execution(* set*(..))
```

AccountService接口定义的所有方法：
```dtd
    execution(* com.xyz.service.AccountService.*(..))
```

该包路径下的所有方法：
```dtd
    execution(* com.xyz.service.*.*(..))
```

该包路径及下级路径下的的所有方法：
```dtd
    execution(* com.xyz.service..*.*(..))
```

该包路径下的所有切入点（在SpringAOP中仅是方法执行）
```dtd
    within(com.xyz.service.*)
```

该包路径及下级路径的所有切入点（在SpringAOP中仅是方法执行）
```dtd
    within(com.xyz.service..*)
```

实现接口的代理对象的所有切入点（在SpringAOP中仅是方法执行），通常是以绑定形式使用。
```dtd
    this(com.xyz.service.AccountService)
```

实现接口的目标对象的所有切入点（在SpringAOP中仅是方法执行），通常是以绑定形式使用。
```dtd
    target(com.xyz.service.AccountService)
```

匹配指定运行时方法参数数量和类型的所有切入点（在SpringAOP中仅是方法执行），通常是以绑定形式使用。
```dtd
    args(java.io.Serializable)
```

包含注解的目标对象的所有切入点（在SpringAOP中仅是方法执行）
```dtd
    @target(org.springframework.transaction.annotation.Transactional)
```

目标对象的类型有指定注解的所有切入点（在SpringAOP中仅是方法执行）
```dtd
@within(org.springframework.transaction.annotation.Transactional)
```

执行难方法上有指定注解的所有切入点（在SpringAOP中仅是方法执行）
```dtd
@annotation(org.springframework.transaction.annotation.Transactional)
```

匹配指定运行时方法参数数量和带有指定注解的类型的所有切入点（在SpringAOP中仅是方法执行），通常是以绑定形式使用。
```dtd
@args(com.xyz.security.Classified)
```

匹配Spring BeanName的对象的所有切入点（在SpringAOP中仅是方法执行）
```dtd
bean(tradeService)
```

通配符匹配Spring BeanName的对象的所有切入点（在SpringAOP中仅是方法执行）
```dtd
bean(*Service)
```

##### 编写好的切入点

- 指定种类（方法、属性）的指示符：execution, get, set, call, and handler.
- 指定范围的指示符号：within and withincode
- 基于上下文的指示符：this, target, and @annotation

尽量缩小切入点匹配搜索范围。
优先使用范围搜索`within`，范围匹配比较快。


#### 5.4.4. 声明通知

通知与切入点表达式关联，并且在切入点匹配的方法执行之前，之后或环绕运行。切入点表达式可以是对命名切入点的简单引用，也可以是直接的切入点表达式。

- 

```dtd
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class Example {

    // 命名切入点的引用
    // 前置通知
    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }
    
    // 直接表达式
    // 前置通知
    @Before("execution(* com.xyz.myapp.dao.*.*(..))")
    public void doAccessCheck() {
        // ...
    }

    // 前置通知，并获取方法参数
    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
    public void validateAccount(Account account) {
        // ...
    }

    // 效果同上。切入点声明和通知分开
    @Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
    private void accountDataAccessOperation(Account account) {}
    @Before("accountDataAccessOperation(account)")
    public void validateAccount(Account account) {
        // ...
    }

    // 正常返回后通知
    @AfterReturning("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }

    // 正常返回后通知，并且获取返回值
    @AfterReturning(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
        returning="retVal")
    public void doAccessCheck(Object retVal) {
        // ...
    }
        
    // 异常返回后通知
    @AfterThrowing("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doRecoveryActions() {
        // ...
    }

    // 异常返回后通知，并且获取【目标方法】的异常（不会包括@After/@AfterReturning通知方法的异常）
    @AfterThrowing(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
        throwing="ex")
    public void doRecoveryActions(DataAccessException ex) {
        // ...
    }

    // after(finally)通知，在正常返回或者异常返回通知后，都会执行。通常用来释放资源。
    @After("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }

    // 环绕通知。第一个参数必须是ProceedingJoinPoint类型，通过该参数可以获取相关参数
    // getArgs()：返回方法参数
    // getThis()：返回代理对象
    // getTarget()：返回目标对象。
    // getSignature()：返回建议使用的方法的描述。
    // toString()：打印有关所建议方法的有用描述。
    @Around("com.xyz.myapp.CommonPointcuts.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
     }
}
```


切面通过`org.springframework.core.Ordered`接口和`@Order`注解，当处理相同切入点时，可以确定优先级。
相同切面内的存在多个相同通知时，顺序不能控制，建议写到同个通知，或者分开多个切面。

```dtd
# Spring5.x
@Around
@Before
执行目标对象方法
@AfterReturning/@AfterThrowing
@After
```

```dtd
@Before // @Order(1)
@Before // @Order(2)
执行目标对象方法
@After // @Order(2)
@After // @Order(1)
```

#### 5.4.5. Introductions引入

```dtd
@Aspect
public class UsageTracking {

    @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
    public static UsageTracked mixin;

    @Before("com.xyz.myapp.CommonPointcuts.businessService() && this(usageTracked)")
    public void recordUsage(UsageTracked usageTracked) {
        usageTracked.incrementUseCount();
    }

}
```

#### 5.4.6. 切面实例化模型

Spring支持AspectJ的perthis和pertarget实例化模型。
切面实例是在服务对象上首次调用方法时创建的。当服务对象超出范围时，切面将超出范围。在创建切面实例之前，其中的任何通知都不会运行。
创建切面实例后，在其中声明的通知将在匹配的连接点处运行，但仅当服务对象是与此切面相关联的对象时才运行。
perthis子句的作用是为执行业务服务的每个唯一服务对象（每个this与切入点表达式匹配的连接点绑定到的唯一对象）创建一个切面实例。
pertarget在匹配的连接点时，每个独立目标对象创建一个切面实例。

```dtd
@Aspect("perthis(com.xyz.myapp.CommonPointcuts.businessService())")
public class MyAspect {

    private int someState;

    @Before("com.xyz.myapp.CommonPointcuts.businessService()")
    public void recordServiceUsage() {
        // ...
    }
}
```

### 5.5. 基于XML-schema的AOP支持

略

### 5.6. 选择AOP声明样式

XML方式的缺点：1.没有完全解决需求的实现封装在一个地方；2.切入点表达式受限制，无法组合XML声明的命名切入点。

### 5.7. 混合切面类型

通过使用自动代理支持，XML-schema定义的<aop:aspect>切面，<aop:advisor>声明的Spring切面，
甚至是同一配置中的其他样式的代理和拦截器，完全可以混合使用@AspectJ样式的切面。
所有这些都是通过使用相同的底层机制实现的，并且可以毫无困难地共存。

### 5.8. 代理机制

Spring AOP使用JDK动态代理或CGLIB创建给定目标对象的代理。JDK动态代理内置在JDK中，
而CGLIB是常见的开源类定义库（重新打包到spring-core）。

如果要代理的目标对象实现至少一个接口，则使用JDK动态代理，代理了目标类型的所有接口实现。
如果目标对象未实现任何接口，则将创建CGLIB代理。

可以强制使用CGLIB代理（代理为目标对象定义的每个方法，而不仅仅是接口方法实现），但是要考虑以下问题：
- `final`方法无法通知，因为无法在运行时生成的子类中重写方法；
- 从Spring 4.0开始，由于CGLIB代理实例是通过Objenesis创建的，因此不再调用代理对象的构造函数两次。仅当您的JVM不允许绕过构造函数时，您才可能从Spring的AOP支持中看到两次调用和相应的调试日志条目。

```java
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true) // 强制使用CGLIB代理
public class AopConfig {
    
}
```

#### 5.8.1 了解AOP代理

目标对象方法执行时，存在调用对象内其他方法时（自调用），调用其他方法是目标对象的调用，而不是代理对象的调用，从而导致其他方法不被代理处理。可通过以下开启为代理对象的调用：

```dtd
@Configuration
@EnableAspectJAutoProxy(exposeProxy=true)
public class AopConfig {

}
```

#### 5.9. 以编程方式创建@AspectJ代理

```dtd
// 创建一个针对目标对象生成代理对象的工厂
AspectJProxyFactory factory = new AspectJProxyFactory(targetObject);

// 添加切面，这个类必须有@AspectJ注解
// 可以多次调用该方法来添加多个切面
factory.addAspect(SecurityManager.class);

// 可以添加已存在的切面实例，这个对象类型必须有@AspectJ注解
factory.addAspect(usageTracker);

// 获取代理对象
MyInterfaceType proxy = factory.getProxy();
```

#### 5.10. 在Spring应用程序中使用AspectJ

略

#### 5.11. 更多资源

[Aspectj官网](https://www.eclipse.org/aspectj)
[指南](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)


## 6. Spring AOP API

上一章使用@AspectJ和基于模式的切面定义描述了Spring对AOP的支持。
在本章中，我们讨论了较低级别的Spring AOP API。
但是对于应用开发，建议将Spring AOP与AspectJ切入点一起使用，如上一章所述。

### 6.1. Spring中的切入点 API

Spring如何处理切入点概念。

#### 6.1.1.概念

Spring的切入点模型使切入点重复使用不受通知类型的影响。您可以使用相同的切入点来定位不同的通知。

`org.springframework.aop.Pointcut`接口是核心接口，用来将通知关联到指定的类和方法。
```java
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
```

`ClassFilter`接口用于将切入点限制为给定的一组目标类。如果该`matches()`方法始终返回true，则匹配所有目标类。
```java
public interface ClassFilter {

    boolean matches(Class clazz);
}
```

`MethodMatcher`接口的`matches(Method, Class)`方法用于测试此切入点是否与目标类上的给定方法匹配。创建AOP代理时可以执行此评估，以避免需要对每个方法调用进行测试。
当两个参数的`matches`方法返回true，且`isRuntime`方法返回true，则每次方法调用时都会执行三个参数的`matches`方法，切入点就可以在目标通知开始之前立即查看传递的方法参数。
```java
public interface MethodMatcher {

    boolean matches(Method m, Class<?> targetClass);

    boolean isRuntime();

    boolean matches(Method m, Class<?> targetClass, Object... args);
}
```
大多数`MethodMatcher`实现都是静态的，这意味着它们的`isRuntime()`方法将返回false。在这种情况下，matches永远不会调用三参数方法。

#### 6.1.2.切入点的操作

Spring支持切入点上的操作（特别是联合和相交）。

联合表示两个切入点其中一个匹配的方法。交集是指两个切入点都匹配的方法。联合通常更有用。您可以通过使用类中的静态方法`org.springframework.aop.support.Pointcuts`或使用同个包中的类 `ComposablePointcut`来编写切入点。但是，使用AspectJ切入点表达式通常是一种更简单的方法。

#### 6.1.3. AspectJ表达切入点

从2.0开始，Spring使用的最重要的切入点类型是`org.springframework.aop.aspectj.AspectJExpressionPointcut`。这是一个切入点，该切入点使用AspectJ提供的库来解析AspectJ切入点表达式字符串。

#### 6.1.4. 便捷切入点实现

- 静态切入点。推荐使用，首次调用方法时，Spring会评估一次静态切入点，之后无需每次方法调用再评估。
- 正则表达式切入点。正则表达式是指定静态切入点的一种方式。`org.springframework.aop.support.JdkRegexpMethodPointcut`通过JDK中的正则表达式支持实现切入点。或者使用`RegexpMethodPointcutAdvisor`指定正则表达式切面同时指定通知bean。
- 属性驱动的切入点。
- 动态切入点。相对静态切入点性能代价高，必须在每次方法调用时对方法进行评估，并且由于参数有所不同，因此无法缓存结果。
- 控制流切入点。`org.springframework.aop.support.ControlFlowPointcut`

#### 6.1.5. 切入点父类

Spring提供了有用的切入点父类，以帮助您实现自定义的切入点。

因为静态切入点最有用，所以可以继承`StaticMethodMatcherPointcut`，仅需要实现一个抽象方法（也可以覆盖其他方法来自定义行为）。以下示例展示：
```java
class TestStaticPointcut extends StaticMethodMatcherPointcut {

    public boolean matches(Method m, Class targetClass) {
        // return true if custom criteria match
    }
}
```
也有动态切入点的父类可用于自定义。

#### 6.1.6. 自定义切入点

由于SpringAop是Java类实现的，而不是语言特征，用户可以声明自定义切入点，无论是静态的还是动态的。但是推荐直接使用AspectJ切入点表达式。

### 6.1. Spring中的通知 API

#### 6.2.1. 通知的生命周期

每种通知都是Spring Bean。一个通知实例可以分享给所有被通知的对象或者对于每个被通知的对象都不同，分别对应与per-class或per-instance通知。

per-class通知较常用。不依赖代理对象的状态或添加新状态，仅作用与方法和参数。

per-instance通知可以使用引用（introductions），来支持mixins，可以将状态添加到代理对象中。

在同一AOP代理中混合使用共享通知和基于实例通知。

#### 6.2.2. Spring几种通知类型

Spring提供了几种通知类型，可以通过实现扩展以支持任意通知类型。

##### 环绕通知拦截处理

Spring使用拦截器接口`MethodInterceptor`实现环绕通知功能：
```java
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}
```
`invoke()`方法的`MethodInvocation`参数封装了被调用的方法、目标连接点、AOP代理以及方法的参数。该`invoke()`方法的返回结果是连接点的返回值。
使用案例如下：
```java
public class DebugInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: invocation=[" + invocation + "]");
        Object rval = invocation.proceed();
        System.out.println("Invocation returned");
        return rval;
    }
}
```
调用`MethodInvocation`的`proceed()`方法，沿着拦截器链向下，直到连接点。


##### 前置通知

前置通知不需要 `MethodInvocation`对象，因为仅在进入方法前才调用它。

```java
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method m, Object[] args, Object target) throws Throwable;
}
```
返回类型是`void`，不能更改返回值。如果执行出现异常，则停止进一步执行拦截器，异常会传播回拦截器链。

使用案例：
```java
public class CountingBeforeAdvice implements MethodBeforeAdvice {

    private int count;

    public void before(Method m, Object[] args, Object target) throws Throwable {
        ++count;
    }

    public int getCount() {
        return count;
    }
}
```

> 前置通知能和所有切入点一起使用

##### 异常通知

异常通知被调用，是由于连接点返回异常。

Spring提供了`org.springframework.aop.ThrowsAdvice`接口，但是里面没有任何方法，这个接口仅用来作为异常通知实现的标记。实现的方法格式有如下要求：
```
afterThrowing([Method, args, target], subclassOfThrowable)
```
最后一个异常类型参数必须有，其他参数根据需要设置。

案例：
```java
public class RemoteThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(RemoteException ex) throws Throwable {
        // Do something with remote exception
    }
}
```
```java
public class ServletThrowsAdviceWithArguments implements ThrowsAdvice {

    public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
        // Do something with all arguments
    }
}
```
下面案例展示一个异常处理类中可以包含多个异常通知：
```java
public static class CombinedThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(RemoteException ex) throws Throwable {
        // Do something with remote exception
    }

    public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
        // Do something with all arguments
    }
}
```

> 如果throws-advice方法本身引发异常，则它会覆盖原始异常。重写异常通常是RuntimeException，它与任何方法签名都兼容。但是如果throws-advice方法抛出一个已检查的异常，则它必须与目标方法的已声明异常匹配。不要抛出与目标方法签名不兼容的未声明检查异常！

> 异常通知能和所有切入点一起使用


##### 正常返回通知

实现该`org.springframework.aop.AfterReturningAdvice`接口
```java
public interface AfterReturningAdvice extends Advice {

    void afterReturning(Object returnValue, Method m, Object[] args, Object target)
            throws Throwable;
}
```
可以访问返回值（但不能修改）、调用方法、方法参数和目标对象。

案例：
```java
public class CountingAfterReturningAdvice implements AfterReturningAdvice {

    private int count;

    public void afterReturning(Object returnValue, Method m, Object[] args, Object target)
            throws Throwable {
        ++count;
    }

    public int getCount() {
        return count;
    }
}
```

如果通知中抛出异常，则会将异常抛出，而不是返回值。

> 异常通知能和所有切入点一起使用

##### 引用通知

暂略。

### 6.3. Spring中的Advisor API

Advisor是只包含一个通知对象和一个切入点表达式的切面。

`org.springframework.aop.support.DefaultPointcutAdvisor`是常用的Advisor类。他可以使用`MethodInterceptor`,`BeforeAdvice`或`ThrowsAdvice`。

### 6.4. 使用`ProxyFactoryBean`创建AOP代理

在Spring中创建AOP代理的基本方法是使用 `org.springframework.aop.framework.ProxyFactoryBean`。这样可以完全控制切入点、任何适用的有序的通知。

#### 6.4.1. 基础

ProxyFactoryBean是一个FactoryBean，通过`getObject()`方法获取目标对象的AOP代理对象。

使用一个ProxyFactoryBean或其他IoC-aware类创建AOP代理的最重要好处之一是，通知和切入点也可以由IoC管理。这是一项强大的功能，可以实现其他AOP框架难以实现的方法。例如，受益于依赖注入提供的所有可插入性，通知本身可以引用应用程序对象。

#### 6.4.2. JavaBean属性

和大多数FactoryBean实现相同，ProxyFactoryBean本身是JavaBean，其属性用于：

- 指定要代理的目标。
- 指定是否使用CGLIB。

一些属性继承自`org.springframework.aop.framework.ProxyConfig`(SpringAOP代理工厂的父类)。这些关键属性包括：
- proxyTargetClass：true则创建CGLIB代理
- optimize：控制是否将积极的优化应用于通过CGLIB创建的代理。除非您完全了解相关的AOP代理如何处理优化，否则不要随意使用此设置。当前仅用于CGLIB代理。它对JDK动态代理无效。
- frozen：如果代理配置为frozen，则不再允许对配置进行更改。此属性的默认值为 false，因此允许进行更改（例如添加其他建议）。
- exposeProxy：确定是否应公开当前代理到`ThreadLocal`，以便目标对象可以访问它。将`exposeProxy`属性设置为true，目标对象使用该`AopContext.currentProxy()`方法获取代理。

其他`ProxyFactoryBean`特定的属性：
- proxyInterfaces：接口名称字符串数组。如果未提供，则使用目标类的CGLIB代理（另请参见基于JDK和CGLIB的代理）。
- interceptorNames：要使用的Advisor、拦截器或其他通知名称的字符串数组。顺序很重要，先到先得。也就是说，列表中的第一个拦截器是第一个能够拦截调用的拦截器。
  名称是当前bean工厂中的bean名称，包括祖先工厂中的bean名称。您不能在此提及bean引用，因为这样做会导致 `ProxyFactoryBean`忽略通知的单例设置。
  您可以在拦截器名称后加上星号（*），这样做会使得名字以*号前开头的所有advisor Bean被使用。
- singleton：无论`getObject()`调用该方法的频率如何，工厂是否应返回单个对象。默认值为true。如果要使用有状态的通知（例如，有状态的mixins），设置false来使用原型通知。

#### 6.4.3. 基于JDK或者CGLIB的代理

如果代理的目标对象的类没有实现任何接口，则将创建基于CGLIB的代理。即使proxyTargetClass=false，也会创建基于CGLIB的代理。
通过设置interceptorNames指定拦截器列表。

如果目标类实现你一个（或多个）接口，则创建的代理类型取决于ProxyFactoryBean的配置。

如果proxyTargetClass=true，即使proxyInterfaces设置一个或多个完全限定的接口名称，也是使用CGLIB代理。

如果proxyInterfaces设置一个或多个完全限定的接口名称，则创建基于JDK的代理。创建的代理实现proxyInterfaces中指定的所有接口。

如果尚未设置的ProxyFactoryBean的proxyInterfaces属性，但是目标类确实实现了一个（或多个）接口，则 ProxyFactoryBean自动检测到目标类确实实现了至少一个接口，并创建了基于JDK的代理。实际代理的接口是目标类实现的所有接口。实际上，这与将目标类的所有接口的提供给该proxyInterfaces属性的效果相同。但是，它的工作量大大减少，而且不容易出现书写错误。


#### 6.4.4. 代理接口


#### 6.4.5. 代理类

CGLIB代理通过在运行时生成目标类的子类来工作。Spring配置该子类来委托其调用原始对象。这个子类通过装饰着模式，编织到通知。

#### 6.4.6. 使用"全局的"Advisors

通过星号匹配拦截器名称
```
<bean id="proxy" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target" ref="service"/>
    <property name="interceptorNames">
        <list>
            <value>global*</value>
        </list>
    </property>
</bean>

<bean id="global_debug" class="org.springframework.aop.interceptor.DebugInterceptor"/>
<bean id="global_performance" class="org.springframework.aop.interceptor.PerformanceMonitorInterceptor"/>
```

### 6.5. 简化代理定义

为代理创建父模板（abstract="true"不会被实例化）：

```
<bean id="txProxyTemplate" abstract="true"
        class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
    <property name="transactionManager" ref="transactionManager"/>
    <property name="transactionAttributes">
        <props>
            <prop key="*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```

创建代理是父模版的子bean定义，将代理的目标包装为内部bean定义：

```
<bean id="myService" parent="txProxyTemplate">
    <property name="target">
        <bean class="org.springframework.samples.MyServiceImpl">
        </bean>
    </property>
</bean>
```

可以覆盖父模版的属性：

```
<bean id="mySpecialService" parent="txProxyTemplate">
    <property name="target">
        <bean class="org.springframework.samples.MySpecialServiceImpl">
        </bean>
    </property>
    <property name="transactionAttributes">
        <props>
            <prop key="get*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="find*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="load*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="store*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```

### 6.6. 使用编码ProxyFactory方式创建AOP代理

```
ProxyFactory factory = new ProxyFactory(myBusinessInterfaceImpl);
factory.addAdvice(myMethodInterceptor);
factory.addAdvisor(myAdvisor);
MyBusinessInterface tb = (MyBusinessInterface) factory.getProxy();
```

### 6.7. 控制通知对象

一但创建AOP代理对象，可以通过`org.springframework.aop.framework.Advised`接口控制。任何AOP代理可以转换成这个接口类型。

```
Advised advised = (Advised) myObject;
Advisor[] advisors = advised.getAdvisors();
int oldAdvisorCount = advisors.length;
System.out.println(oldAdvisorCount + " advisors");

// 添加一个通知，譬如没有切入点的拦截器
// 将会匹配所有代理方法
// 可添加拦截器，前置、正常后置、异常后置通知
advised.addAdvice(new DebugInterceptor());

// 添加指定切入点的通知
advised.addAdvisor(new DefaultPointcutAdvisor(mySpecialPointcut, myAdvice));

assertEquals("Added two advisors", oldAdvisorCount + 2, advised.getAdvisors().length);
```

### 6.8. 使用自动代理功能


#### 6.8.1. 自动代理的Bean定义

这章节的类主要来自`org.springframework.aop.framework.autoproxy`包。

##### BeanNameAutoProxyCreator

通过BeanPostProcessor机制，自动创建名称规则匹配的Bean的AOP代理。

```
<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    <property name="beanNames" value="jdk*,onlyJdk"/>
    <property name="interceptorNames">
        <list>
            <value>myInterceptor</value>
        </list>
    </property>
</bean>
```

##### DefaultAdvisorAutoProxyCreator

在当前上下文中自动应用符合的advisors。不能是拦截器或通知，因为需要通过切入点评估是否有业务对象的匹配。
没有匹配的，则该对象不会被代理。

```
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>

<bean class="org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor">
    <property name="transactionInterceptor" ref="transactionInterceptor"/>
</bean>

<bean id="customAdvisor" class="com.mycompany.MyAdvisor"/>

<bean id="businessObject1" class="com.mycompany.BusinessObject1">
    <!-- Properties omitted -->
</bean>

<bean id="businessObject2" class="com.mycompany.BusinessObject2"/>
```

### 6.9. 使用TargetSource实现

`org.springframework.aop.TargetSource`接口负责返回实现连接点的"目标对象"。AOP代理处理一个方法调用时就会实现你一个目标实例。

#### 6.9.1. 热插拔目标对象

`org.springframework.aop.target.HotSwappableTargetSource`让AOP代理的目标切换立即生效。`HotSwappableTargetSource`是线程安全的。

```
<bean id="initialTarget" class="mycompany.OldTarget"/>

<bean id="swapper" class="org.springframework.aop.target.HotSwappableTargetSource">
    <constructor-arg ref="initialTarget"/>
</bean>

<bean id="swappable" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="targetSource" ref="swapper"/>
</bean>
```

```
HotSwappableTargetSource swapper = (HotSwappableTargetSource) beanFactory.getBean("swapper");
// 调用swap方法，切换目标对象
Object oldTarget = swapper.swap(newTarget);
```

#### 6.9.2. 池化目标对象

`org.springframework.aop.target.AbstractPoolingTargetSource`

```
<bean id="businessObjectTarget" class="com.mycompany.MyBusinessObject"
        scope="prototype">
    ... properties omitted
</bean>

<bean id="poolTargetSource" class="org.springframework.aop.target.CommonsPool2TargetSource">
    <property name="targetBeanName" value="businessObjectTarget"/>
    <property name="maxSize" value="25"/>
</bean>

<bean id="businessObject" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="targetSource" ref="poolTargetSource"/>
    <property name="interceptorNames" value="myInterceptor"/>
</bean>
```

请注意，目标对象（businessObjectTarget在前面的示例中）必须是原型。这使PoolingTargetSource实现可以创建目标的新实例，以根据需要扩展池。

#### 6.9.3. 原型目标对象

```
<bean id="prototypeTargetSource" class="org.springframework.aop.target.PrototypeTargetSource">
    <property name="targetBeanName" ref="businessObjectTarget"/>
</bean>
```

#### 6.9.4. ThreadLocal目标对象

```
<bean id="threadlocalTargetSource" class="org.springframework.aop.target.ThreadLocalTargetSource">
    <property name="targetBeanName" value="businessObjectTarget"/>
</bean>
```

### 6.10. 自定义通知类型

自定义类实现`org.aopalliance.aop.Advice`接口。编写`org.springframework.aop.framework.adapter`的SPI配置。