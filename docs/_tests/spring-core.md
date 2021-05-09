
# Core Technologies

参考[Spring Core文档](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html)
> 版本：5.3.5

该文档主要介绍了**Spring框架的控制反转（IoC）容器**和**Spring的面向方面的编程（AOP）技术**。

## 1. IOC容器

### 1.1. Spring IoC容器和Bean简介

本章将介绍Spring的控制反转（IoC）实现。IoC也称为依赖注入（DI）。他是一个过程，首先只能通过构造函数参数、工厂方法的参数或在构造或创建对象实例后在对象实例上设置属性的这些方式来**定义依赖项**，然后在容器创建bean时**注入依赖项**。

IoC容器的基础是这两个包：`org.springframework.beans`和`org.springframework.context`。其中`BeanFactory`接口定义了管理对象的机制，`ApplicationContext`是其子接口，附加了企业级功能，本章节也是重点围绕`ApplicationContext`展开的。

在Spring中，Bean是由IoC容器实例化、组装和管理的对象。容器使用的配置元数据反映了Bean之间的依赖关系。

### 1.2. 容器简介

`org.springframework.context.ApplicationContext`接口代表了Spring IoC容器，并且其负责Bean的实例化、组装及管理。容器通过读取配置元数据获取Bean信息及依赖关系。 比如`ClassPathXmlApplicationContext`容器实现可用于读取XML配置。

![Spring工作原理](./others/how-spring-work.jpg)

#### 1.2.1 配置元数据方式
配置元数据，是开发人员指定用于Spring容器实例化、组装和管理对象的配置。
元数据配置方式有XML、基于注解配置、基于Java代码配置。
XML方式通过`<bean>`标签及属性定义配置；
基于注解配置，相关注解如`@Component`、`@Autowire`等；
基于Java代码配置，其功能需要配合`@Configuration`，`@Bean`，`@Import`，和`@DependsOn`注解使用；

#### 1.2.2 实例化容器
在容器管理Bean之前，首先要创建容器。
```
ApplicationContext context = new ClassPathXmlApplicationContext("1_2_2_实例化容器.xml");
```
> 指定配置文件不建议使用"../"相对路径

#### 1.2.3. 使用容器
通过使用`ApplicationContext`的方法`T getBean(String name, Class<T> requiredType)`，可以检索bean的实例。
> 代码见 io.github.wesleyone.spring.core.c1.c2.ContainerTest

### 1.3. Bean简介
Spring IoC容器管理一个或多个bean。这些Bean是根据开发人员提供的配置元数据创建生成。
在容器内部，通过`BeanDefinition`对象保存bean定义，该对象包含以下属性：
- 类全限定名
- Bean在容器中的行为（作用域、生命周期回调等）
- 依赖项
- 其他自定义配置

配置元数据会转换成`BeanDefinition`中的属性。下表描述了部分属性：

| 属性 | 介绍|
| --- | ---|
|Class| 需要管理的类|
|Name | Bean名称|
|Scope| Bean生命周期|
|Constructor arguments| 配置的构造器入参，用于DI|
|Properties| 配置的属性，用于DI|
|Autowiring mode| 自动注入模式|
|Lazy initialization mode| 懒加载模式|
|Initialization method| 初始化回调方法|
|Destruction method| 销毁回调方法|

> 另外允许手动注册方式添加Bean定义。但是，手动注册需要尽早完成，以便容器在自动装配时可以正确的获取到它。
>
> 虽然允许覆盖已有的Bean定义或单例实例，但是在运行时注册新的Bean是官方不支持的(程序正常，不建议)，防止并发访问异常。
>
> 参考用例 io.github.wesleyone.spring.core.c1.c3.BeanTest

#### 1.3.1. Bean命名
一个Bean可以对应多个名称，一个名称只能对应唯一Bean。
在Bean定义命名外，可以为Bean Name管理更多的别称（alias）。

