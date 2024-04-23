insert into department (name, fac_id) values('Department #2', 1);
insert into user (name, surname, password, email, roles)
values('Dmitri','Smirnoff','855fgUwd','dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id) values(4, 2, 1);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Low grading (all courses)', 45, 0, 0, 2, 1, 1);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('High grading (all courses)', 90, 0, 0, 2, 1, 1);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Standard year 1', 60, 0, 0, 2, 1, 1);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Standard year 5', 80, 0, 0, 2, 1, 1);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Strict', 85, 0, 0, 2, 4, 2);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Strict for interns', 90, 0, 0, 2, 4, 2);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Standard year 3', 60, 0, 0, 2, 4, 2);

insert into two_point(name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
        values('Standard year 6', 80, 0, 0, 2, 4, 2);



