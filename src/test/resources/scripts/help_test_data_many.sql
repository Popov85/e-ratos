insert into department (name, fac_id)
values ('Department #2', 1);
insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (2, 2, 1);
insert into course (name, created, created_by, belongs_to, access_id)
values ('Test course #2', CURRENT_TIMESTAMP, 1, 1, 1);

insert into help(name, text, staff_id)
values ('help name #1', 'Please, refer to schema #1', 1);
insert into help(name, text, staff_id)
values ('assist grading #2', 'Please, refer to schema #2', 1);
insert into help(name, text, staff_id)
values ('assist grading #3', 'Please, refer to schema #3', 1);
insert into help(name, text, staff_id)
values ('help name #4', 'Please, refer to schema #4', 2);
insert into help(name, text, staff_id)
values ('help name #5', 'Please, refer to schema #5', 2);
insert into help(name, text, staff_id)
values ('help name #6', 'Please, refer to schema #6', 2);
insert into help(name, text, staff_id)
values ('help name #7', 'Please, refer to schema #7', 2);

insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema01.jpg', 'Schema#1', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema02.jpg', 'Schema#2', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema03.jpg', 'Schema#3', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema04.jpg', 'Schema#4', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema05.jpg', 'Schema#5', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema06.jpg', 'Schema#6', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema07.jpg', 'Schema#7', 1, 640, 480, '2020-07-18 11:45:15.999999999');

insert into help_resource(help_id, resource_id)
values (1, 1);
insert into help_resource(help_id, resource_id)
values (2, 2);
insert into help_resource(help_id, resource_id)
values (3, 3);
insert into help_resource(help_id, resource_id)
values (4, 4);
insert into help_resource(help_id, resource_id)
values (5, 5);
insert into help_resource(help_id, resource_id)
values (6, 6);
insert into help_resource(help_id, resource_id)
values (7, 7);

