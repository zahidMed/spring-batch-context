# Welcom to spring-batch-context

The aim of this library is to allow developers who use spring-batch to propagate information from the main thread that runs the batch to the executions context of the batch items : ItemReader, ItemProcessor and ItemWriter.
For example we some times need to extract the current user from Security Context, so instead of writing the code that passes the current user information as a job parameter we let this library to handle it.
This library can be extended to support any information developer want to add.

No additional development is needed in order to use this library because each module (described bellow) as auto-configurable.

# Limitations
This library is compatible with SimpleJobBuilder and cannot be used with FlowJobBuilder

# Modules
This Project includes the modules described bellow. All those modules have starters so just add them to the pom.xml in order to have them working

## spring-batch-security
This module is used to propagate the spring security context throw the batch Items

### Usage
```xml
<dependency>
	<groupId>com.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-security</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

## spring-batch-mdc
This module is used to propagate values stored in Slf4j's MDC context throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>com.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-mdc</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

## spring-batch-sleuth
This module is used to propagate sleuth Span informations throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>com.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-sleuth</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

## spring-batch-locale
This module is used to propagate the locale context throw the batch Items
##### Usage
```xml
<dependency>
	<groupId>com.digibooster.spring.batch</groupId>
	<artifactId>spring-batch-locale</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```
