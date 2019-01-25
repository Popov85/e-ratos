insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, is_deleted, grading_id, access_id)
values('Scheme #1', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 0, 1, 1);

insert into theme (name, course_id, created_by, access_id) values('Theme #1', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #2', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #3', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #4', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #5', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #6', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #7', 1, 1, 1);
insert into theme (name, course_id, created_by, access_id) values('Theme #8', 1, 1, 1);

insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 1, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 2, 1);
insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 3, 2);
insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 4, 3);

insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 5, 4);

insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 6, 5);
insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 7, 6);
insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 8, 7);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(1, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(2, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(3, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(4, 1, 10, 0, 0);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(5, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(5, 2, 5, 0, 0);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(6, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(7, 1, 10, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(8, 1, 10, 0, 0);