#### 1.3.2. 实例化Bean
Bean定义是创建一个或多个对象的诀窍。容器通过Bean命名查找到Bean定义，然后使用该Bean定义封装的配置元数据创建（或获取）真实的对象。
- 构造函数实例化
- 静态工厂方法实例化
- 使用实例对象的工厂方法

> 参考用例 io.github.wesleyone.spring.core.c1.c3.InstantiationBeanTest.instantiationBeanTest
>
>  本章配置文件中的`factory-bean`和`FactoryBean`没有任何关系。`factory-bean`配置是指定静态工厂方法所属的实例对象。而`FactoryBean`是用于创建对象。

**获取运行时的Bean类型**
由于使用静态工厂方法实例化、实例对象的工厂方法实例化、`FactoryBean.getObject()`、AOP代理包装Bean实例，导致Bean的实际类型暴露程度有限。
推荐使用`BeanFactory.getType`获取运行时的Bean类型。

> 参考用例 io.github.wesleyone.spring.core.c1.c3.InstantiationBeanTest.getTypeTest

### 1.4. 依赖关系（Dependencies）
Bean定义内的依赖项用于表名当前Bean的依赖关系

#### 1.4.1. 依赖注入（DI）
依赖注入是一个过程。该过程中，对象只能通过构造函数参数、工厂方法的参数或在构造或创建对象实例后在对象实例上设置的属性来定义其依赖关系，然后在创建Bean时注入依赖项。这个过程本质上是bean本身的逆转控制过程，通过类的构造函数和服务定位模式（Service Locator pattern）来控制其依赖项的实例化控制或者定位。

DI存在两个主要方式：**基于构造函数的DI**,**基于Setter的DI**。

##### 基于构造函数的DI
基于构造函数的DI，通过容器调用具有多个参数的构造函数来实现的，其中每个参数都是依赖项。调用带有参数的`static`工厂方法来构造Bean时，他的参数也是依赖项。

构造函数参数解析方式包括：
1. 构造函数参数类型匹配
2. 构造函数参数索引(索引从0开始)
3. 构造函数参数名称

后两种方式，在存在相同类型参数时，可以消除歧义。
> 参考用例：io.github.wesleyone.spring.core.c1.c4.DITest.DIConstructorTest

##### 基于Setter的DI
通过使用无参数构造函数或无参数`static`工厂方法实例化bean之后，容器通过调用bean上的setter方法来实现基于setter的DI。

> 基于构造函数的DI 和 基于Setter的DI 可以混合使用。建议将构造函数注入用于强制参数依赖项注入，setter注入用于可选依赖项注入。
>
> 参考用例：io.github.wesleyone.spring.core.c1.c4.DITest.DISetterTest

##### 依赖关系解析过程
容器执行bean依赖项解析过程如下：
- `ApplicationContext`通过配置元数据来创建和初始化所有bean。配置元数据可以通过XML，Java代码或注解来指定。
- 对于每个bean，其依赖项都以属性、构造函数参数或`static-factory`方法的参数的形式表示。在创建Bean时，会将这些依赖项提供给Bean。
- 每个属性或构造函数参数都是要设置具体的值，或者是对容器中另一个bean的引用。
- 每个值的属性或构造函数参数都将从其指定的格式转换为该属性或构造函数参数的实际类型。默认情况下，Spring会以`String`类型，自动把值转换成所需的内置类型，比如int， long，String，boolean等等。

在创建容器时，Spring会验证每个bean的配置。但是只有创建Bean时，才会设置依赖项。懒加载或作用域不同，可能会造成较迟创建Bean。

> 循环依赖：
>
> 如果使用构造函数注入，则可能无法解决的循环依赖。比如说A类构造函数参数为B类，B类构造函数参数为A类，则相互注入，容器在运行时检测到后会抛出`BeanCurrentlyInCreationException`异常。
>
> 使用setter注入有效解决循环依赖
>
> 参考用例：io.github.wesleyone.spring.core.c1.c4.DITest.circularDependenciesTest
>
> 模仿IoC源码处理依赖循环代码：io.github.wesleyone.spring.core.c1.c4.DITest.normalCircularDependencies_like_spring

