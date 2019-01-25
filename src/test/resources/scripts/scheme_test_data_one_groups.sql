insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, is_deleted, grading_id, access_id)
values('Scheme #1', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 0, 1, 1);

insert into groups(name, is_enabled, staff_id) values('Group #1', 1, 1);
insert into groups(name, is_enabled, staff_id) values('Group #2', 1, 1);
insert into groups(name, is_enabled, staff_id) values('Group #3', 1, 1);

insert into group_scheme(group_id, scheme_id) values(1, 1);
insert into group_scheme(group_id, scheme_id) values(2, 1);

