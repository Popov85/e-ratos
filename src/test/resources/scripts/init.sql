INSERT INTO organisation (name) VALUES ('University');
INSERT INTO faculty (name, org_id) VALUES ('Faculty', 1);
INSERT INTO department (name, fac_id) VALUES ('Department', 1);
INSERT INTO class (name, fac_id) VALUES ('Class', 1);

INSERT INTO position (name) VALUES ('System admin');
INSERT INTO position (name) VALUES ('Dean');
INSERT INTO position (name) VALUES ('Head');
INSERT INTO position (name) VALUES ('Professor');
INSERT INTO position (name) VALUES ('Instructor');
INSERT INTO position (name) VALUES ('Researcher');
INSERT INTO position (name) VALUES ('Postgraduate');
INSERT INTO position (name) VALUES ('Lab. assistant');

INSERT INTO user (name, surname, password, email, roles, is_active) VALUES
  ('Alexei', 'Portnov', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'alexei.portnov@example.com', JSON_ARRAY('ROLE_GLOBAL_ADMIN'), 1);
INSERT INTO staff (staff_id, dep_id, pos_id) VALUES (1, 1, 1);

INSERT INTO user (name, surname, password, email, roles, is_active) VALUES
  ('Maria', 'Medvedeva', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'maria.medvedeva@example.com', JSON_ARRAY('ROLE_STUDENT'), 1);
INSERT INTO student (stud_id, class_id, fac_id, org_id, entrance_year) VALUES (2, 1, 1, 1, 2018);

INSERT INTO user (name, surname, password, email, roles, is_active) VALUES
  ('Student', 'Student', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'student.student@example.com', JSON_ARRAY('ROLE_STUDENT'), 1);
INSERT INTO student (stud_id, class_id, fac_id, org_id, entrance_year) VALUES (3, 1, 1, 1, 2018);

INSERT INTO language (name, eng_abbreviation) VALUES ('English', 'en');
INSERT INTO language (name, eng_abbreviation) VALUES ('Deutsch', 'de');
INSERT INTO language (name, eng_abbreviation) VALUES ('français', 'fr');
INSERT INTO language (name, eng_abbreviation) VALUES ('español', 'es');
INSERT INTO language (name, eng_abbreviation) VALUES ('polski', 'pl');
INSERT INTO language (name, eng_abbreviation) VALUES ('українська', 'ua');
INSERT INTO language (name, eng_abbreviation) VALUES ('русский', 'ru');

INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (1, 'MCQ', 'Multiple choice question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (2, 'FBSQ', 'Fill blank single question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (3, 'FBMQ', 'Fill blank multiple question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (4, 'MQ', 'Matcher question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (5, 'SQ', 'Sequence question');

INSERT INTO complaint_type (type_id, name, description)
VALUES (1, 'Wrong statement', 'Incorrect statement of question');
INSERT INTO complaint_type (type_id, name, description)
VALUES (2, 'Typo in question', 'Typo in question, grammatical error');
INSERT INTO complaint_type (type_id, name, description)
VALUES (3, 'Typo in answer(s)', 'Typo in one or many answer(s)');
INSERT INTO complaint_type (type_id, name, description)
VALUES (4, 'Wrong question formatting', 'Wrong question formatting: alignment, media, positioning, etc.');
INSERT INTO complaint_type (type_id, name, description)
VALUES (5, 'Wrong answer(s) formatting', 'Wrong answer(s) formatting: alignment, media, positioning, etc.');
INSERT INTO complaint_type (type_id, name, description) VALUES (6, 'Other', 'Another unnamed questionType of errors');

INSERT INTO access_level (name) VALUES ('dep-private');
INSERT INTO access_level (name) VALUES ('private');

INSERT INTO strategy (name, description)
VALUES ('default', 'Default sequence sorting strategy');
INSERT INTO strategy (name, description)
VALUES ('random', 'Random sequence sorting strategy');
INSERT INTO strategy (name, description)
VALUES ('types&levels', 'TypesThenLevels sequence sorting strategy');

INSERT INTO grading (name, description) VALUES ('four-point', 'classic 4 points grading system {2, 3, 4, 5}');
INSERT INTO grading (name, description)
VALUES ('two-point', 'classic 2 points grading system {0, 1} or {passed, not passed}');
INSERT INTO grading (name, description) VALUES ('free-point', 'universal discrete grading system {min, ..., max}');

INSERT INTO four_point (name, threshold_3, threshold_4, threshold_5, is_default, is_deleted, grading_id, created_by, belongs_to)
VALUES ('default', 50, 70, 85, 1, 0, 1, 1, 1);
INSERT INTO two_point (name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
VALUES ('default', 50, 1, 0, 2, 1, 1);
INSERT INTO free_point (name, min_value, pass_value, max_value, grading_id, is_default, created_by, belongs_to)
VALUES ('ects', 0, 60, 200, 3, 1, 1, 1);
INSERT INTO free_point (name, min_value, pass_value, max_value, grading_id, is_default, created_by, belongs_to)
VALUES ('lms', 0, 0.5, 1, 3, 1, 1, 1);

INSERT INTO settings (name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
VALUES ('default', 60, 0, 1, 1, 1, 1, 0, 1, 1, 1);

INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to, is_deleted)
VALUES ('exam', 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to, is_deleted)
VALUES ('training', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('exam', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('training', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1);

INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('eng default', 5, 100, 0, 0, 0, 1, 1);
INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('ua default', 5, 100, 0, 0, 0, 5, 1);
INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('ru default', 5, 100, 0, 0, 0, 6, 1);

INSERT INTO course (name, created, created_by, belongs_to, access_id)
VALUES ('Test LTI course #1', CURRENT_TIMESTAMP, 1, 1, 1);

INSERT INTO lti_version (version) VALUES ('1p0');

INSERT INTO lti_credentials (lti_consumer_key, lti_client_secret) VALUES ('ratos_consumer_key', 'ratos_client_secret');
INSERT INTO lti_credentials (lti_consumer_key, lti_client_secret)
VALUES ('ratos_consumer_key_1', 'ratos_client_secret_1');
INSERT INTO lti_credentials (lti_consumer_key, lti_client_secret)
VALUES ('ratos_consumer_key_2', 'ratos_client_secret_2');

INSERT INTO lms (name, lti_version_id, org_id, credentials_id) VALUES ('Open edX', 1, 1, 1);

INSERT INTO lms (name, lti_version_id, org_id, credentials_id) VALUES ('Open edX-1', 1, 1, 2);

INSERT INTO lms (name, lti_version_id, org_id, credentials_id) VALUES ('Open edX-2', 1, 1, 3);

INSERT INTO lms_course (course_id, lms_id) VALUES (1, 1);
