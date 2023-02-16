# Welcom to spring-batch-context

The aim of this library is to allow developers who use spring-batch to propagate information from the main thread that runs the batch to the executions context of the batch items : ItemReader, ItemProcessor and ItemWriter.
For example we some times need to extract the current user from Security Context, so instead of writing the code that passes the current user information as a job parameter we let this library to handle it.
This library can be extended to support any information developer want to add.

The integration of this library require a simple configuration during the job definition (see Code example)

# What is new in the version 1.1.0
This version supports multi-threaded step, multi-process, parallel steps and flow by using customized task executors (similar to simple and pool task executors)

# Modules
This Project includes the modules described bellow. All those modules have starters so just add them to the pom.xml in order to have them working

## spring-batch-security
This module is used to propagate the spring security context throw the batch Items

### Usage
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-security</artifactId>
	<version>1.1.0</version>
</dependency>
```

## spring-batch-mdc
This module is used to propagate values stored in Slf4j's MDC context throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-mdc</artifactId>
	<version>1.1.0</version>
</dependency>
```

## spring-batch-sleuth
This module is used to propagate sleuth Span information throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-sleuth</artifactId>
	<version>1.1.0</version>
</dependency>
```

## spring-batch-locale
This module is used to propagate the locale context throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-locale</artifactId>
	<version>1.1.0</version>
</dependency>
```

## Customised task executors
In order to run the jobs asynchronously with parallel processing, we should use one of the following task executors:
- ContextBasedSimpleAsyncTaskExecutor: inherits `SimpleAsyncTaskExecutor` with context propagation support.
- ContextBasedThreadPoolTaskExecutor: inherits `ThreadPoolTaskExecutor` with context propagation support.

### Code example
#### Task executors configuration
```java

/**
 * Bean example for creating a simple task executor
 * The task listener beans are created automatically by the starter
 */
@Bean
public TaskExecutor simpleTaskExecutor(@Autowired List<TaskExecutorListener> taskExecutorListeners){
        return new ContextBasedSimpleAsyncTaskExecutor(taskExecutorListeners);
}

/**
 * Bean example for creating a thread pool task executor
 * The task listener beans are created automatically by the starter
 */
@Bean
public TaskExecutor threadpoolTaskExecutor(@Autowired List<TaskExecutorListener> taskExecutorListeners) {
        return new ContextBasedThreadPoolTaskExecutor(taskExecutorListeners);
}

```

#### Example of job launcher configuration

````java
/**
 * Bean example of Job launcher that uses the custom simple task executor for running jobs
 */
@Bean
public JobLauncher jobLauncher(@Autowired JobRepository jobRepository, TaskExecutor simpleTaskExecutor) throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(simpleTaskExecutor);// configuration of simple task executor for runing the job asynchronously
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
}
````

#### Example of step configuration

````java
/**
 * Bean example of Step that uses the custom thread pool task executor for parallel processing
 */
@Bean
public Step step(@Autowired TaskExecutor threadpoolTaskExecutor) {
    return stepBuilderFactory.get("step")
    .reader(reader())
    .processor(processor())
    .writer(writer())
    .listener(stepExecutionListener())
    .taskExecutor(threadpoolTaskExecutor) // configuration of step task executor for parallel processing
    .build();
}
````

#### Example of flow configuration

````java
/**
 * Bean example of Flow that uses the custom thread pool task executor for
 * running flow1 and flow2 in parallel
 */
@Bean
public Flow splitFlow(@Autowired TaskExecutor threadpoolTaskExecutor) {
    return new FlowBuilder<SimpleFlow>("splitFlow")
    .split(threadpoolTaskExecutor)
    .add(flow1(), flow2())
    .build();
}
````

**Remark**: Unlike the version 1.0.0, this version doesn't include spring-batch and spring boot dependencies so developer should add his own ones.
