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
