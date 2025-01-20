# 토이 프로젝트 - 게시판

점진적으로 발전해나가는 게시판 프로젝트

## Sprint 5

### 완전한 로컬 환경에서 실행 가능하도록 구성

로컬에서 외부 서버를 사용하지 않고 H2와 Embedded Redis를 사용하여 인터넷이 안되더라도 개발 가능하도록 함 

* 외부 MariaDB → H2 Tcp Database Server
* 외부 Redis → [Embedded Redis Server](https://github.com/codemonstur/embedded-redis)

### Bootstrap UI 구성

부트스트랩을 사용하여 화면을 이쁘게(?) 꾸밈

* [Bootstrap 5](https://getbootstrap.com/)
* [Bootstrap Icons](https://icons.getbootstrap.com/)
* [Tags for Bootstrap 4/5](https://github.com/lekoala/bootstrap5-tags)
* 나눔고딕 웹폰트

### 위지윅에디터 적용

* [CKEditor5](https://ckeditor.com/ckeditor-5/)
* [Prism](https://prismjs.com/) : 코드 블럭 하이라이트

### 태그 목록 재구성

```mysql
alter table posts modify column tags text null comment '태그';
```

## Sprint 4

파일 업로드를 구현했다.

![sprint3-db](./assets/sprint4-db.png)

```mysql
create table files
(
    file_id   bigint       not null auto_increment comment '파일 ID',
    file_name varchar(255) not null comment '파일명',
    file_size int          not null comment '파일 크기',
    file_type varchar(50)  not null comment '파일 타입',
    file_path varchar(255) not null comment '파일 경로',
    created   datetime     not null comment '등록 일시',
    primary key (file_id)
);

create table post_files
(
    post_id bigint not null comment '게시글 ID',
    file_id bigint not null comment '파일 ID',
    primary key (post_id, file_id)
);

alter table post_files add foreign key (post_id) references posts (post_id) on delete cascade;
alter table post_files add foreign key (file_id) references files (file_id) on delete cascade;
```

## Sprint 3

조회 수와 태그 목록 기능을 개발했다.

조회 수 중복 증가 방지를 위해 레디스를 사용 했다. 업데이트는 비관적 락(`select for update`)을 사용해 업데이트 했다.

![sprint2-db](./assets/sprint3-db.png)

```mysql
/* 조회수 컬럼 추가 */
alter table posts add column hits bigint default 0 not null comment '조회수';
/* 태그 컬럼 추가 */
alter table posts add column tags json default '[]' not null comment '태그' check (json_valid(`tags`));
```

## Sprint 2

오프셋(`offset`) 방식의 페이징과 검색 기능을 개발했다.

## Sprint 1

간단한 테이블을 생성하고 기본적인 CRUD를 구현했다.

* 프로젝트 생성
* CRUD 구현

![sprint1-db](./assets/sprint1-db.png)

```mysql
create table posts
(
    post_id  bigint       not null auto_increment comment '게시글 ID',
    subject  varchar(255) not null comment '제목',
    content  text         not null comment '내용',
    author   varchar(100) not null comment '작성자',
    created  datetime     not null comment '등록 일시',
    modified datetime     null comment '수정 일시',
    primary key (post_id)
);
```

```mysql
/* 데이터베이스와 계정 생성 */
create database toy_board;
create user 'antop'@'%' identified by 'local';
grant all privileges on toy_board.* to 'antop'@'localhost';
flush privileges;
```
