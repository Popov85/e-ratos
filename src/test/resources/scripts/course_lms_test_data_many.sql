insert into department (name, fac_id)
values ('Department #2', 1);

insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmitri.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 1);

insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #1', '2019-01-20 12:33:20.999999999', 1, 1, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #2', '2019-01-20 12:33:20.999999999', 1, 1, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #3 (edX)', '2019-01-20 12:33:20.999999999', 1, 1, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #4', '2019-01-20 12:33:20.999999999', 1, 1, 0, 1);

insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #5', '2019-01-20 12:33:20.999999999', 4, 2, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #6 (lti)', '2019-01-20 12:33:20.999999999', 4, 2, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #7', '2019-01-20 12:33:20.999999999', 4, 2, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #8', '2019-01-20 12:33:20.999999999', 4, 2, 0, 1);
insert into course(name, created, created_by, belongs_to, is_deleted, access_id)
values ('Course #9', '2019-01-20 12:33:20.999999999', 4, 2, 0, 1);

insert into lms_course(course_id, lms_id)
values (2, 1);
insert into lms_course(course_id, lms_id)
values (3, 1);
insert into lms_course(course_id, lms_id)
values (4, 1);
insert into lms_course(course_id, lms_id)
values (5, 1);

insert into lms_course(course_id, lms_id)
values (6, 2);
insert into lms_course(course_id, lms_id)
values (7, 2);
insert into lms_course(course_id, lms_id)
values (8, 2);
insert into lms_course(course_id, lms_id)
values (9, 2);
insert into lms_course(course_id, lms_id)
values (10, 2);






