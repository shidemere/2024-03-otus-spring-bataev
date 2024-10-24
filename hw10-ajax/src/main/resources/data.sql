insert into authors(full_name)
values ('Дуглас Адамс'),
       ('Фёдор Достоевский'),
       ('Роберт Сапольски');

insert into genres(name)
values ('Научная фантастика'),
       ('Роман'),
       ('Нон-фикшн');



insert into books(title, author_id, genre_id)
values ('Автостопом по Галактике', 1, 1),
       ('Идиот', 2, 2),
       ('Биология добра и зла', 3, 3);

insert into comments(COMMENT_TEXT, book_id)
values ('Форд Префект классно кинулся в окно два раза', 1),
       ('Автор какой то редкостный укурыш, книга не очень', 1),
       ('Марвина жаль', 1),
       ('Тягомотина как обычно', 2),
       ('Очень много религиозных отсылок', 2),
       ('А на деньги с этой книги он потом в игры пошёл играть', 2),
       ('Еще не читал но собираюсь', 3),
       ('Дарвинизм устарел', 3),
       ('Сапольски бесподобен как всегда', 3);


INSERT INTO public.users (username, password, role)
VALUES ('admin', '$2a$10$a5G4QCc3YEstJEO8vd2DQuc0QEu14aQE7gQSQqj/WZA2q8iGG8rSS', 'ADMIN'),
       ('user', '$2a$10$a5G4QCc3YEstJEO8vd2DQuc0QEu14aQE7gQSQqj/WZA2q8iGG8rSS', 'USER'),
       ('editor', '$2a$10$a5G4QCc3YEstJEO8vd2DQuc0QEu14aQE7gQSQqj/WZA2q8iGG8rSS', 'EDITOR');


-- data for spring acl

-- даём понять Spring ACL какими классами мы будем управлять
INSERT INTO acl_class (class, class_id_type)
VALUES  ('ru.otus.hw.model.Book', 'java.lang.Long'),
        ('ru.otus.hw.model.Author', 'java.lang.Long'),
        ('ru.otus.hw.model.Genre', 'java.lang.Long'),
        ('ru.otus.hw.model.Comment', 'java.lang.Long');

insert into acl_sid (principal, sid)
values  (true, 'admin'),
        (true, 'user'),
        (false, 'editor');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
-- ID в ACL_CLASS, ID в БД, родительский ACL, ссылка на SID владельца ACL, ставь False
VALUES  -- книги
        (/* 1 */ 1, 1, NULL, 1, false),
        (/* 2 */ 1, 2, NULL, 1, false),
        (/* 3 */ 1, 3, NULL, 1, false),
        -- авторы
        (/* 4 */ 2, 1, NULL, 1, false),
        (/* 5 */ 2, 2, NULL, 1, false),
        (/* 6 */ 2, 3, NULL, 1, false),
        -- жанры
        (/* 7 */ 3, 1, NULL, 1, false),
        (/* 8 */ 3, 2, NULL, 1, false),
        (/* 9 */ 3, 3, NULL, 1, false),
        -- комментарии
        (/* 10 */ 4, 1, NULL, 1, false),
        (/* 11 */ 4, 2, NULL, 1, false),
        (/* 12 */ 4, 3, NULL, 1, false),
        (/* 13 */ 4, 4, NULL, 1, false),
        (/* 14 */ 4, 5, NULL, 1, false),
        (/* 15 */ 4, 6, NULL, 1, false),
        (/* 16 */ 4, 7, NULL, 1, false),
        (/* 17 */ 4, 8, NULL, 1, false),
        (/* 18 */ 4, 9, NULL, 1, false);

-- Устанавливаем разрешения. На каждое разрешение должна быть отдельная стркоа
/*
    каждая комбинация acl_object_identity и ace_order должна быть уникальной, в ином случае будет ошибка
    ACE_ORDER это порядок применения правил.
    Когда система проверяет права доступа к объекту, она обрабатывает все записи из таблицы ниже связанные с этим объектом в порядке, указанном в ACE_ORDER
    Если срабатывает одно правило - остальные могут не проверяться, поэтому выставлять ACE_ORDER надо правильно
    Чем ниже значение ACE_ORDER - тем оно важнее
 */
