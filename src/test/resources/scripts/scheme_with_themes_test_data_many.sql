insert into department (name, fac_id)
values ('Department #2', 1);
insert into department (name, fac_id)
values ('Department #3', 1);

insert into course (name, created, created_by, belongs_to, access_id)
values ('Test course #2', CURRENT_TIMESTAMP, 1, 2, 1);

insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmitri.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 1);

insert into user (name, surname, password, email, roles)
values ('Semen', 'Fine', 'pOt67n21', 'semen.fine@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (5, 3, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #1', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #2', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #3 for trainee', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #4', 1, 1, 1, 1, 2, CURRENT_TIMESTAMP, 1, 2, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #5', 1, 1, 1, 1, 2, CURRENT_TIMESTAMP, 1, 2, 0, 1, 1, 1);


insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #1', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #2', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #3', 1, 1, 1, 1, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #4', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #5', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #6', 1, 1, 1, 1, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #7', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #8', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #9', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created)
values ('Theme #10', 1, 1, 1, 1, CURRENT_TIMESTAMP);


insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 1, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (2, 2, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (3, 3, 0);

insert into scheme_theme(scheme_id, theme_id, theme_order)
values (4, 4, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (4, 5, 1);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (4, 6, 2);

insert into scheme_theme(scheme_id, theme_id, theme_order)
values (5, 7, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (5, 8, 1);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (5, 9, 2);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (5, 10, 3);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (1, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (2, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (3, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (4, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (5, 1, 10, 0, 0);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (6, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (7, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (8, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (9, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (10, 1, 10, 0, 0);






