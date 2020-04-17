CREATE TABLE question
(
    id int,
    title varchar(50),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,
    comment_count int,
    view_count int,
    like_count int,
    tag int
);