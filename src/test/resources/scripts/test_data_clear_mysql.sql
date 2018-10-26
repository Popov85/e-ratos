delete from answer_mcq_resource;
delete from answer_mq_resource;
delete from answer_sq_resource;
delete from help_resource;
delete from question_resource;
delete from fbsq_phrase;
delete from fbmq_phrase;

delete from question_help;
delete from resource;
delete from help;
delete from accepted_phrase;
delete from answer_mcq;
delete from answer_fbsq;
delete from answer_fbmq;
delete from answer_mq;
delete from answer_sq;
delete from settings_fbq;
delete from question;
delete from language;

delete from type_level;
delete from scheme_theme;
delete from theme;
delete from question_type;
delete from scheme_four_point;
delete from scheme_two_point;
delete from scheme_free_point;
delete from scheme;
delete from mode;
delete from settings;
delete from strategy;
delete from four_point;
delete from two_point;
delete from free_point;
delete from grading;

delete from course;
delete from staff;
delete from user_role;
delete from user;
delete from role;
delete from position;
delete from department;
delete from faculty;
delete from organisation;

ALTER TABLE organisation AUTO_INCREMENT = 1;
ALTER TABLE faculty AUTO_INCREMENT = 1;
ALTER TABLE department AUTO_INCREMENT = 1;
ALTER TABLE role AUTO_INCREMENT = 1;
ALTER TABLE position AUTO_INCREMENT = 1;
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE staff AUTO_INCREMENT = 1;
ALTER TABLE course AUTO_INCREMENT = 1;
ALTER TABLE scheme AUTO_INCREMENT = 1;
ALTER TABLE mode AUTO_INCREMENT = 1;
ALTER TABLE settings AUTO_INCREMENT = 1;
ALTER TABLE strategy AUTO_INCREMENT = 1;
ALTER TABLE grading AUTO_INCREMENT = 1;
ALTER TABLE four_point AUTO_INCREMENT = 1;
ALTER TABLE two_point AUTO_INCREMENT = 1;
ALTER TABLE free_point AUTO_INCREMENT = 1;
ALTER TABLE scheme_theme AUTO_INCREMENT = 1;
ALTER TABLE type_level AUTO_INCREMENT = 1;
ALTER TABLE theme AUTO_INCREMENT = 1;
ALTER TABLE question AUTO_INCREMENT = 1;
ALTER TABLE help AUTO_INCREMENT = 1;
ALTER TABLE resource AUTO_INCREMENT = 1;
ALTER TABLE language AUTO_INCREMENT = 1;
ALTER TABLE settings_fbq AUTO_INCREMENT = 1;
ALTER TABLE answer_mcq AUTO_INCREMENT = 1;
ALTER TABLE answer_fbmq AUTO_INCREMENT = 1;
ALTER TABLE accepted_phrase AUTO_INCREMENT = 1;
ALTER TABLE answer_mq AUTO_INCREMENT = 1;
ALTER TABLE answer_sq AUTO_INCREMENT = 1;
