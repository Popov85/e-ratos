insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #1', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);

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

insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 1, 0);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 2, 1);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 3, 2);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 4, 3);
insert into scheme_theme(scheme_id, theme_id, theme_order)
values (1, 5, 4);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (1, 1, 5, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (2, 2, 5, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (3, 3, 5, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (4, 4, 5, 0, 0);
insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3)
values (5, 5, 5, 0, 0);

insert into u_groups(name, is_enabled, created_by, belongs_to, created)
values ('Group #1', 1, 1, 1, '2020-07-18 11:45:15.999999999');

insert into group_scheme(group_id, scheme_id)
values (1, 1);

insert into student_group(stud_id, group_id)
values (2, 1);
insert into student_group(stud_id, group_id)
values (3, 1);




