insert into department (name, fac_id)
values ('Department #2', 1);

insert into user (name, surname, password, email, roles)
values ('John', 'Emmerson', '995fgpRd', 'john.bucket@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 1, 5);

insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (5, 1, 5);

insert into user (name, surname, password, email, roles)
values ('Emma', 'Barton', '705irUwO', 'emma.barton@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (6, 1, 5);


insert into user (name, surname, password, email, roles)
values ('Isaak', 'Jefferson', '018oYEwq', 'isaak.jefferson@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (7, 2, 3);

insert into user (name, surname, password, email, roles)
values ('Amanda', 'Robertson', '715oTtwa', 'amanda.robertson@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (8, 2, 5);

insert into user (name, surname, password, email, roles)
values ('Denis', 'Suarez', 'y15opLxa', 'denis.suarez@gmail.com', JSON_ARRAY('ROLE_INSTRUCTOR'));
insert into staff (staff_id, dep_id, pos_id)
values (9, 2, 4);

insert into user (name, surname, password, email, roles)
values ('Liza', 'Bucket', '725OIysc', 'liza.soberano@gmail.com', JSON_ARRAY('ROLE_LAB_ASSISTANT'));
insert into staff (staff_id, dep_id, pos_id)
values (10, 2, 8);








