## 1. 自定义注解
#### 定义
+ 注解是一种元数据形式。即注解是属于java的一种数据类型，和类、接口、数组、枚举类似。
+ 注解用来修饰，类、方法、变量、参数、包。
+ 注解不会对所修饰的代码产生直接的影响。

自定义注解示例
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface CherryAnnotation {
    String name();
    int age() default 18;
    int[] score();
}
```
#### 常用的元注解
元注解：专门修饰注解的注解。
+ @Target
+ @Retention
+ @Documented
+ @Inherited
+ @Import 用于依赖第三方包中bean的配置和加载
+ @SpringBootApplication
+ @@ComponentScan
+ @@EnableAutoConfiguration
+ @Conditional
ConfigurationClassParser.doProcessConfigurationClass
## 2. 配置文件
springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件
– file:./config/
– file:./
– classpath:/config/
– classpath:/
[spring外部配置方式](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config)
+ 4.命令行参数 --spring.profiles.active=dev; -Dspring.profiles.active=dev
+ 9.Java System properties.
+ 10.OS environment variables.
+ 12.Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants)
+ 13.Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants)
+ 14.Application properties outside of your packaged jar (application.properties and YAML variants).
+ 15.Application properties packaged inside your jar (application.properties and YAML variants).
+ 16.@PropertySource annotations on your @Configuration classes.
## 3. Springboot 启动过程
实例化
```java
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
	this.resourceLoader = resourceLoader;
	Assert.notNull(primarySources, "PrimarySources must not be null");
	this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
	this.webApplicationType = WebApplicationType.deduceFromClasspath();
	//实例化初始器
	setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
	//实例化监听器
	setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
	this.mainApplicationClass = deduceMainApplicationClass();
}
```
run方法
```java
public ConfigurableApplicationContext run(String... args) {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	ConfigurableApplicationContext context = null;
	Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
	configureHeadlessProperty();
	//初始化监听器
	SpringApplicationRunListeners listeners = getRunListeners(args);
	listeners.starting();
	try {
		ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
		ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
		//初始化填充Environment的参数
		configureIgnoreBeanInfo(environment);
		Banner printedBanner = printBanner(environment);
		context = createApplicationContext();
		exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
				new Class[] { ConfigurableApplicationContext.class }, context);
		prepareContext(context, environment, listeners, applicationArguments, printedBanner);
		refreshContext(context);
		afterRefresh(context, applicationArguments);
		stopWatch.stop();
		if (this.logStartupInfo) {
			new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
		}
		listeners.started(context);
		callRunners(context, applicationArguments);
	}
	catch (Throwable ex) {
		handleRunFailure(context, ex, exceptionReporters, listeners);
		throw new IllegalStateException(ex);
	}

	try {
		listeners.running(context);
	}
	catch (Throwable ex) {
		handleRunFailure(context, ex, exceptionReporters, null);
		throw new IllegalStateException(ex);
	}
	return context;
}
 ```
 ```java
 public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        //记录启动时间、状态，web容器初始化其property，复制listener
        prepareRefresh();
        //这里返回的是context的BeanFactory
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        //beanFactory注入一些标准组件，例如ApplicationContextAwareProcessor，ClassLoader等
        prepareBeanFactory(beanFactory);
        try {
            //给实现类留的一个钩子，例如注入BeanPostProcessors，这里是个空方法
            postProcessBeanFactory(beanFactory);

            // 调用切面方法
            invokeBeanFactoryPostProcessors(beanFactory);

            // 注册切面bean
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            initMessageSource();

            // bean工厂注册一个key为applicationEventMulticaster的广播器
            initApplicationEventMulticaster();

            // 给实现类留的一钩子，可以执行其他refresh的工作，这里是个空方法
            onRefresh();

            // 将listener注册到广播器中
            registerListeners();

            // 实例化未实例化的bean
            finishBeanFactoryInitialization(beanFactory);

            // 清理缓存，注入DefaultLifecycleProcessor，发布ContextRefreshedEvent
            finishRefresh();
        }

        catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                        "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }
    }
}
```
## 4. 自动配置原理
1. SpringBoot启动的时候加载主配置类，开启了自动配置功能 ==@EnableAutoConfiguration==

总的来说，@EnableAutoConfiguration完成了一下功能：
从classpath中搜寻所有的 META-INF/spring.factories 配置文件，并将其中org.springframework.boot.autoconfigure.EnableutoConfiguration 对应的配置项通过反射实例化为对应的标注了@Configuration的JavaConfig形式的IoC容器配置类，然后汇总为一个并加载到IoC容器。

