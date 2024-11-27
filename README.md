# 토이 프로젝트 - 게시판

* [Kotlin 1.9.25](https://kotlinlang.org/docs/home.html)
* [Spring Boot 3.4.0](https://docs.spring.io/spring-boot/index.html)
* [Exposed 0.56.1](https://jetbrains.github.io/Exposed/home.html)
* [Spring Boot and Thymeleaf library for htmx 3.5.0](https://github.com/wimdeblauwe/htmx-spring-boot)
* [prettytime](https://www.ocpsoft.org/prettytime/)

```mariadb
/* 데이터베이스와 계정 생성 */
create database toy_board;
create user 'antop'@'%' identified by 'local';
grant all privileges on toy_board.* to 'antop'@'localhost';
flush privileges;
```

### Sprint 1

간단한 테이블을 생성하고 기본적인 CRUD를 구현했다.

* 프로젝트 생성
* CRUD 구현

```mariadb
/* 테이블 생성 */
create table posts
(
    post_id  bigint       not null auto_increment comment '게시글ID',
    subject  varchar(255) not null comment '제목',
    content  text         not null comment '내용',
    author   varchar(100) not null comment '작성자',
    created  datetime     not null comment '생성일시',
    modified datetime     null comment '수정일시',
    primary key (post_id)
);
```
