INSERT INTO organisation (name) VALUES ('Université');
INSERT INTO faculty (name, org_id) VALUES ('Faculté', 1);
INSERT INTO department (name, fac_id) VALUES ('Département', 1);
INSERT INTO class (name, fac_id) VALUES ('Classe', 1);

INSERT INTO role (name) VALUES ('ROLE_OAUTH');
INSERT INTO role (name) VALUES ('ROLE_STUDENT');
INSERT INTO role (name) VALUES ('ROLE_INSTRUCTOR');
INSERT INTO role (name) VALUES ('ROLE_LAB-ASSISTANT');
INSERT INTO role (name) VALUES ('ROLE_DEP-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_FAC-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_ORG-ADMIN');
INSERT INTO role (name) VALUES ('ROLE_GLOBAL-ADMIN');

INSERT INTO position (name) VALUES ('Administrateur système');
INSERT INTO position (name) VALUES ('Doyen');
INSERT INTO position (name) VALUES ('Chef');
INSERT INTO position (name) VALUES ('Professeur');
INSERT INTO position (name) VALUES ('Assistant');
INSERT INTO position (name) VALUES ('Chercheur');
INSERT INTO position (name) VALUES ('Étudiant');
INSERT INTO position (name) VALUES ('Laborantin');

INSERT INTO user (name, surname, password, email, is_active) VALUES
  ('Personnel', 'Personnel', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'personnel.personnel@example.com', 1);
INSERT INTO user_role (user_id, role_id) VALUES (1, 8);
INSERT INTO staff (staff_id, dep_id, pos_id) VALUES (1, 1, 1);

INSERT INTO user (name, surname, password, email, is_active) VALUES
  ('Élève', 'Élève', '{bcrypt}$2a$10$e.MY/qnalhvaoqI5QczLSuahfGbmthqd0QJh2NJ/38nB7LOZCW7d.',
   'student.student@example.com', 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO student (stud_id, class_id, fac_id, org_id, entrance_year) VALUES (2, 1, 1, 1, 2018);

INSERT INTO language (name, eng_abbreviation) VALUES ('English', 'en');
INSERT INTO language (name, eng_abbreviation) VALUES ('Deutsch', 'de');
INSERT INTO language (name, eng_abbreviation) VALUES ('français', 'fr');
INSERT INTO language (name, eng_abbreviation) VALUES ('español', 'es');
INSERT INTO language (name, eng_abbreviation) VALUES ('polski', 'pl');
INSERT INTO language (name, eng_abbreviation) VALUES ('українська', 'ua');
INSERT INTO language (name, eng_abbreviation) VALUES ('русский', 'ru');

INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (1, 'MCQ', 'Question à choix multiples');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (2, 'FBSQ', 'Remplir à blanc seule question');
INSERT INTO question_type (type_id, eng_abbreviation, description)
VALUES (3, 'FBMQ', 'Remplir à blanc plusieurs question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (4, 'MQ', 'Matcher question');
INSERT INTO question_type (type_id, eng_abbreviation, description) VALUES (5, 'SQ', 'Séquence question');

INSERT INTO complaint_type (type_id, name, description)
VALUES (1, 'Mauvaise déclaration', 'Déclaration de question incorrecte');
INSERT INTO complaint_type (type_id, name, description)
VALUES (2, 'Typo en question', 'Typo en question, erreur grammaticale');
INSERT INTO complaint_type (type_id, name, description)
VALUES (3, 'Typo en réponse(s)', 'Typo dans une ou plusieurs réponse(s)');
INSERT INTO complaint_type (type_id, name, description)
VALUES (4, 'Mauvais format de question', 'Mauvais format de question: alignement, support, positionnement, etc.');
INSERT INTO complaint_type (type_id, name, description) VALUES
  (5, 'Mauvaise réponse(s) formatée', 'Formats de réponse(s) incorrect(s): alignement, support, positionnement, etc.');
INSERT INTO complaint_type (type_id, name, description) VALUES (6, 'Autre', 'Un autre type de erreur sans nom.');

INSERT INTO access_level (name) VALUES ('dep-privé');
INSERT INTO access_level (name) VALUES ('privé');

INSERT INTO strategy (name, description)
VALUES ('défaut', 'Stratégie de tri de séquence par défaut');
INSERT INTO strategy (name, description)
VALUES ('au hasard', 'Stratégie de tri séquentiel aléatoire');
INSERT INTO strategy (name, description)
VALUES ('type&niveau', 'Stratégie de tri séquentiel types&niveaux');

INSERT INTO grading (name, description)
VALUES ('quatre-points', 'système de classement classique à 4 points {2, 3, 4, 5}');
INSERT INTO grading (name, description)
VALUES ('deux-points', 'système de classement classique à 2 points {0, 1} or {passé, ou pas passé}');
INSERT INTO grading (name, description)
VALUES ('libre-points', 'système de classement discret universel {min, ..., max}');

INSERT INTO four_point (name, threshold_3, threshold_4, threshold_5, is_default, is_deleted, grading_id, created_by, belongs_to)
VALUES ('défaut', 50, 70, 85, 1, 0, 1, 1, 1);
INSERT INTO two_point (name, threshold, is_default, is_deleted, grading_id, created_by, belongs_to)
VALUES ('défaut', 50, 1, 0, 2, 1, 1);
INSERT INTO free_point (name, min_value, pass_value, max_value, grading_id, is_default, created_by, belongs_to)
VALUES ('ECTS', 0, 60, 200, 3, 1, 1, 1);
INSERT INTO free_point (name, min_value, pass_value, max_value, grading_id, is_default, created_by, belongs_to)
VALUES ('LMS', 0, 0.5, 1, 3, 1, 1, 1);

INSERT INTO settings (name, seconds_per_question, strict_seconds_per_question, questions_per_sheet, days_keep_result_details, level_2_coefficient, level_3_coefficient, is_deleted, is_default, created_by, belongs_to)
VALUES ('défaut', 60, 0, 1, 1, 1, 1, 0, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('examen', 0, 0, 0, 0, 0, 0, 1, 1, 1, 1);
INSERT INTO mode (name, is_helpable, is_pyramid, is_skipable, is_rightans, is_preservable, is_pauseable, is_reportable, is_default, created_by, belongs_to)
VALUES ('entraînement', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('examen', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1);
INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score, display_progress, display_motivational_messages, display_result_score, display_result_mark, display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default, created_by, belongs_to)
VALUES ('entraînement', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1);

INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('eng défaut', 5, 100, 0, 0, 0, 1, 1);
INSERT INTO settings_fbq (name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id, staff_id)
VALUES ('fr défaut', 5, 100, 0, 0, 0, 1, 1);