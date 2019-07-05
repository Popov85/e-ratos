insert into scheme(name, is_active, strategy_id, settings_id, mode_id, grading_id, course_id, created, created_by, belongs_to, is_deleted, access_id)
    values('Sample scheme #1', 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1);

insert into scheme_four_point(scheme_id, four_point_id) values(1, 1);

insert into theme (name, course_id, created_by, belongs_to, access_id) values('Sample theme #1', 1, 1, 1, 1);

insert into scheme_theme(scheme_id, theme_id, theme_order) values(1, 1, 0);

insert into type_level(scheme_theme_id, type_id, level_1, level_2, level_3) values(1, 2, 10, 0, 0);