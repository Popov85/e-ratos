insert into department (name, fac_id)
values ('Department #2', 1);
insert into user (name, surname, password, email, roles)
values ('Dmitri', 'Smirnoff', '855fgUwd', 'dmirti.smirnoff@gmail.com', JSON_ARRAY('ROLE_FAC_ADMIN'));
insert into staff (staff_id, dep_id, pos_id)
values (4, 2, 1);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('One word universal', 1, 25, 0, 0, 1, 1, 1);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Two word universal', 2, 50, 0, 0, 1, 1, 1);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Sentence common', 10, 200, 0, 0, 1, 1, 1);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Sentence short', 5, 100, 0, 0, 1, 1, 1);


insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Two-word strict', 2, 40, 0, 0, 1, 1, 4);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Single number', 1, 4, 1, 0, 1, 1, 4);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Two-words short', 2, 20, 0, 0, 1, 1, 4);

insert into settings_fbq(name, words_limit, symbols_limit, is_numeric, is_typo_allowed, is_case_sensitive, lang_id,
                         staff_id)
values ('Long number', 1, 10, 1, 0, 1, 1, 4);


