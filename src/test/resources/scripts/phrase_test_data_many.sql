insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (2, 1, 1);

insert into phrase(phrase, staff_id, last_used)
values ('access', 1, '2018-07-18 10:30:15.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('assert', 1, '2018-06-06 15:02:13.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('implements', 1, '2018-06-20 12:59:10.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('extends', 2, '2018-04-01 08:11:44.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('container', 1, '2018-06-27 13:35:10.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('transaction', 2, '2018-06-27 13:35:10.999999999');
insert into phrase(phrase, staff_id, last_used)
values ('transformer', 2, '2018-06-27 13:35:10.999999999');

insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/1', 'Res #1', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/2', 'Res #2', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/3', 'Res #3', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/4', 'Res #4', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/5', 'Res #5', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/6', 'Res #6', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');
insert into resource(hyperlink, description, is_deleted, staff_id, width, height, last_used)
values ('http://res/7', 'Res #7', 0, 1, 640, 480, '2020-07-18 11:45:15.999999999');

insert into phrase_resource(phrase_id, resource_id)
values (1, 1);
insert into phrase_resource(phrase_id, resource_id)
values (2, 2);
insert into phrase_resource(phrase_id, resource_id)
values (3, 3);
insert into phrase_resource(phrase_id, resource_id)
values (4, 4);
insert into phrase_resource(phrase_id, resource_id)
values (5, 5);
insert into phrase_resource(phrase_id, resource_id)
values (6, 6);
insert into phrase_resource(phrase_id, resource_id)
values (7, 7);