Spring默认**非懒加载**，可以提前发现依赖项问题引起的异常。
如果不存在依赖循环关系，容器在对BeanA调用setter方法之前完全实例化BeanB。

#### 1.4.2. 依赖关系配置细节
- 内部类的引用。不能指定id、name、scope。内部类始终与外部类一起创建。
- 集合、集合合并
- 复合属性名

> 参考用例：io.github.wesleyone.spring.core.c1.c4.DIConfigurationTest

#### 1.4.3. `depends-on`
当bean之间没有直接依赖关系，但是初始化时希望有序，则可以使用`depends-on`。
`depends-on`强制使用该配置的Bean在初始化之前，先完成该配置中的所有Bean的初始化。当销毁时，则顺序相反。

> 参考用例：io.github.wesleyone.spring.core.c1.c4.DIDependsOnTest

#### 1.4.4. 懒加载
单例默认**非懒加载**。可以立即发现配置或环境的错误。
开启懒加载的Bean在初次使用时创建Bean实例。
当懒加载的Bean被其他非懒加载的Bean依赖时，会提前初始化。

> 参考用例：io.github.wesleyone.spring.core.c1.c4.DILazyInitTest

#### 1.4.5. *自动装配（Autowiring）
自动装配，可以减少指定属性和构造函数参数的编码量，可以自动更新依赖项的实现类。

自动装配有四种模式：

| 模式 | 介绍 |
| --- | --- |
| `no` | （默认）无自动装配。Bean引用必须由`ref`元素显式定义。 |
| `byName` | 按照属性名称自动装配。容器会查找属性名称同名的Bean来装配。 |
| `byType` | 按照属性类型自动装配。容器中有且只有一个该类型的Bean时则用来装配，多个则抛异常，没有则不设置该属性。|
| `constructor` | 按照构造函数参数的类型自动装配。容器中有且只有一个该类型的Bean时则用来装配，否则抛异常 |

##### 自动装配的局限性和缺点
如果项目中都使用自动装配时效果最佳，如果只是部分使用，可能会产生问题。
局限性和缺点如下：
- 显式依赖项`property`和`constructor-arg`设置始终会覆盖自动装配。不能自动装配简单的属性，例如基础类型、`String`、`Class`（以及这些类型的数组）。
- 自动装配不如显式接线精确。
- 装配信息可能不适用于通过Spring容器生成文档的工具。
- 自动装配的属性类型或构造函数参数类型可能与容器中多个Bean定义匹配。如果没有唯一的bean定义，则会抛异常。但对于数组、集合、`Map`实例，这不一定会报错。

最后一点的解决方法如下：
- 关闭自动装配，使用显式声明依赖
- 排除属性Bean被自动装配
- 通过设置`<bean>`标签的`primary=true`，将该Bean定义指定为主要候选者
- 基于注解配置，实现更细力度的控制

##### 排除属性Bean被自动装配
容器在自动装配BeanA时，不希望根据类型查找到的BeanB，可以设置BeanB的配置`autowire-candidate=false`。
前提是**基于类型的自动装配**才有效，该配置不会影响基于名称的引用。

> 参考用例：io.github.wesleyone.spring.core.c1.c4.DIAutowiringTest

#### 1.4.6. 方法注入
单例对象依赖非单例对象时，由于单例对象只会创建一次，设置非单例对象属性也只有一次，导致在单例中使用非单例对象时都是相同的对象。
方法注入就是为了解决这个问题。
Spring框架通过CGLIB库动态生成子类，从而实现方法注入。因此该方法编码限制参考CGLIB要求。

> 参考用例：io.github.wesleyone.spring.core.c1.c4.DIMethodInjectionTest

### 1.5. Bean Scopes 作用域
有六种作用域：

| Scope | 描述 |
| --- | --- |
| singleton | （默认）单例。单个Bean定义限定用于单个实例 |
| prototype | 原型。单个Bean定义可用于多个实例|
| request | 单个Bean定义限定用于单个HTTP请求的生命周期。只能在Web容器中使用。|
| session | 单个Bean定义限定用于单个HTTP会话的生命周期。只能在Web容器中使用。|
| application | 单个Bean定义限定用于单个`ServletContext`的生命周期。只能在Web容器中使用。|
| websocket |单个Bean定义限定用于单个`WebSocket`的生命周期。只能在Web容器中使用。 |

