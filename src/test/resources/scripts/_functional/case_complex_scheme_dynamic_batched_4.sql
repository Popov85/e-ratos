insert into mode(name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_reportable, is_default,
                 created_by, belongs_to)
values ('training2', 1, 0, 1, 1, 1, 1, 0, 1, 1);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet,
                     days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default,
                     created_by, belongs_to)
values ('default2', 60, 0, 4, 1, 1, 1, 0, 1, 1, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, grading_id, course_id, created, created_by,
                   belongs_to, is_deleted, access_id, options_id)
values ('Sample scheme #1', 1, 1, 2, 3, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 2);

insert into scheme_four_point(scheme_id, four_point_id)
values (1, 1);

insert into theme (name, course_id, created_by, belongs_to, access_id, created)
values ('Sample theme #1 (All MCQ)', 1, 1, 1, 1, '2020-07-18 11:45:15.999999999');
insert into theme (name, course_id, created_by, belongs_to, access_id, created)
values ('Sample theme #2 (All FBSQ)', 1, 1, 1, 1, '2020-07-18 11:45:15.999999999');
insert into theme (name, course_id, created_by, belongs_to, access_id, created)
values ('Sample theme #3 (All FBMQ)', 1, 1, 1, 1, '2020-07-18 11:45:15.999999999');
insert into theme (name, course_id, created_by, belongs_to, access_id, created)
values ('Sample theme #4 (All MQ)', 1, 1, 1, 1, '2020-07-18 11:45:15.999999999');
insert into theme (name, course_id, created_by, belongs_to, access_id, created)
values ('Sample theme #5 (All SQ)', 1, 1, 1, 1, '2020-07-18 11:45:15.999999999');

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
