# board-client

사용자가 이용하는 게시판 사이트

### 개발 환경 설정

프로젝트를 로컬에서 실행하기 위해 다음과 같은 서버들이 필요하다.

#### 1. MariaDB 설정

- 포트: `3306`
- 계정: `antop` / `local`
- 데이터베이스 : `toy_board`
- 스키마 파일 : [schema-mariadb.sql](src/main/resources/schema-mariadb.sql)

```mariadb
create database toy_board;
create user 'antop'@'%' identified by 'local';
grant all privileges on toy_board.* to 'antop'@'%';
```

스키마 적용:

```bash
# MariaDB CLI 사용
mysql -u antop -p toy_board < src/main/resources/schema-mariadb.sql
```

#### 2. Redis 설정

- 포트: `6379`
- 인증: 불필요
- 기본 설정으로 Redis 서버 실행

#### 3. SMTP 서버 설정

프로젝트 상위 디렉토리의 [fake-smtp 디렉터리](../fake-smtp)에서 운영체제에 맞는 스크립트를 실행

- SMTP 서버 포트: `2525`

### 애플리케이션 실행

[BoardApplication.kt](src/main/kotlin/org/antop/board/BoardApplication.kt) 클래스를 `local` 프로파일로 실행한다. (보통 개발 툴에서 시작하니까?)
