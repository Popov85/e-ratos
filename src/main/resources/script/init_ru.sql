INSERT INTO organisation (name) VALUES ('Университет');
INSERT INTO faculty (name, org_id) VALUES ('Факультет', 1);
INSERT INTO department (name, fac_id) VALUES ('Кафедра', 1);
INSERT INTO class (name, fac_id) VALUES ('Группа', 1);

INSERT INTO role (name) VALUES ('ROLE_OAUTH');
INSERT INTO role (name) VALUES ('ROLE_STUDENT');
INSERT INTO role (name) VALUES ('ROLE_INSTRUCTOR');
INSERT INTO role (name) VALUES ('ROLE_LAB-ASSISTANT');
INSERT INTO role (name) VALUES ('ROLE_DEP-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_FAC-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_ORG-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_GLOBAL-ADMIN');

INSERT INTO position (name) VALUES ('Сис. админ');
INSERT INTO position (name) VALUES ('Декан');
INSERT INTO position (name) VALUES ('Зав. каф.');
INSERT INTO position (name) VALUES ('Профессор');
INSERT INTO position (name) VALUES ('Преподаватель');
INSERT INTO position (name) VALUES ('Исследователь');
INSERT INTO position (name) VALUES ('Аспирант');
INSERT INTO position (name) VALUES ('Лаборант');

INSERT INTO user (name, surname, password, email, is_active) VALUES
  ('Персонал', 'Персонал', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'staff.staff@example.com', 1);
INSERT INTO user_role (user_id, role_id) VALUES (1, 8);
INSERT INTO staff (staff_id, dep_id, pos_id) VALUES (1, 1, 1);

INSERT INTO user (name, surname, password, email, is_active) VALUES
  ('Студент', 'Студент', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'student.student@example.com', 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO student (stud_id, class_id, fac_id, org_id, entrance_year) VALUES (2, 1, 1, 1, 2019);

INSERT INTO language (name, eng_abbreviation) VALUES ('English', 'en');
INSERT INTO language (name, eng_abbreviation) VALUES ('Deutsch', 'de');
INSERT INTO language (name, eng_abbreviation) VALUES ('français', 'fr');
INSERT INTO language (name, eng_abbreviation) VALUES ('español', 'es');
INSERT INTO language (name, eng_abbreviation) VALUES ('polski', 'pl');
INSERT INTO language (name, eng_abbreviation) VALUES ('українська', 'ua');
INSERT INTO language (name, eng_abbreviation) VALUES ('русский', 'ru');

INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (1, 'ВМВ', 'Вопрос со множественным выбором');
INSERT INTO question_type (type_id, eng_abbreviation, description)
VALUES (2, 'ВЗОП', 'Вопрос на заполнение одного пропуска');
INSERT INTO question_type (type_id, eng_abbreviation, description)
VALUES (3, 'ВЗМП', 'Вопрос на заполнение множества пропусков');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (4, 'ВС', 'Вопрос на сопоставление');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (5, 'ВП', 'Вопрос на последовательность');

INSERT INTO complaint_type (type_id, name, description)
VALUES (1, 'Неверная постановка вопроса', 'Неверная или некоректная поставнка вопроса');
INSERT INTO complaint_type (type_id, name, description)
VALUES (2, 'Опечатка в вопросе', 'Опечатка или грамматическая ошибка в загловоке вопроса');
INSERT INTO complaint_type (type_id, name, description)
VALUES (3, 'Опечатка в ответе(ах)', 'Опечатка или грамматическая ошибка в одном или нескольких вариантах ответов');
INSERT INTO complaint_type (type_id, name, description) VALUES (4, 'Ошибка в форматирования вопроса',
                                                                'Неверное выравнивание текста, не отображается мультимедиа, неправильное размещение фрагмента, пр.');
INSERT INTO complaint_type (type_id, name, description) VALUES (5, 'Ошибка в форматирования ответа(ов)',
                                                                'Неверное выравнивание текста, не отображается мультимедиа, неправильное размещение фрагмента, пр.');
INSERT INTO complaint_type (type_id, name, description) VALUES (6, 'Другое', 'Другой тип проблемы');

INSERT INTO access_level (name) VALUES ('каф.-приватный');
INSERT INTO access_level (name) VALUES ('приватный');

INSERT INTO strategy (name, description)
VALUES ('по-умолчанию', 'Стратегия сортировки последовательсноти вопросов по умолчанию');
INSERT INTO strategy (name, description)
VALUES ('случайная', 'Случайная стратегия сортировки последовательности вопросов');
INSERT INTO strategy (name, description)
VALUES ('тип&уровень', 'Сортировка последовательсности вопросов по типу затем по уровню сложности');

INSERT INTO grading (name, description)
VALUES ('4-х бальная', 'Классическая 4-х бальная шкала оценивания {2, 3, 4, 5}');
INSERT INTO grading (name, description)
VALUES ('2-х бальная', 'Классическая 2-х бальная шкала оценивания {0, 1} или {зачтено, не зачтено}');
INSERT INTO grading (name, description)
VALUES ('универсальная', 'Универсальная дискретная шкала оценивания {min, ..., max}');

INSERT INTO four_point (name, threshold_3, threshold_4, threshold_5, staff_id, is_default, is_deleted, grading_id)
VALUES ('4-х бальная по-умолчанию', 50, 70, 85, 1, 1, 0, 1);
INSERT INTO two_point (name, threshold, staff_id, is_default, is_deleted, grading_id)
VALUES ('2-х бальная по-умолчанию', 50, 1, 1, 0, 2);
INSERT INTO free_point (name, min_value, pass_value, max_value, staff_id, grading_id)
VALUES ('ECTS', 0, 60, 200, 1, 3);
INSERT INTO free_point (name, min_value, pass_value, max_value, staff_id, grading_id)
VALUES ('LMS', 0, 0.5, 1, 1, 3);

INSERT INTO settings (name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
VALUES ('по-умолчанию', 60, 0, 1, 1, 1, 1, 0, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('экзамен', 0, 0, 0, 0, 0, 0, 1, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('тренировка', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('экзамен', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('тренировка', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1);

INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('eng по-умолчанию', 5, 100, 0, 0, 0, 1, 1);
INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('ua по-умолчанию', 5, 100, 0, 0, 0, 2, 1);
INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('ru по-умолчанию', 5, 100, 0, 0, 0, 3, 1);

