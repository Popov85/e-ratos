insert into department (name, fac_id) values('Department #2', 1);
insert into user (name, surname, password, email, roles)
values('Dmitri','Smirnoff','855fgUwd','dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id) values(4, 2, 1);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('custom #1', 60, 0, 1, 1, 1, 1,  0, 0, 1, 1);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('custom #2', 30, 0, 1, 1, 1, 1, 0, 0, 1, 1);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('classic', 120, 0, 1, 10, 1, 1, 0, 0, 1, 1);


insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('custom/year1', 60, 0, 1, 1, 1, 1, 0, 0, 4, 2);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('custom/year2', 30, 0, 1, 1, 1, 1, 0, 0, 4, 2);

insert into settings(name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
values('custom/foreign', 60, 0, 2, 1, 1, 1, 0, 0, 4, 2);




