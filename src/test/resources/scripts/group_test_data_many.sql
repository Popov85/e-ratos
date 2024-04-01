insert into department (name, fac_id) values('Department #2', 1);

insert into user (name, surname, password, email) values('Dmitri','Smirnoff','855fgUwd','dmirti.smirnoff@gmail.com');
insert into user_role(user_id, role_id) VALUES (4, 6);
insert into staff (staff_id, dep_id, pos_id) values(4, 2, 1);


insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('IT #25 (2d sem 18/19)', 1, 1, 1, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('Med #11 (1st sem 19/20)', 1, 1, 1, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('Farm #01 (2d sem 19/20)', 1, 1, 1, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('IT #09 (1st sem 17/18)', 0, 1, 1, 0, CURRENT_TIMESTAMP);


insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('Tech #04 (1st sem 18/19)', 1, 4, 2, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('Tech #05 (1st sem 18/19)', 1, 4, 2, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('IT #12 (2d sem 18/19)', 1, 4, 2, 0, CURRENT_TIMESTAMP);

insert into u_groups(name, is_enabled, created_by, belongs_to, is_deleted, created)
    values('IT #13 (2d sem 18/19)', 1, 4, 2, 0, CURRENT_TIMESTAMP);

insert into student_group(stud_id, group_id) values(2, 1);
insert into student_group(stud_id, group_id) values(3, 1);

