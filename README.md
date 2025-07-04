# 토이 프로젝트 - 게시판

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=bugs)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=coverage)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=antop-dev_toy-board&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=antop-dev_toy-board)

점진적으로 발전해나가는 게시판 프로젝트

## Sprint 15

```sql
alter table users add avatar blob null comment '프로필 이미지' after nickname;
```

## Sprint 11

### 비밀글

게시글 등록/수정/답변에 비밀글 추가. 답변을 비밀글로 등록하면 상위 게시글 작성자도 답글을 불 수 있다.

```mysql
alter table posts add secret boolean default false not null comment '비밀글 여부';
alter table posts add parent_id bigint null comment '상위 게시글ID';
alter table posts add foreign key (parent_id) references posts (post_id);
/* 마이그레이션 */
update posts p
set parent_id = (select post_id from posts where depth = p.depth - 1 and thread > p.thread order by p.thread limit 1)
where p.depth >= 1;
```

### 피드 제공

RSS<sup>`Really Simple Syndication`</sup>, ATOM<sup>`Atom Syndication Format`</sup> 포멧으로 피드를 제공한다.

* `/feed/rss`
* `/feed/rss`

## Sprint 10

### 회원과 게시판 연동

* 글 작성 시 로그인 필요
* 내가 작성한 글만 수정/삭제 가능
* 로그인 성공 시 원래 가려고 앴던 곳으로 리다이렉트 (SavedRequest)

## Sprint 9

### 회원 테이블 설계

```mysql
create table users
(
    user_id  bigint auto_increment comment '유저ID',
    email    varchar(100) not null comment '이메일',
    password varchar(255) not null comment '비밀번호',
    nickname varchar(100) not null comment '닉네임',
    created  datetime     not null comment '등록일시',
    modified datetime comment '수정일시',
    primary key (user_id),
    unique (email)
);

```

### 로그인 구현

* 이메일/비빌번호로 로그인
* 로그아웃
* 로그인 상태 유지(Remember Me)
* HTMX + CSRF<sup>`Cross Site Request Forgery`</sup>

## Sprint 8

### AI Code Review 추가

https://github.com/marketplace/actions/chatgpt-codereviewer

`gpt-4o-mini` 모델을 사용하여 PR 단계에서 AI 코드 리뷰

### 캡챠 구현

https://github.com/mewebstudio/java-captcha-generator

게시글 등록/수정, 답글 등록 폼에 캡챠 적용.

![sprint8-captcha](./assets/sprint8-captcha.png)

### 좋아요/싫어요 구현

게시글 보기 상단 오른쪽에 좋아요/싫어요 버튼 배치

```mysql
alter table posts add likes bigint default 0 not null comment '좋아요 수';
alter table posts add dislikes bigint default 0 not null comment '싫어요 수';
```

![sprint8-like](./assets/sprint8-like.png)

## Sprint 7

### 코멘트 구현

코멘트 수정은 아직 구현 못함 ㅠㅠ

```mysql
create table comments
(
   comment_id bigint   not null auto_increment comment '코멘트ID',
   post_id    bigint   not null comment '게시물ID',
   content    text     not null comment '내용',
   created    datetime not null comment '등록일시',
   modified   datetime null comment '수정일시',
   removed    boolean  not null default false comment '삭제여부',
   primary key (comment_id),
   foreign key (post_id) references posts (post_id)
);

alter table posts add comments int default 0 not null comment '코멘트 수';
```

## Sprint 6

### `local-docker` 프로파일 추가 : 도커의 MariaDB, Redis를 사용.

* `local` : H2(MySQL Mode)와 Embedded Redis를 사용. 서버 재시작하면 데이터 다 날라감 ㅜㅠ
* `local-docker` : 도커(아니어도 됨)에 올린 MariaDB와 Redis를 사용

### SQL 쿼리 로깅

`Exposed`에서 제공하는 로그는 맘에 들지 않아 p6spy + sql-formatter 조합으로 로그 SQL 로그 출력

### 공통 에러 페이지 구성

### 댓글 구현

1. 쓰레드로 댓글값 구현 (1000 단위 : 게시글, 1~999 : 댓글)
   1. 게시글은 최대 `long / 1000`개 등록할 수 있다. 
   2. 댓글은 게시글 하나당  최대 999개를 등록할 수 있다.
2. 등록/수정/댓글 컨트롤러 분리 (+ Thymeleaf)
3. 게시글 소프트 삭제 (삭제된 게시글은 "삭제된 게시글"로 노출)

```mysql
alter table posts add thread bigint not null comment '쓰레드';
alter table posts add depth int not null default 0 comment '들여쓰기';
alter table posts add removed boolean not null default false comment '삭제 여부';
```

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
