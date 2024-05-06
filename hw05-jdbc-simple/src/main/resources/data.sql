insert into authors(full_name)
values ('Дуглас Адамс'), ('Фёдор Достоевский'), ('Роберт Сапольски');

insert into genres(name)
values ('Научная фантастика'), ('Роман'), ('Нон-фикшн');

insert into books(title, author_id, genre_id)
values ('Автостопом по Галактике', 1, 1), ('Идиот', 2, 2), ('Биология добра и зла', 3, 3);
