create table if not exists authors
(
    id        bigint auto_increment,
    full_name varchar(255),
    primary key (id)
);

create table if not exists genres
(
    id   bigint auto_increment,
    name varchar(255),
    primary key (id)
);




create table if not exists books
(
    id        bigint auto_increment,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id  bigint references genres (id) on delete cascade,
    primary key (id)
);

create table if not exists comments
(
    id        bigint auto_increment,
    comment_text varchar,
    book_id     bigint references books (id) on delete cascade,
    primary key (id)
);

CREATE TABLE public.users
(
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              username VARCHAR(255) NOT NULL UNIQUE,
                              password VARCHAR(255) NOT NULL,
                              role VARCHAR(50) NOT NULL
);