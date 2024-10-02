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
       ('user', '$2a$10$a5G4QCc3YEstJEO8vd2DQuc0QEu14aQE7gQSQqj/WZA2q8iGG8rSS', 'USER');