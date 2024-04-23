insert into department (name, fac_id) values('Department #2', 1);
insert into department (name, fac_id) values('Department #3', 1);

insert into user (name, surname, password, email, roles)
values('Dmitri','Smirnoff','855fgUwd','dmitri.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id) values(4, 2, 1);

insert into user (name, surname, password, email, roles)
values('Semen','Fine','pOt67n21','semen.fine@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id) values(5, 3, 1);

insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #1', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #2', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #3', 1, 1, 1, 1, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #4', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #5', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #6', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #7', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #8', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #9', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #10', 1, 1, 1, 1, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('IT theme #11', 1, 1, 1, 1, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Farm theme #1', 1, 4, 1, 2, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Farm theme #2', 1, 4, 1, 2, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Farm theme #3', 1, 4, 1, 2, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Med theme #4', 1, 4, 1, 2, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Med theme #5', 1, 4, 1, 2, CURRENT_TIMESTAMP);

insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Med theme #6', 1, 5, 1, 3, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Med theme #7', 1, 5, 1, 3, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Med theme #8', 1, 5, 1, 3, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Advanced Med theme #9 (advanced)', 1, 5, 1, 3, CURRENT_TIMESTAMP);
insert into theme (name, course_id, created_by, access_id, belongs_to, created) values('Advanced Med theme#10 (advanced)', 1, 5, 1, 3, CURRENT_TIMESTAMP);



