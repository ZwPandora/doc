## 1. 自定义注解
#### 定义
+ 注解是一种元数据形式。即注解是属于java的一种数据类型，和类、接口、数组、枚举类似。
+ 注解用来修饰，类、方法、变量、参数、包。
+ 注解不会对所修饰的代码产生直接的影响。

自定义注解示例
```java
	public @interface CherryAnnotation {
		public String name();
		int age() default 18;
		int[] array();
	}
```
#### 常用的元注解
元注解：专门修饰注解的注解。
+ @Target
+ @Retention
+ @Documented
+ @Inherited
## 2. 配置文件
[spring外部配置方式](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config)
## 3. 自动配置原理
