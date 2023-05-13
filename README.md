# 🌱 GreenIng
The goal of this project is to build an eco-friendly Java application.
This is achieved by using a fast framework that ensures a quick start and optimal use of resources, implementing services with efficient data processing and reducing the size of the executable file. Small size of the codebase is also important!

### 🌱 About the app
This [Micronaut](https://micronaut.io) application exposes three REST services:
* <b>POST /atms/calculateOrder</b> - plans the work of the ATM service team

* <b>POST /onlinegame/calculate</b> - builds a list of players in an online game

* <b>POST /transactions/report</b> - generates a report for debit and credit transactions

The [OpenAPI](https://swagger.io/specification/) specification for services is available in the ```src/main/resources/openapi``` directory. 

Integration tests contain sample requests with expected results.


### 🌱 Setup

Use:
* [Maven](https://maven.apache.org) 3.6.3 
* [OpenJDK](https://openjdk.org) 17

### 🌱 Build

Build and run tests:
```bash
mvn clean package
```
Quick build without tests:
```bash
mvn clean package -Dmaven.test.skip=true
```
Build and run benchmarks:
```bash
mvn clean package -Prun-benchmarks
```
[JMH](https://openjdk.org/projects/code-tools/jmh/) benchmark report will be available in `target/benchmark_results_*.json` file. You can visualise it using 
https://jmh.morethan.io
### 🌱 Running
Run:
```bash
java -jar ./target/greening-1.0-SNAPSHOT.jar
```

Run a bit faster:
```bash
java -jar -noverify ./target/greening-1.0-SNAPSHOT.jar
```
### 🌱 Project dependencies
Generate report:
```bash
mvn project-info-reports:dependencies
```
The dependency report (including licenses) will be available in the `target/site/dependencies.html` file.

<details>
  <summary>Dependency license summary (click to expand)</summary>

> * MIT-0: reactive-streams
> * The Apache License, Version 2.0: org.apiguardian:apiguardian-api, org.opentest4j:opentest4j
> * MIT License: SLF4J API Module
> * Eclipse Public License v2.0: JUnit Jupiter API, JUnit Jupiter Engine, JUnit Jupiter Params, JUnit Platform Commons, JUnit Platform Engine API
> * GPL2 w/ CPE: Jakarta Annotations API
> * GNU Lesser General Public License: Logback Classic Module, Logback Core Module
> * Apache License 2.0: Bean Validation API, swagger-annotations
> * The MIT License: JOpt Simple
> * Apache License, Version 2.0: Netty/Buffer, Netty/Codec, Netty/Codec/HTTP, Netty/Codec/HTTP2, Netty/Codec/Socks, Netty/Common, Netty/Handler, Netty/Handler/Proxy, Netty/Resolver, Netty/Transport, Netty/Transport/Native/Unix/Common, Non-Blocking Reactive Foundation for the JVM, SnakeYAML
> * CDDL + GPLv2 with classpath exception: javax.annotation API
> * EPL 2.0: Jakarta Annotations API
> * GNU General Public License (GPL), version 2, with the Classpath exception: JMH Core
> * The Apache Software License, Version 2.0: Commons Math, Jackson datatype: JSR310, Jackson datatype: jdk8, Jackson-annotations, Jackson-core, Jakarta Dependency Injection, Micronaut, Micronaut Security, Micronaut Test, greening, jackson-databind
> * Eclipse Public License - v 1.0: Logback Classic Module, Logback Core Module

</details>

Scan project to identify the use of known vulnerable components:
```bash
mvn verify
```
The report will be saved to the `target/dependency-check-report.html` file.


### 🌱 Info
This project is taking part in a green coding competition. Check the details [here](https://www.ing.pl/zielonykod).