insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to, is_deleted, grading_id, access_id, options_id)
    values('Scheme #1', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);

insert into scheme_four_point(scheme_id, four_point_id) values(1, 1);