INSERT INTO acl_entry (/*id,*/ acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES -- ссылка на сам ACL для объекта | порядковый номер правила, ставлю везде 0 | владелец, то бишь USER | сами права (1 - чтение) | разрешающее правило | логирование успеха | логгирование неудачи
       -- выдача прав user
       -- для чтения книг
       (1, 3, 2, 1, true, true, true),
       (2, 3, 2, 1, true, true, true),
       (3, 3, 2, 1, true, true, true),
       -- для чтения авторов
       (4, 3, 2, 1, true, true, true),
       (5, 3, 2, 1, true, true, true),
       (6, 3, 2, 1, true, true, true),
       -- для чтения жанров
       (7, 3, 2, 1, true, true, true),
       (8, 3, 2, 1, true, true, true),
       (9, 3, 2, 1, true, true, true),
       -- для чтения комментариев
       (10, 3, 2, 1, true, true, true),
       (11, 3, 2, 1, true, true, true),
       (12, 3, 2, 1, true, true, true),
       (13, 3, 2, 1, true, true, true),
       (14, 3, 2, 1, true, true, true),
       (15, 3, 2, 1, true, true, true),
       (16, 3, 2, 1, true, true, true),
       (17, 3, 2, 1, true, true, true),
       (18, 3, 2, 1, true, true, true),
       -- выдача прав admin
       -- для чтения, записи и создания книг (выдача этих прав в битовом эквиваленте равна числу 7)
       (1, 2, 1, 7, true, true, true),
       (2, 2, 1, 7, true, true, true),
       (3, 2, 1, 7, true, true, true),
       -- для чтения, записи и создания авторов (выдача этих прав в битовом эквиваленте равна числу 7)
       (4, 2, 1, 7, true, true, true),
       (5, 2, 1, 7, true, true, true),
       (6, 2, 1, 7, true, true, true),
       -- для чтения, записи и создания жанров (выдача этих прав в битовом эквиваленте равна числу 7)
       (7, 2, 1, 7, true, true, true),
       (8, 2, 1, 7, true, true, true),
       (9, 2, 1, 7, true, true, true),
       -- для чтения, записи и создания комментариев (выдача этих прав в битовом эквиваленте равна числу 7)
       (10, 2, 1, 7, true, true, true),
       (11, 2, 1, 7, true, true, true),
       (12, 2, 1, 7, true, true, true),
       (13, 2, 1, 7, true, true, true),
       (14, 2, 1, 7, true, true, true),
       (15, 2, 1, 7, true, true, true),
       (16, 2, 1, 7, true, true, true),
       (17, 2, 1, 7, true, true, true),
       (18, 2, 1, 7, true, true, true),
       -- выдача прав для editor
       -- для администрирования книг (выдача этих прав в битовом эквиваленте равна числу 31)
       (1, 0, 3, 31, true, true, true),
       (2, 0, 3, 31, true, true, true),
       (3, 0, 3, 31, true, true, true),
       -- для администрирования авторов (выдача этих прав в битовом эквиваленте равна числу 31)
       (4, 0, 3, 31, true, true, true),
       (5, 0, 3, 31, true, true, true),
       (6, 0, 3, 31, true, true, true),
       -- для администрирования жанров (выдача этих прав в битовом эквиваленте равна числу 31)
       (7, 0, 3, 31, true, true, true),
       (8, 0, 3, 31, true, true, true),
       (9, 0, 3, 31, true, true, true),
       -- для администрирования комментариев (выдача этих прав в битовом эквиваленте равна числу 31)
       (10, 0, 3, 31, true, true, true),
       (11, 0, 3, 31, true, true, true),
       (12, 0, 3, 31, true, true, true),
       (13, 0, 3, 31, true, true, true),
       (14, 0, 3, 31, true, true, true),
       (15, 0, 3, 31, true, true, true),
       (16, 0, 3, 31, true, true, true),
       (17, 0, 3, 31, true, true, true),
       (18, 0, 3, 31, true, true, true);

