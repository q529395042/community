CREATE TABLE question
(
    id int,
    title varchar(50),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,
    commentCount int DEFAULT 0,
    viewCount int DEFAULT 0,
    likeCount int DEFAULT 0,
    tag varchar(256)

);