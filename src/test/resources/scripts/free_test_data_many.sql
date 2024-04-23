insert into department (name, fac_id)
values ('Department #2', 1);
insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 1);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Low grading (all courses)', 0, 100, 200, 3, 1, 1);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('High grading (all courses)', 0, 150, 200, 3, 1, 1);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Standard year 1', 0, 90, 200, 3, 1, 1);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Standard year 5', 0, 140, 200, 3, 1, 1);


insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Strict', 0, 180, 200, 3, 4, 2);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Strict for interns', 0, 170, 200, 3, 4, 2);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Standard year 3', 0, 90, 200, 3, 4, 2);

insert into free_point(name, min_value, pass_value, max_value, grading_id, created_by, belongs_to)
values ('Standard year 6', 0, 140, 200, 3, 4, 2);




