insert into department (name, fac_id)
values ('Department #2', 1);
insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmitri.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (2, 2, 1);

insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema01.jpg', 'Schema#1', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema02.jpg', 'Schema#2', 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema03.jpg', 'Schema#3', 2, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema04.jpg', 'Schema#4', 2, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/schema05.jpg', 'Schema#5', 2, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/image01.jpg', 'Image#1', 2, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource (hyperlink, description, staff_id, width, height, last_used)
values ('https://image.slidesharecdn.com/image02.jpg', 'Image#2', 2, 640, 480, '2020-07-18 11:45:15.999999999');
