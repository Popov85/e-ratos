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
    ('Global', 'Admin', '{bcrypt}$2a$10$W15Rq6z2vplUn4ociodYA.LaJ/atSVsNRicZupLe4qVDJ.rXCgg9C', 'global.admin@example.com', JSON_ARRAY('ROLE_GLOBAL_ADMIN'), 1);
INSERT INTO staff (staff_id, dep_id, pos_id) VALUES (1, 1, 1);

INSERT INTO user (name, surname, password, email, roles, is_active) VALUES
    ('Student', 'Student', '{bcrypt}$2a$10$c3lFu8vPdAtda.FFJgQXoOU.y9QKaHmlrML1dwx6CJt687aWybNEy', 'student@example.com', JSON_ARRAY('ROLE_STUDENT'), 1);
INSERT INTO student (stud_id, class_id, fac_id, org_id, entrance_year) VALUES (2, 1, 1, 1, 2024);

INSERT INTO language (name, eng_abbreviation) VALUES ('English', 'en');

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
INSERT INTO complaint_type (type_id, name, description) VALUES (6, 'Other', 'Another unnamed type of errors');

INSERT INTO access_level (name) VALUES ('dep-private');
INSERT INTO access_level (name) VALUES ('private');

INSERT INTO strategy (name, description)
VALUES ('default', 'Default sequence sorting strategy');
INSERT INTO strategy (name, description)
VALUES ('random', 'Random sequence sorting strategy');
INSERT INTO strategy (name, description)
VALUES ('types&levels', 'Types->Levels sequence sorting strategy');

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
INSERT INTO settings (name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
VALUES ('batched', 60, 0, 1, 3, 1, 1, 0, 0, 1, 1);
INSERT INTO settings (name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
VALUES ('non_limited', 0, 0, 0, 3, 1, 1, 0, 0, 1, 1);

INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('exam', 0, 0, 0, 0, 0, 0, 1, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('exam_mock', 1, 0, 0, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('dynamic', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('exam', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('exam_mock', 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('training', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1);

INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('eng default', 5, 100, 0, 0, 0, 1, 1);