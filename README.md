# 토이 프로젝트 - 게시판

로컬 MariaDB 데이터베이스 생성

```mariadb
create database toy_board;
create user 'antop'@'%' identified by 'local';
grant all privileges on toy_board.* to 'antop'@'localhost';
flush privileges;
```

* [Kotlin 1.9.25](https://kotlinlang.org/docs/home.html)
* [Spring Boot 3.4.0](https://docs.spring.io/spring-boot/index.html)
* [Exposed 0.56.1](https://jetbrains.github.io/Exposed/home.html)
* [Spring Boot and Thymeleaf library for htmx 3.5.0](https://github.com/wimdeblauwe/htmx-spring-boot)
* [prettytime](https://www.ocpsoft.org/prettytime/)
