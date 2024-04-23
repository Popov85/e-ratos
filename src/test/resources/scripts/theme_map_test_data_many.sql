insert into department (name, fac_id)
values ('IT department #2', 1);

insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 1);

insert into course (name, created, created_by, belongs_to, access_id)
values ('IT course #1 (D1)', CURRENT_TIMESTAMP, 1, 1, 1);

insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('IT theme #1', 1, 1, 1, 1, CURRENT_TIMESTAMP);

insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #1 (T1)', 1, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #2 (T1)', 1, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #3 (T1)', 2, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #4 (T1)', 2, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #5 (T1)', 2, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #6 (T1)', 3, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #7 (T1)', 3, 1, 1);
insert into question (title, level, type_id, theme_id)
values ('Multiple choice question #8 (T1)', 3, 1, 1);

insert into question (title, level, type_id, theme_id)
values ('FBSQ question #1 (T1)', 1, 2, 1);
insert into question (title, level, type_id, theme_id)
values ('FBSQ question #2 (T1)', 2, 2, 1);
insert into question (title, level, type_id, theme_id)
values ('FBSQ question #3 (T1)', 3, 2, 1);

insert into question (title, level, type_id, theme_id)
values ('FBMQ #1 (T1)', 1, 3, 1);
insert into question (title, level, type_id, theme_id)
values ('FBMQ #2 (T1)', 1, 3, 1);
insert into question (title, level, type_id, theme_id)
values ('FBMQ #3 (T1)', 1, 3, 1);

insert into question (title, level, type_id, theme_id)
values ('Matcher question #1 (T1)', 1, 4, 1);
insert into question (title, level, type_id, theme_id)
values ('Matcher question #2 (T1)', 1, 4, 1);
insert into question (title, level, type_id, theme_id)
values ('Matcher question #3 (T1)', 1, 4, 1);
insert into question (title, level, type_id, theme_id)
values ('Matcher question #4 (T1)', 2, 4, 1);

insert into question (title, level, type_id, theme_id)
values ('Sequence question #1 (T1)', 1, 5, 1);
insert into question (title, level, type_id, theme_id)
values ('Sequence question #2 (T1)', 1, 5, 1);
insert into question (title, level, type_id, theme_id)
values ('Sequence question #3 (T1)', 3, 5, 1);






