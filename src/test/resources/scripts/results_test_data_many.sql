insert into department (name, fac_id)
values ('Department #2', 1);
insert into course (name, created, created_by, belongs_to, access_id)
values ('Test course #2', CURRENT_TIMESTAMP, 1, 2, 1);

insert into user (name, surname, password, email, roles, is_active)
values ('Max', 'Smirnoff', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
        'max.smirnoff@example.com', JSON_ARRAY('ROLE_INSTRUCTOR'), 1);
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 5);

insert into user (name, surname, password, email, roles, is_active)
values ('Maria', 'Dubrovska', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
        'maria.dubrovska@example.com', JSON_ARRAY('ROLE_STUDENT'), 1);
insert into student (stud_id, class_id, fac_id, org_id, entrance_year)
values (5, 1, 1, 1, 2018);

insert into user (name, surname, password, email, roles, is_active)
values ('Denis', 'Kosinski', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
        'denis.kosiniski@example.com', JSON_ARRAY('ROLE_INSTRUCTOR'), 1);
insert into staff (staff_id, dep_id, pos_id)
values (6, 2, 5);

insert into user (name, surname, password, email, roles, is_active)
values ('Den', 'Abramov', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
        'den.abramov@example.com', JSON_ARRAY('ROLE_STUDENT'), 1);
insert into student (stud_id, class_id, fac_id, org_id, entrance_year)
values (7, 1, 1, 1, 2020);

insert into course (name, created, created_by, belongs_to, access_id)
values ('Test course #3', CURRENT_TIMESTAMP, 6, 2, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #1', 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #2', 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #3', 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #4', 1, 1, 1, 1, 1, 2, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #5', 1, 1, 1, 1, 1, 2, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #6', 1, 1, 1, 1, 1, 3, CURRENT_TIMESTAMP, 6, 2, 0, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, options_id, course_id, created, created_by,
                   belongs_to, is_deleted, grading_id, access_id)
values ('Scheme #7', 1, 1, 1, 1, 1, 3, CURRENT_TIMESTAMP, 6, 2, 0, 1, 1);


insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (1, 2, 1, 90, 5, 1, '2018-12-20 13:35:07.999999999', 400, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (1, 3, 1, 85, 5, 1, '2018-12-24 10:39:14.999999999', 500, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (2, 2, 1, 75, 4, 1, '2018-12-25 11:55:36.999999999', 288, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (2, 3, 1, 88, 5, 1, '2018-12-29 14:44:16.999999999', 445, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (3, 2, 1, 66, 3, 1, '2018-12-30 09:49:38.999999999', 411, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (3, 3, 1, 92, 5, 1, '2019-01-15 08:23:55.999999999', 181, 0, 0, null);

insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (4, 2, 2, 82, 5, 1, '2019-01-20 17:49:30.999999999', 540, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (4, 3, 2, 79, 4, 1, '2019-01-25 09:52:45.999999999', 287, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (5, 2, 2, 86, 5, 1, '2019-01-28 11:08:09.999999999', 435, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (5, 3, 2, 62, 3, 1, '2019-01-29 08:00:17.999999999', 419, 0, 0, null);

insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (6, 5, 2, 85, 5, 1, '2019-01-30 11:08:09.999999999', 399, 0, 0, null);
insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, session_ended, session_lasted, is_timeouted,
                   is_cancelled, lms_id)
values (7, 5, 2, 60, 3, 1, '2019-01-31 08:00:17.999999999', 411, 0, 1, null);


insert into result(scheme_id, user_id, dep_id, percent, grade, is_passed, is_points_granted, session_ended,
                   session_lasted, is_timeouted, is_cancelled, lms_id)
values (7, 7, 2, 86, 5, 1, 1, '2019-09-22 09:50:29.999999999', 51, 0, 0, 1);


insert into result_details(detail_id, data, when_remove)
values (1, 'JSON DATA', '2050-09-22 12:00:00.000000000');
insert into result_details(detail_id, data, when_remove)
values (2, 'JSON DATA', '2050-09-22 09:50:29.999999999');
insert into result_details(detail_id, data, when_remove)
values (3, 'JSON DATA', '2050-09-22 09:50:29.999999999');
insert into result_details(detail_id, data, when_remove)
values (4, 'JSON DATA', '2050-09-22 09:50:29.999999999');
insert into result_details(detail_id, data, when_remove)
values (5, 'JSON DATA', '2050-09-22 09:50:29.999999999');
insert into result_details(detail_id, data, when_remove)
values (6, 'JSON DATA', '2050-09-22 09:50:29.999999999');