> Spring 3.0开始，线程作用域可用，需要手动注册。参考`SimpleThreadScope`。

#### 1.5.1. Singleton Scope 单例范围
请求相同ID或命名的Bean实例时，容器返回相同实例对象。
容器会在第一次请求时，将该单个实例存储在缓存中，并且对该命名bean的所有后续请求和引用都返回该缓存对象。

#### 1.5.2. Prototype Scope 单例范围
请求相同ID或命名的Bean实例时，容器每次都会创建新对象返回。
Spring不管理原型Bean的完整生命周期。初始化回调方法会执行，但是销毁回调不会执行。

#### 1.5.3 单例Bean依赖原型Bean
单例对象依赖非单例对象时，由于单例对象只会创建一次，设置非单例对象属性也只有一次，导致在单例中使用非单例对象时都是相同的对象。
解决方法为[方法注入](#1.4.6._方法注入)

#### 1.5.4. request、session、application、websocket scope
只能在web容器下使用，比如`XmlWebApplicationContext`。

#### 1.5.5. 自定义作用域

1. 创建自定义作用域实现。实现`org.springframework.beans.factory.config.Scope`接口。
2. 注册自定义作用域。`ApplicationContext.registerScope(..)`

```
// 以官方提供的`SimpleThreadScope`为例
Scope threadScope = new SimpleThreadScope();
beanFactory.registerScope("thread", threadScope);

// 然后指定bean使用自定义的作用域
<bean id="..." class="..." scope="thread">
```

### 1.6. 自定义Bean性质

自定义Bean性质（the Nature of a Bean）常用接口如下：

- 生命周期回调
- `ApplicationContextAware` and `BeanNameAware`
- 其他`Aware`接口

#### 1.6.1. *生命周期回调

##### 初始化回调

- 实现接口`InitializingBean`方法`afterPropertiesSet()`
- 使用JSR-250规范的`@PostConstruct`注解方法（官方推荐，减少与Spring强耦合）
- xml配置`init-method`属性指定方法名
- `@Bean`的`initMethod`配置方法名

##### 销毁回调

- 实现接口`DisposableBean`方法`destroy()`
- 使用JSR-250规范的`@PreDestroy`注解方法（官方推荐，减少与Spring强耦合）
- xml配置`destroy-method`属性指定方法名
- `@Bean`的`destroyMethod`配置方法名

##### 默认初始化和销毁方法
通过设置`<beans default-init-method="init">`，则其内部的所有bean标签默认初始化调用`init`方法。


##### 生命周期机制混合使用

同个Bean同时使用多种方式的执行顺序：

初始化：
1. `@PostConstruct`方法注解
2. `InitializingBean#afterPropertiesSet()`
3. xml配置`init-method`属性或者`@Bean`配置`init`属性（包括默认配置）

销毁：
1. `@PreDestroy`方法注解
2. `DisposableBean#destroy()`
3. xml配置`destroy-method`属性或者`@Bean`配置`destroy`属性（包括默认配置）

##### 启动和关机回调

实现`Lifecycle`接口。当容器收到启动和停止信号时，它将为委派给`LifecycleProcessor`的默认实现`DefaultLifecycleProcessor`，调用所有`Lifecycle`实现的方法。

需要指定顺序可以使用`SmartLifecycle`接口。

##### 在非Web应用程序中正常关闭Spring IoC容器

注册关闭钩子`ConfigurableApplicationContext.registerShutdownHook()`

#### 1.6.2. ApplicationContextAware和BeanNameAware

`ApplicationContextAware`可用于获取容器对象。不推荐使用，不遵循控制反转。可以使用根据类型的自动装配方式获取。
`BeanNameAware`可用于获取定义中的名称。

#### 1.6.3. 其他Aware接口

最重要的`Aware`接口

|名称| 注入依赖 | 
| --- | --- | 
| ApplicationContextAware | 容器事件发布对象 |  |
| ApplicationEventPublisherAware | 容器事件发布对象 |  |
| BeanClassLoaderAware | 类加载器，用于加载Bean类。 | 实例化Bean |
| BeanFactoryAware | BeanFactory |  |
| BeanNameAware | Bean的名称 |  |
| LoadTimeWeaverAware | 编织器，用于加载时处理类定义 | 在Spring框架中使用AspectJ进行加载时编织 |
| MessageSourceAware | 处理消息的策略配置 |  |
| NotificationPublisherAware | Spring JMX通知发布者。 |  |
| ResourceLoaderAware | 配置的加载程序，用于对资源的低级别访问 |  |
| ServletConfigAware | web容器中的ServletConfig |  |
| ServletContextAware | web容器中的ServletContext |  |


### 1.7. Bean定义继承

子bean定义可以从父bean定义中继承配置数据。子定义可以覆盖某些值或根据需要添加其他值。使用继承可以减少编码量，是一种模版形式。
子bean定义的对象实质上是`ChildBeanDefinition`。

子bean定义能继承的内容包括：构造函数参数、属性值和方法，并可以添加新值。对于任何作用域，初始化方法、销毁方法或者静态工厂方法设置都会覆盖父定义配置。
始终从子定义获取的内容包括：依赖项、自动装配模式、依赖项检查、单例以及懒加载。

> 参考用例：io.github.wesleyone.spring.core.c1.c7.BeanDefinitionInheritanceTest

### 1.8. 容器扩展点
通过插入扩展接口实现来扩展IoC容器功能。

#### 1.8.1. 使用BeanBeanPostProcessor自定义Beans
可以在每个Bean初始化前后进行处理。
容器在初始化Bean之前，会先自动检测所有`BeanPostProcessor`实现，以便用于其他Bean的初始化。
可以通过同时实现`Ordered`接口指定多个`BeanBeanPostProcessor`执行顺序。

`BeanPostProcessor`实现的前置方法在每个Bean初始化之前，后置方法在每个Bean初始化之后；

> 编程方式主动注册`BeanPostProcessor`对象。`ConfigurableBeanFactory.addBeanPostProcessor()`。这用于跨层（hierarchy）上下文复制`BeanPostProcessor`对象。
> 这种方式不遵守`Ordered`接口，按照注册顺序执行。这种编程注册总是在自动检测注册前被调用执行。

> SpringAOP动态代理是通过`BeanPostProcessor`实现的，所以`BeanPostProcessor`对象本身或它引用的bean无法被编织;通常会看到这样的日志`Bean someBean is not eligible for getting processed by all BeanPostProcessor interfaces (for example: not eligible for auto-proxying)`

> 例如：自动装配`@Autowired`的功能实现就是由`AutowiredAnnotationBeanPostProcessor`实现的；自动装配带注解的属性、setter方法和任意配置方法。

> 参考用例：io.github.wesleyone.spring.core.c1.c8.BeanPostProcessorTest

#### 1.8.2. 使用BeanFactoryPostProcessor自定义配置元数据

`BeanFactoryPostProcessor`用于对Bean的配置元数据进行操作。
可以通过同时实现`Ordered`接口指定多个`BeanFactoryPostProcessor`执行顺序。

Spring内置了很多`BeanFactoryPostProcessor`实现。比如`PropertyOverrideConfigurer`和 `PropertySourcesPlaceholderConfigurer`。`PropertyOverrideConfigurer`根据beanName.属性名匹配配置值替换bean属性，`PropertySourcesPlaceholderConfigurer`根据占位值替换。

容器会自动检测`BeanFactoryPostProcessor`接口的所有实现bean定义。

> 参考用例：io.github.wesleyone.spring.core.c1.c8.BeanFactoryPostProcessorTest

#### 1.8.3. 自定义FactoryBean

对于复杂对象的初始化，XML方式配置会冗长，可以通过实现`FactoryBean`接口的代码方式更好地表达初始化实例化过程。

`FactoryBean`三个方法解释：

- `Object getObject()`:返回此工厂创建的对象实例。
- `boolean isSingleton()`:返回对象是否单例。
- `Class getObjectType()`:返回对象的类型。未知类型返回null。

当需要调用`FactoryBean`自身对象实例时，需要在bean的`id`前面加上`&`符号。

> 参考用例：io.github.wesleyone.spring.core.c1.c8.FactoryBeanTest


### 1.9. 基于注解的容器配置

以下注解启用及对应需要注入的`BeanPostProcessor`：
- 启用`@Autowire`d自动装配的`AutowiredAnnotationBeanPostProcessor`,
- 启用JSR标准的`@Resource` 、`@PostConstruct`、`@PreDestroy`等注解的`CommonAnnotationBeanPostProcessor`,
- 启用`@PersistenceContext`注解的`PersistenceAnnotationBeanPostProcessor`,
- （废弃）启用@Required的注解的`RequiredAnnotationBeanPostProcessor`

通过xml配置`<context:annotation-config>`方式，隐式地自动向Spring容器注册4个以上BeanPostProcessor。

#### 1.9.1. @Required
从Spring Framework 5.1开始，`@Required`的注解和`RequiredAnnotationBeanPostProcessor`正式弃用；

#### 1.9.2. @Autowired

更加细粒度的自动装配；
可以用JSR330的@Inject注解替代；
可用在有参构造方法、属性、setter方法上；
可用于自定义类型、数组、集合、Map；
其中集合需要有序的化可以通过@Order注解；
Map类型的Key是BeanName;
注解默认配置，属性必须在容器中能找到，否则报错；可通过required=false关闭；

> 从4.3开始，只有一个构造方法时可以不用配置`@Autowired`注解，存在多个构造方法时需要通过注解指定一个。

> 在@Autowired，@Inject，@Value，和@Resource注释由Spring处理 BeanPostProcessor实现。这意味着您不能在自己定义的BeanPostProcessor或BeanFactoryPostProcessor中使用这些注解。必须使用XML或@Bean方法显式设置这些类型？！

#### 1.9.3. @Primary

用于`@Autowired`微调；
自动装配时，根据类型可以找到多个候选实例时，选择有该注解的一个实例用于自动装配。

等同于<bean class="xxx" primary="true">

#### 1.9.4. @Qualifier

用于`@Autowired`微调；
自动装配时，通过指定`beanName`选出当前属性对应的实例；

还可以用于泛型的装配；

#### 1.9.5. 将泛型作为@Autowired的限定符Qualifier

#### 1.9.6. CustomAutowireConfigurer自定义自动装配注解

#### 1.9.7. @Resource

JSR-250的注入注解，Spring支持。
不指定名称时，根据类型查找对象（类似@Autowired）

#### 1.9.8. @Value

用于注入外部化配置
譬如使用`PropertySourcesPlaceholderConfigurer`后置处理器，会从配置文件中查找属性值。

#### 1.9.9. @PostConstruct and @PreDestroy

JSR-250的生命周期注解；
在上文[1.6.1. 生命周期机制混合使用]提过执行顺序；

### 1.10. 类路径扫描和组件管理

#### 1.10.1. 组件注解

@Component
@Service
@Controller
@Repository

#### 1.10.2. 元注解和组合注解

组合注解例如Spring MVC中`@RestController`由`@Controller`和`@ResponseBody`组成。

#### 1.10.3. 自动检测类文件以及注册bean定义

通过在`@Configuration`配置类中添加`@ComponentScan(basePackages = "org.example")`注解，
开启自动扫描和注册bean;

功能同XML配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

> <context:component-scan> 配置直接开启<context:annotation-config>的功能

#### 1.10.4. 自动移扫描过滤器

#### 1.10.5. 通过组件方式定义bean元数据

实质上是工厂方法方式实现；

有个注意点是：
`@Bean`和`@Configuration`一起使用，调用依赖对象`@Bean`方法会通过CGLIB代理在容器中查找该beanName；
而`@Bean`和`@Component`一起使用，调用依赖对象`@Bean`方法就是普通JAVA语义方法调用；

> 参考用例：
> io.github.wesleyone.spring.core.c1.c10.FactoryMethodComponentTest
> io.github.wesleyone.spring.core.c1.c10.FactoryMethodConfigurationTest

[链接](https://blog.csdn.net/long476964/article/details/80626930)



#### 1.10.6. 命名自动检测的组件

组件名称通过`BeanNameGenerator`默认实现策略生成命名；
可通过实现该接口实现自定义命名；

#### 1.10.7. 指定自动检测的组件的使用范围

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

#### 1.10.8. 对组件指定限定符

配合`@Autowired`注解，在解析自动装配候选时提供细粒度的控制

#### 1.10.9. 生成候选组件的索引

编译时创建候选列表，提高启动性能。
需要将所有模块都添加以下依赖：
```
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context-indexer</artifactId>
        <version>5.3.6</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```
编译完成后，会在生成的jar包中生成`META-INF/spring.components`文件。

可以通过JVM系统属性和Spring外部配置方式设置`spring.index.ignore=true`取消使用索引。

### 1.11. Using JSR 330 Standard Annotations

需要添加依赖：
```
<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```

`@Inject`注解使用和`@Autowired`相似，属性、构造方法、setter方法；
`@Inject`配合`@Nullable`实现必填参数；
`@Named`注解参数时指定该参数实例的`beanName`，注解类时表示该类的`beanName`；
`@ManagedBean`用法等同于`@Named`注解在类上，也等同于`@Component`用法；

[使用JSR-330标准注解的限制](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-standard-annotations-limitations)

### 1.12. 基于Java代码的容器配置

#### 1.12.1. 基本概念：@Bean and @Configuration

`@Bean`:通过方法完成SpringIoC容器管理的对象的实例配置初始化，和XML配置中的`<bean/>`功能相同；

`@Configuration`:声明当前类用于Bean定义，即类中的@Bean方法可以相互调用来显示依赖关系；

> 参考用例：
> io.github.wesleyone.spring.core.c1.c10.FactoryMethodConfigurationTest


#### 1.12.2. 使用注解配置型容器AnnotationConfigApplicationContext

构建注解配置容器所需的信息，可通过以下方法：
- 通过构造方法
- 通过指定注册类
- 开启自动扫描注册组件
- 用于MVC的AnnotationConfigWebApplicationContext

#### 1.12.3. 使用@Bean注解

- 使用@Bean注解声明一个Bean
  注解属性有`init-method`、`destroy-method`、`autowiring`、`name`；
  注解的方法返回值类型默认是Bean定义的类型，方法名字默认是BeanName;

- Bean依赖
  方法入参作为依赖

- 设置生命周期回调方法
  可以的类型本身实现生命周期相关接口或注解，也可以使用@Bean的属性配置：
  `@Bean(initMethod = "init",destroyMethod = "cleanup")`

- 指定Bean使用范围
  配合使用@Scope()

- 自定义Bean名称
  自定义BeanName，使用@Bean的name属性；

- 设置Bean别名
  `@Bean({"dataSource", "subsystemA-dataSource", "subsystemB-dataSource"})`

- 设置Bean描述
  使用配合注解 `@Description("Provides a basic example of a bean")`

#### 1.12.4. 使用@Configuration注解

`@Configuration`注解，用来表明当前类用于Bean定义；

- 内部类依赖注入
  类中的多个@Bean方法可以内部依赖；

- 方法注入
  单例Bean依赖原型Bean时

- 其内默认单例@Bean
  `@Configuration`内的`@Bean`方法被`CGLIB`代理，单例时初始化的对象受容器管理；
  所以方法要求能被代理，不能`非public`、`final`


#### 1.12.5. 组合基于JAVA的配置

`@Import`等同于`<import/>`配置，导入其他配置类；

`@Profile`根据环境配置激活配置类；

`@Conditional`根据条件激活配置类；

`@ImportResource`导入XML配置文件；


### 1.13. Environment抽象

#### 1.13.1. 通过Bean定义设置Profiles

1. 指定Profile方式：
- 在@Bean工厂方法上使用@Profile指定环境
- XML Bean定义<beans profile="production"/>

2. 激活Profile方式：
- 主动设置。`ctx.getEnvironment().setActiveProfiles("development");`

3. 默认Profile配置。`ctx.getEnvironment().setDefaultProfiles()`, 使用`spring.profiles.default`属性配置


#### 1.13.2. PropertySource抽象

通过`Environment`抽象获取属性抽象，可用于查询JVM系统配置`System.getProperties()`和JVM环境配置`System.getenv()`。
```
ApplicationContext ctx = new GenericApplicationContext();
Environment env = ctx.getEnvironment();
boolean containsMyProperty = env.containsProperty("my-property");
System.out.println("Does my environment contain the 'my-property' property? " + containsMyProperty);
```

#### 1.13.3. 使用@PropertySource

在`@Configuration`注解的配置类中使用`@PropertySource("classpath:/com/myco/app.properties")`添加配置。

#### 1.13.4. 解析声明中的占位符

例如以下声明，`Environment`会搜索系统属性和环境变量查找`customer`值并替换
```dtd
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>
```

#### 1.14. 注册`LoadTimeWeaver`

`LoadTimeWeaver`是当类加载到JVM时，用于Spring动态转换类。

需要使用的话需要开启：
```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig {
}
```
或者
```dtd
<beans>
    <context:load-time-weaver/>
</beans>
```

后文5.10.4会提到在Spring框架中的AspectJ加载时编织。


### 1.15. ApplicationContext的其他功能

#### 1.15.1. 国际化MessageSource

#### 1.15.2. 标准事件、自定义事件

标准事件：
- ContextRefreshedEvent
- ContextStartedEvent
- ContextStoppedEvent
- ContextClosedEvent
- RequestHandledEvent
- ServletRequestHandledEvent

自定义事件需要继承ApplicationEvent接口；

发布自定义事件：
业务类实现`ApplicationEventPublisherAware`获取`ApplicationEventPublisher`；
调用`publisher.publishEvent(自定义事件)`；

监听自定义事件：
业务类实现`ApplicationListener<自定义事件>`接口，实现`onApplicationEvent`方法监听处理；


注解注册监听事件：
`@EventListener`注解在方法上，方法参数是自定义事件；
注解属性也指定监听事件或SpEL条件；

监听事件异步处理：
配合`@Async`注解

有序监听：
配合`@Order`注解

自定义泛型事件：
```
public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    public EntityCreatedEvent(T entity) {
        super(entity);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
    }
}
```


#### 1.15.3. 简化访问底层资源

`Resource`资源抽象，它的实现类是对`java.net.URL`的封装；

容器可以通过`ResourceLoader`加载资源；

#### 1.15.4. 应用启动跟踪

默认不开启；
`ApplicationStartup`、`StartupStep`

可用于收集启动阶段的各种数据：
- 容器的生命周期（包扫描、配置类管理）
- beans的生命周期（实例化，智能初始化，后置处理）
- 事件处理

#### 1.15.5.简化Web应用的容器实例化

#### 1.15.6.使用JavaEE的RAR文件发布Spring容器

### 1.16. BeanFactory

`ApplicationContext`是`BeanFactory`的实现；

`BeanFactory`仅完成初始实例化和编织，
`ApplicationContext`实现类还提供了生命周期管理、自动注册`BeanPostProcessor`、自动注册`BeanFactoryPostProcessor`、简化国际化`MessageSource`使用、内置事件机制`ApplicationEvent`；


## 2. Resources

如何加载资源和在Spring中使用

TODO 

## 3. 验证，数据绑定和类型转换

## 4. Spring表达式语言

## 5. 使用Spring面向切面编程

## 6. Spring AOP APIs

## 7. Null安全

## 8. 数据缓存和编解码

字节缓存抽象

## 9. 日志










