# 토이 프로젝트 - 게시판

로컬 MariaDB 데이터베이스 생성

```mariadb
create database toy_board;
create user 'antop'@'%' identified by 'local';
grant all privileges on toy_board.* to 'antop'@'localhost';
flush privileges;
```
