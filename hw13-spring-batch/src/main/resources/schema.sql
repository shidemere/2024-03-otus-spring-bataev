create table if not exists authors
(
    id        bigint auto_increment,
    full_name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "seq_authors"
    MINVALUE 1
    INCREMENT BY 10;


create table if not exists genres
(
    id   bigint auto_increment,
    name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "seq_genres"
    MINVALUE 1
    INCREMENT BY 10;


create table if not exists books
(
    id        bigint auto_increment,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id  bigint references genres (id) on delete cascade,
    primary key (id)
);

CREATE SEQUENCE "seq_books"
    MINVALUE 1
    INCREMENT BY 10;

create table if not exists comments
(
    id        bigint auto_increment,
    comment_text varchar,
    book_id     bigint references books (id) on delete cascade,
    primary key (id)
);

CREATE SEQUENCE "seq_comments"
    MINVALUE 1
    INCREMENT BY 10;