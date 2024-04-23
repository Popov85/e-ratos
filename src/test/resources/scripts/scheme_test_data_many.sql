insert into department (name, fac_id)
values ('Department #2', 1);
insert into department (name, fac_id)
values ('Department #3', 1);

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
values ('Scheme #4', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #5', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #6 for trainee', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #7', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #8', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #9 for trainee', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #10 for trainee', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1, 0, 1, 1, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #11 for exam', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #12 for exam', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #13 for exam', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #14', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #15', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 4, 2, 0, 1, 1, 1);

insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #16', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 5, 3, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #17', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 5, 3, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #18', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 5, 3, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #19', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 5, 3, 0, 1, 1, 1);
insert into scheme(name, is_active, strategy_id, settings_id, mode_id, course_id, created, created_by, belongs_to,
                   is_deleted, grading_id, access_id, options_id)
values ('Scheme #20', 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 5, 3, 0, 1, 1, 1);



