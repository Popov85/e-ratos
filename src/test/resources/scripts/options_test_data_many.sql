INSERT INTO department (name, fac_id)
VALUES ('Department #2', 1);
INSERT INTO user (name, surname, password, email, roles)
VALUES ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
INSERT INTO staff (staff_id, dep_id, pos_id)
VALUES (4, 2, 1);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('custom #1', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('custom #2', 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('classic', 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1);


INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('custom/year1', 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 4, 2);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('custom/year2', 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 4, 2);

INSERT INTO options (name, display_questions_left, display_batches_left, display_current_score, display_effective_score,
                     display_progress, display_motivational_messages, display_result_score, display_result_mark,
                     display_time_spent, display_result_themes, display_result_questions, is_deleted, is_default,
                     created_by, belongs_to)
VALUES ('custom/foreign', 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 4, 2);




