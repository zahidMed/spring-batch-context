# Welcom to spring-batch-context

The aim of this library is to allow developers who use spring-batch to propagate information from the main thread that runs the batch (where JobLauncher is called) to the executions context of the batch items (ItemReader, ItemProcessor and ItemWriter) including the execution listeners (JobsExecutionListener and stepExecutionListener).
For example we some times need to extract the current user from Security Context, so instead of writing the code that passes the current user information as a job parameter we let this library to handle it.
This library can be extended to support any information developer want to add.

The integration of this library require a simple configuration during the job definition (see Configuration paragraph)

# What is new in the version 1.1.0-RELEASE
This version supports multi-threaded step, multi-process, parallel steps and flow by using customized task executors that inherit from `SimpleAsyncTaskExecutor` and `ThreadPoolTaskExecutor`

# Modules
This Project includes the modules described bellow.

**Remark**: Unlike the version 1.0.0, this one doesn't include spring-batch and spring boot dependencies so developer should add his own ones.
## Spring Security context propagation
### Usage
To propagate Spring Security context (Authentication) inside the Job items and listeners , you need to add `spring-batch-security` as a dependency to your Spring based application, as shown in the following example for Maven:
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-security</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```

## MDC Context Map propagation
### Usage
To propagate Slf4j's MDC context inside the Job items and listeners , you need to add `spring-batch-mdc` as a dependency to your Spring based application, as shown in the following example for Maven:
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-mdc</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```

## Sleuth Span Information propagation
### Usage
To propagate Sleuth Span information context (TraceId, SpanId, Parents, Tags ...) inside the Job items and listeners , you need to add `spring-batch-sleuth` as a dependency to your Spring based application, as shown in the following example for Maven:
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-sleuth</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```

## Locale propatation (Internationalization)
### Usage
To propagate internationalization context (Locale) inside the Job items and listeners , you need to add `spring-batch-locale` as a dependency to your Spring based application, as shown in the following example for Maven:
```xml
<dependency>
	<groupId>org.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-locale</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```

# Configuration
In order to run jobs asynchronously with parallel processing with context propagation, you need use one of the following task executors:
- ContextBasedSimpleAsyncTaskExecutor: inherits `SimpleAsyncTaskExecutor` with context propagation support.
- ContextBasedThreadPoolTaskExecutor: inherits `ThreadPoolTaskExecutor` with context propagation support.

## Configuration example
### Task executors configuration
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

### Job launcher configuration

````java
/**
 * Bean example of Job launcher that uses the custom simple task executor for running jobs
 */
@Bean
public JobLauncher jobLauncher(@Autowired JobRepository jobRepository, TaskExecutor simpleTaskExecutor) throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(simpleTaskExecutor);// configuration of simple task executor for running the job asynchronously
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
}
````

### Step configuration

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

### Example of flow configuration

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
