create table if not exists posts
(
    post_id  bigint       not null auto_increment comment '게시글 ID',
    subject  varchar(255) not null comment '제목',
    content  text         not null comment '내용',
    author   varchar(100) not null comment '작성자',
    created  datetime     not null comment '등록 일시',
    modified datetime     null comment '수정 일시',
    hits     bigint       not null default 0 not null comment '조회수',
    tags     text         null comment '태그',
    thread   bigint       not null comment '쓰레드',
    depth    int          not null default 0 comment '들여쓰기',
    removed  boolean      not null default false comment '삭제 여부',
    comments int          not null default 0 comment '코멘트 수',
    primary key (post_id)
);

create table if not exists files
(
    file_id   bigint       not null auto_increment comment '파일 ID',
    file_name varchar(255) not null comment '파일명',
    file_size int          not null comment '파일 크기',
    file_type varchar(100) not null comment '파일 타입',
    directory varchar(225) not null default '~/' comment '업로드 디렉터리',
    completed boolean      not null default false comment '업로드 완료 여부',
    created   datetime     not null comment '등록 일시',
    modified  datetime     null comment '수정 일시',
    primary key (file_id)
);

create table if not exists post_files
(
    post_id bigint not null comment '게시글 ID',
    file_id bigint not null comment '파일 ID',
    primary key (post_id, file_id),
    foreign key (post_id) references posts (post_id) on delete cascade,
    foreign key (file_id) references files (file_id) on delete cascade
);

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
