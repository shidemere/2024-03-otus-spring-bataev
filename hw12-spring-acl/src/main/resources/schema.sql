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

-- Tables for ACL

-- таблица для списка субъектов
CREATE TABLE IF NOT EXISTS acl_sid (
                                       id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       principal boolean NOT NULL, -- является ли субъект пользователем? 1 - пользователь / 0 - роль
                                       sid varchar NOT NULL, -- имя субъекта
                                       UNIQUE (sid,principal)
    );

-- таблица для списка классов, фигурирующих в ACL движухе
CREATE TABLE IF NOT EXISTS acl_class (
                                         id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         class varchar NOT NULL, -- имя класса, должно использоваться полное имя, вместе с пакетом.
                                         class_id_type varchar, -- имя класса идентификатора объекта. Является необязательной если в качестве идентификатора используется Long (у меня именно так)
                                         UNIQUE (class)
    );



-- Идентификация конкретного объекта для дальнейшего использования на нём ACL
CREATE TABLE IF NOT EXISTS acl_object_identity (
                                                   id bigint NOT NULL AUTO_INCREMENT,
                                                   object_id_class bigint NOT NULL , -- идентификатор класса, т.е. ссылка на табличку с классами
                                                   object_id_identity bigint NOT NULL, -- идентификатор объекта, т.е. его ID в БД
                                                   parent_object bigint DEFAULT NULL , -- ссылка на родительский ACL (а не объект) если он есть
                                                   owner_sid bigint DEFAULT NULL , -- ссылка на субъекта, который является владельцем данного ACL
                                                   entries_inheriting boolean NOT NULL, -- данный объект может наследовать правила доступа из родительского объекта, указанного в paren_object?
                                                   PRIMARY KEY (id),
    UNIQUE (object_id_class,object_id_identity),
    CONSTRAINT fk_object_id_class FOREIGN KEY (object_id_class) REFERENCES acl_class(id),
    CONSTRAINT fk_parent_object FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id),
    CONSTRAINT fk_owner_sid FOREIGN KEY (owner_sid) REFERENCES acl_sid(id)
    );

-- Описание правила доступа в рамках ACL для конкретного объекта
CREATE TABLE IF NOT EXISTS acl_entry (
                                         id bigint NOT NULL AUTO_INCREMENT,
                                         acl_object_identity bigint NOT NULL, -- ссылка на объект к которому будут применяться все дальнейшие правила
                                         ace_order int NOT NULL, -- порядковый номер правила, порядок правил имеет значение. На одного субъекта может быть навешано несколько правил. То, которое встретиться первым будет наиболее приоритетным. Пока не очень понимаю что это такое.
                                         sid bigint NOT NULL, -- ссылка на субъект
                                         mask int NOT NULL, -- является той самой маской, биты которого определяют разрешения. Должно быть 32-битным, но в демо проекта с урока использутся только 11 бит. Хз почему
                                         granting boolean NOT NULL, -- указывает на то, является ли данное правило разрешающим или запрещающим. Если 1 - правило является разрешающим. Если 0 - является запрещающим. Например если в mask стоит разрешение на чтение, а здесь стоит 0 - значит пользователю запрещено чтение
                                         audit_success boolean NOT NULL, -- нужно для аудита, говорит о срабатывании данного правила в лог
                                         audit_failure boolean NOT NULL, -- нужно для аудита, говорит о не срабатывании данного правила в лог
                                         PRIMARY KEY (id),
    UNIQUE (acl_object_identity,ace_order),
    CONSTRAINT fk_acl_object_identity FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id),
    CONSTRAINT fk_sid FOREIGN KEY (sid) REFERENCES acl_sid(id)
    );