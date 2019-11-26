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
	setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
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
	SpringApplicationRunListeners listeners = getRunListeners(args);
	listeners.starting();
	try {
		ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
		ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
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
## 4. 自动配置原理
1. SpringBoot启动的时候加载主配置类，开启了自动配置功能 ==@EnableAutoConfiguration==
