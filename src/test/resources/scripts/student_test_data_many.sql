insert into organisation (name, is_deleted) values('Organisation #2', 0);
insert into faculty (name, org_id) values('Faculty #2', 2);
insert into department (name, fac_id) values('Department #2', 2);
insert into class (name, fac_id) values('Class #2', 2);

insert into user (name, surname, password, email, roles) values('John','Emmerson','995fgpRd','john.bucket@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(4, 1, 1, 1, 2019);

insert into user (name, surname, password, email, roles) values('Emma','Barton','705irUwO','emma.barton@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(5, 1, 1, 1, 2019);


insert into user (name, surname, password, email, roles) values('Isaak','Jefferson','018oYEwq','isaak.jefferson@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(6, 2, 1, 1, 2019);

insert into user (name, surname, password, email, roles) values('Amanda','Robertson','715oTtwa','amanda.robertson@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(7, 2, 1, 1, 2019);

insert into user (name, surname, password, email, roles) values('Denis','Suarez','y15opLxa','denis.suarez@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(8, 2, 1, 1, 2019);

insert into user (name, surname, password, email, roles) values('Liza','Bucket','725OIysc','liza.soberano@gmail.com', JSON_ARRAY('ROLE_STUDENT'));
insert into student(stud_id, class_id, fac_id, org_id, entrance_year) values(9, 2, 1, 1, 2019);








