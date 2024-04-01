-- -----------------------------------------------------
-- Schema ratos3
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS ratos3;

-- -----------------------------------------------------
-- Table organisation
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS organisation (
  org_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(500) NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (org_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table faculty
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS faculty (
  fac_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(500) NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  org_id INT  NOT NULL,
  PRIMARY KEY (fac_id),
  INDEX fk_faculty_organisation1_idx (org_id ASC),
  CONSTRAINT fk_faculty_organisation1
  FOREIGN KEY (org_id)
  REFERENCES organisation (org_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table department
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS department (
  dep_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(500) NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  fac_id INT  NOT NULL,
  PRIMARY KEY (dep_id),
  INDEX fk_department_faculty_fac_id_idx (fac_id ASC),
  CONSTRAINT fk_department_faculty_fac_id
  FOREIGN KEY (fac_id)
  REFERENCES faculty (fac_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table access_level
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS access_level (
  access_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  PRIMARY KEY (access_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table position
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS position (
  pos_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(500) NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (pos_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
  user_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  surname VARCHAR(100) NOT NULL,
  password VARCHAR(70) NOT NULL,
  email VARCHAR(200) NOT NULL,
  is_active TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (user_id),
  UNIQUE INDEX email_UNIQUE (email ASC),
  INDEX surname_and_is_active_idx (surname ASC, is_active ASC),
  INDEX name_and_is_active_idx (name ASC, is_active ASC))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table staff
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS staff (
  staff_id INT  NOT NULL,
  dep_id INT  NOT NULL,
  pos_id INT  NOT NULL,
  PRIMARY KEY (staff_id),
  INDEX fk_staff_department_dep_id_idx (dep_id ASC),
  INDEX fk_staff_position_id_idx (pos_id ASC),
  INDEX fk_staff_user_user_id_idx (staff_id ASC),
  CONSTRAINT fk_staff_department_dep_id
  FOREIGN KEY (dep_id)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_staff_position_pos_id
  FOREIGN KEY (pos_id)
  REFERENCES position (pos_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_staff_user_user_id
  FOREIGN KEY (staff_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table course
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course (
  course_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(500) NOT NULL,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  access_id INT  NOT NULL,
  created DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (course_id),
  INDEX fk_course_department_dep_id_idx (belongs_to ASC),
  INDEX fk_course_access_level_access_id_idx (access_id ASC),
  INDEX fk_course_staff_staff_id_idx (created_by ASC),
  INDEX course_department_and_name_and_is_deleted_idx (belongs_to ASC, name ASC, is_deleted ASC),
  INDEX course_department_and_created_and_is_deleted_idx (belongs_to ASC, created ASC, is_deleted ASC),
  INDEX course_staff_and_name_and_is_deleted_idx (created_by ASC, name ASC, is_deleted ASC),
  INDEX course_staff_and_created_and_is_deleted_idx (created_by ASC, created ASC, is_deleted ASC),
  UNIQUE INDEX course_department_name_UNIQUE_idx (belongs_to ASC, name ASC),
  CONSTRAINT fk_course_department_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_course_access_level_access_id
  FOREIGN KEY (access_id)
  REFERENCES access_level (access_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_course_staff_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table theme
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS theme (
  theme_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  course_id INT  NOT NULL,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  access_id INT  NOT NULL,
  created DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (theme_id),
  INDEX fk_theme_course_course_id_idx (course_id ASC),
  INDEX fk_theme_access_level_access_id_idx (access_id ASC),
  INDEX fk_theme_staff_staff_id_idx (created_by ASC),
  INDEX fk_theme_department_dep_id_idx (belongs_to ASC),
  INDEX theme_department_and_name_and_is_deleted_idx (belongs_to ASC, name ASC, is_deleted ASC),
  INDEX theme_department_and_created_and_is_deleted_idx (belongs_to ASC, created ASC, is_deleted ASC),
  INDEX theme_staff_and_name_and_is_deleted_idx (created_by ASC, name ASC, is_deleted ASC),
  INDEX theme_staff_and_created_and_is_deleted_idx (created_by ASC, created ASC, is_deleted ASC),
  UNIQUE INDEX theme_department_name_UNIQUE_idx (belongs_to ASC, name ASC),
  CONSTRAINT fk_theme_course_course_id
  FOREIGN KEY (course_id)
  REFERENCES course (course_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_theme_access_level_access_id
  FOREIGN KEY (access_id)
  REFERENCES access_level (access_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_theme_staff_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_theme_department_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table question_type
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS question_type (
  type_id INT  NOT NULL,
  eng_abbreviation VARCHAR(50) NOT NULL,
  description TEXT(1000) NOT NULL,
  PRIMARY KEY (type_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table language
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS language (
  lang_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  eng_abbreviation VARCHAR(10) NOT NULL,
  PRIMARY KEY (lang_id),
  UNIQUE INDEX eng_abbreviation_UNIQUE (eng_abbreviation ASC),
  UNIQUE INDEX name_UNIQUE (name ASC))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table question
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS question (
  question_id INT  NOT NULL AUTO_INCREMENT,
  title VARCHAR(2500) NOT NULL,
  level TINYINT NOT NULL DEFAULT 1,
  type_id INT  NOT NULL,
  theme_id INT  NOT NULL,
  is_partial TINYINT NOT NULL DEFAULT 0,
  is_required TINYINT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (question_id),
  INDEX fk_question_theme_theme_id_idx (theme_id ASC),
  INDEX fk_question_question_type_type_id_idx (type_id ASC),
  INDEX theme_and_type_and_is_deleted_idx (theme_id ASC, type_id ASC, is_deleted ASC),
  CONSTRAINT fk_question_theme_theme_id
  FOREIGN KEY (theme_id)
  REFERENCES theme (theme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_question_question_type_type_id
  FOREIGN KEY (type_id)
  REFERENCES question_type (type_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_mcq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_mcq (
  answer_id INT  NOT NULL AUTO_INCREMENT,
  answer VARCHAR(1000) NOT NULL,
  percent TINYINT NOT NULL,
  is_required TINYINT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  question_id INT  NOT NULL,
  PRIMARY KEY (answer_id),
  INDEX fk_answer_mcq_question_question_id_idx (question_id ASC),
  CONSTRAINT fk_answer_mcq_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table settings_fbq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS settings_fbq (
  set_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  words_limit INT  NOT NULL,
  symbols_limit INT  NOT NULL,
  is_numeric TINYINT NOT NULL,
  is_typo_allowed TINYINT NOT NULL,
  is_case_sensitive TINYINT NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  lang_id INT  NOT NULL,
  staff_id INT  NOT NULL,
  PRIMARY KEY (set_id),
  INDEX fk_settings_fbq_language_lang_id_idx (lang_id ASC),
  UNIQUE INDEX name_staff_UNIQUE (name ASC),
  INDEX fk_settings_fbq_staff_staff_id_idx (staff_id ASC),
  CONSTRAINT fk_settings_fbq_language_lang_id
  FOREIGN KEY (lang_id)
  REFERENCES language (lang_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_settings_fbq_staff_staff_id
  FOREIGN KEY (staff_id)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_fbsq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_fbsq (
  answer_id INT  NOT NULL,
  set_id INT  NOT NULL,
  INDEX fk_answer_fbsq_settings_set_id_idx (set_id ASC),
  PRIMARY KEY (answer_id),
  CONSTRAINT fk_answer_fbsq_settings_set_id
  FOREIGN KEY (set_id)
  REFERENCES settings_fbq (set_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_fbsq_question_answer_id
  FOREIGN KEY (answer_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table phrase
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS phrase (
  phrase_id INT  NOT NULL AUTO_INCREMENT,
  phrase VARCHAR(100) NOT NULL,
  last_used DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  staff_id INT  NOT NULL,
  PRIMARY KEY (phrase_id),
  INDEX fk_phrase_staff_staff_id_idx (staff_id ASC),
  UNIQUE INDEX phrase_staff_is_idx (phrase ASC, staff_id ASC),
  CONSTRAINT fk_phrase_staff_staff_id
  FOREIGN KEY (staff_id)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_mq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_mq (
  answer_id INT  NOT NULL AUTO_INCREMENT,
  left_phrase_id INT  NOT NULL,
  right_phrase_id INT  NOT NULL,
  question_id INT  NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (answer_id),
  INDEX fk_answer_mq_question_question_id_idx (question_id ASC),
  INDEX fk_answer_mq_phrase_left_phrase_id_idx (right_phrase_id ASC),
  INDEX fk_answer_mq_phrase_right_phrase_id_idx (left_phrase_id ASC),
  CONSTRAINT fk_answer_mq_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_mq_phrase1
  FOREIGN KEY (right_phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_mq_phrase2
  FOREIGN KEY (left_phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_sq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_sq (
  answer_id INT  NOT NULL AUTO_INCREMENT,
  phrase_id INT  NOT NULL,
  phrase_order TINYINT  NOT NULL,
  question_id INT  NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (answer_id),
  INDEX fk_answer_sq_question_question_id_idx (question_id ASC),
  INDEX fk_answer_sq_phrase_phrase_id_idx (phrase_id ASC),
  CONSTRAINT fk_answer_sq_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_sq_phrase_phrase_id
  FOREIGN KEY (phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table help
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS help (
  help_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  text VARCHAR(2000) NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  staff_id INT  NOT NULL,
  PRIMARY KEY (help_id),
  INDEX fk_help_staff_staff_id_idx (staff_id ASC),
  UNIQUE INDEX name_stafff_id_idx (name ASC, staff_id ASC),
  CONSTRAINT fk_help_staff_staff_id
  FOREIGN KEY (staff_id)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table resource
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS resource (
  resource_id INT  NOT NULL AUTO_INCREMENT,
  hyperlink VARCHAR(200) NOT NULL,
  description VARCHAR(200) NOT NULL,
  type VARCHAR(20) NOT NULL DEFAULT 'misc.',
  width INT NOT NULL,
  height INT NOT NULL,
  last_used DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  staff_id INT  NOT NULL,
  PRIMARY KEY (resource_id),
  INDEX fk_resource_staff_staff_id_idx (staff_id ASC),
  UNIQUE INDEX hyperlink_staff_id_idx (hyperlink ASC, staff_id ASC),
  CONSTRAINT fk_resource_staff_staff_id
  FOREIGN KEY (staff_id)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table fbsq_phrase
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS fbsq_phrase (
  phrase_id INT  NOT NULL,
  answer_id INT  NOT NULL,
  INDEX fk_bfsq_phrase_accepted_phrase_phrase_id_idx (phrase_id ASC),
  INDEX fk_fbsq_phrase_answer_fbsq_answer_id_idx (answer_id ASC),
  PRIMARY KEY (answer_id, phrase_id),
  CONSTRAINT fk_bfsq_phrase_accepted_phrase_phrase_id
  FOREIGN KEY (phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_fbsq_phrase_answer_fbsq_answer_id
  FOREIGN KEY (answer_id)
  REFERENCES answer_fbsq (answer_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_fbmq
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_fbmq (
  answer_id INT  NOT NULL AUTO_INCREMENT,
  phrase VARCHAR(100) NOT NULL,
  occurrence INT NOT NULL,
  set_id INT  NOT NULL,
  question_id INT  NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (answer_id),
  INDEX fk_answer_fbmq_settings_set_id_idx (set_id ASC),
  INDEX fk_answer_fbmq_question_question_id_idx (question_id ASC),
  CONSTRAINT fk_answer_fbmq_settings_set_id
  FOREIGN KEY (set_id)
  REFERENCES settings_fbq (set_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_fbmq_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table fbmq_phrase
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS fbmq_phrase (
  answer_id INT  NOT NULL,
  phrase_id INT  NOT NULL,
  INDEX fk_fbmq_phrase_answer_fbmq_answer_id_idx (answer_id ASC),
  INDEX fk_fbmq_phrase_accepted_phrase_phrase_id_idx (phrase_id ASC),
  PRIMARY KEY (answer_id, phrase_id),
  CONSTRAINT fk_fbmq_phrase_answer_fbmq_answer_id
  FOREIGN KEY (answer_id)
  REFERENCES answer_fbmq (answer_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_fbmq_phrase_accepted_phrase_phrase_id
  FOREIGN KEY (phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table help_resource
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS help_resource (
  help_id INT  NOT NULL,
  resource_id INT  NOT NULL,
  PRIMARY KEY (help_id, resource_id),
  INDEX fk_help_resource_resource_id_idx (resource_id ASC),
  CONSTRAINT fk_help_resource_help_help_id
  FOREIGN KEY (help_id)
  REFERENCES help (help_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_help_resource_resource_resource_id
  FOREIGN KEY (resource_id)
  REFERENCES resource (resource_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table question_resource
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS question_resource (
  question_id INT  NOT NULL,
  resource_id INT  NOT NULL,
  PRIMARY KEY (question_id, resource_id),
  INDEX fk_question_resource_resource_resource_id_idx (resource_id ASC),
  CONSTRAINT fk_question_resource_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_question_resource_resource_resource_id
  FOREIGN KEY (resource_id)
  REFERENCES resource (resource_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table answer_mcq_resource
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS answer_mcq_resource (
  answer_id INT  NOT NULL,
  resource_id INT  NOT NULL,
  PRIMARY KEY (answer_id, resource_id),
  INDEX fk_answer_mcq_resource_resource_resource_id_idx (resource_id ASC),
  CONSTRAINT fk_answer_mcq_resource_answer_mcq_answer_id
  FOREIGN KEY (answer_id)
  REFERENCES answer_mcq (answer_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_answer_mcq_resource_resource_resource_id
  FOREIGN KEY (resource_id)
  REFERENCES resource (resource_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table strategy
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS strategy (
  str_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description TEXT(500) NOT NULL,
  PRIMARY KEY (str_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table settings
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS settings (
  set_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  seconds_per_question INT NOT NULL DEFAULT 60 COMMENT 'Specifies general timing rule: time estimated to answer a question, session time restriction is calculated based on this value: questions*seconds_per_question\nNegative value/ zero spesifies \"not limited in time\"',
  strict_seconds_per_question TINYINT NOT NULL COMMENT 'Specifies if we should limit time for single question in this scheme',
  questions_per_sheet INT NOT NULL DEFAULT 1 COMMENT 'Negative value/ zero spesifies \"all questions per sheet/batch\"',
  days_keep_result_details INT  NOT NULL DEFAULT 1 COMMENT 'Specifies for how many days to store the extended result in the database, 1 day (24 hours by default)',
  level_2_coefficient FLOAT  NOT NULL DEFAULT 1,
  level_3_coefficient FLOAT  NOT NULL DEFAULT 1,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  is_default TINYINT NOT NULL DEFAULT 0,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  PRIMARY KEY (set_id),
  UNIQUE INDEX settings_name_dep_UNIQUE (name ASC, belongs_to ASC),
  INDEX fk_settings_created_by_staff_id_idx (created_by ASC),
  INDEX fk_settings_belongs_to_dep_id_idx (belongs_to ASC),
  CONSTRAINT fk_settings_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_settings_belongs_to_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mode
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mode (
  mode_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  is_helpable TINYINT NOT NULL DEFAULT 0,
  is_pyramid TINYINT NOT NULL DEFAULT 0,
  is_skipable TINYINT NOT NULL DEFAULT 0,
  is_rightans TINYINT NOT NULL DEFAULT 0,
  is_preservable TINYINT NOT NULL DEFAULT 0,
  is_pauseable TINYINT NOT NULL DEFAULT 0,
  is_reportable TINYINT NOT NULL DEFAULT 0 COMMENT 'Specifies if a user can report an error in a question or an answer.',
  is_starrable TINYINT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  is_default TINYINT NOT NULL DEFAULT 0,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  PRIMARY KEY (mode_id),
  UNIQUE INDEX mode_name_dep_UNIQUE (name ASC, belongs_to ASC),
  INDEX fk_mode_created_by_staff_id_idx (created_by ASC),
  INDEX fk_mode_belongs_to_dep_id_idx (belongs_to ASC),
  CONSTRAINT fk_mode_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_mode_belongs_to_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table grading
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS grading (
  grading_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description TEXT(500) NOT NULL,
  PRIMARY KEY (grading_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table options
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS options (
  opt_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  display_questions_left TINYINT NOT NULL DEFAULT 0,
  display_batches_left TINYINT NOT NULL DEFAULT 0,
  display_current_score TINYINT NOT NULL DEFAULT 0,
  display_effective_score TINYINT NOT NULL DEFAULT 0,
  display_progress TINYINT NOT NULL DEFAULT 0,
  display_motivational_messages TINYINT NOT NULL DEFAULT 0,
  display_result_score TINYINT NOT NULL DEFAULT 0,
  display_result_mark TINYINT NOT NULL DEFAULT 0,
  display_time_spent TINYINT NOT NULL DEFAULT 0,
  display_result_themes TINYINT NOT NULL DEFAULT 0,
  display_result_questions TINYINT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  is_default TINYINT NOT NULL DEFAULT 0,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  PRIMARY KEY (opt_id),
  INDEX fk_options_created_by_staff_id_idx (created_by ASC),
  INDEX fk_options_belongs_to_dep_id_idx (belongs_to ASC),
  UNIQUE INDEX options_name_dep_UNIQUE (name ASC, belongs_to ASC),
  CONSTRAINT fk_options_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_options_belongs_to_depId
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table scheme
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS scheme (
  scheme_id INT  NOT NULL AUTO_INCREMENT,
  name VARCHAR(400) NOT NULL,
  is_active TINYINT NOT NULL,
  lms_only TINYINT NOT NULL DEFAULT 0,
  strategy_id INT  NOT NULL,
  settings_id INT  NOT NULL,
  mode_id INT  NOT NULL,
  grading_id INT  NOT NULL,
  access_id INT  NOT NULL,
  course_id INT  NOT NULL,
  created_by INT  NOT NULL,
  belongs_to INT  NOT NULL,
  created DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  options_id INT  NOT NULL,
  PRIMARY KEY (scheme_id),
  INDEX fk_scheme_strategy_strategy_id_idx (strategy_id ASC),
  INDEX fk_scheme_settings_settings_id_idx (settings_id ASC),
  INDEX fk_scheme_mode_mode_id_idx (mode_id ASC),
  INDEX fk_scheme_course_course_id_idx (course_id ASC),
  UNIQUE INDEX scheme_department_name_UNIQUE_idx (belongs_to ASC, name ASC),
  INDEX fk_scheme_grading_grading_id_idx (grading_id ASC),
  INDEX fk_scheme_access_level_access_id_idx (access_id ASC),
  INDEX fk_scheme_staff_staff_id_idx (created_by ASC),
  INDEX fk_scheme_department_dep_id_idx (belongs_to ASC),
  INDEX scheme_department_and_created_and_is_deleted_idx (belongs_to ASC, created DESC, is_deleted ASC),
  INDEX scheme_staff_and_created_and_is_deleted_idx (created_by ASC, created DESC, is_deleted ASC),
  INDEX scheme_department_and_name_and_is_deleted_idx (belongs_to ASC, name ASC, is_deleted ASC),
  INDEX scheme_staff_and_name_and_is_deleted_idx (created_by ASC, name ASC, is_deleted ASC),
  INDEX fk_scheme_options_opt_id_idx (options_id ASC),
  CONSTRAINT fk_scheme_strategy_strategy_id
  FOREIGN KEY (strategy_id)
  REFERENCES strategy (str_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_settings_settings_id
  FOREIGN KEY (settings_id)
  REFERENCES settings (set_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_mode_mode_id
  FOREIGN KEY (mode_id)
  REFERENCES mode (mode_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_course_course_id
  FOREIGN KEY (course_id)
  REFERENCES course (course_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_grading_grading_id
  FOREIGN KEY (grading_id)
  REFERENCES grading (grading_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_access_level_access_id
  FOREIGN KEY (access_id)
  REFERENCES access_level (access_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_staff_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_department_depId
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_options_opt_id
  FOREIGN KEY (options_id)
  REFERENCES options (opt_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table scheme_theme
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS scheme_theme (
  scheme_theme_id INT  NOT NULL AUTO_INCREMENT,
  scheme_id INT  NOT NULL,
  theme_id INT  NOT NULL,
  theme_order TINYINT NOT NULL,
  PRIMARY KEY (scheme_theme_id),
  INDEX fk_scheme_theme_theme_theme_id_idx (theme_id ASC),
  INDEX fk_scheme_theme_scheme_scheme_id_idx (scheme_id ASC),
  CONSTRAINT fk_scheme_theme_theme_theme_id
  FOREIGN KEY (theme_id)
  REFERENCES theme (theme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_theme_scheme_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table type_level
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS type_level (
  type_level_id INT  NOT NULL AUTO_INCREMENT,
  scheme_theme_id INT  NOT NULL,
  type_id INT  NOT NULL,
  level_1 TINYINT NOT NULL,
  level_2 TINYINT NOT NULL,
  level_3 TINYINT NOT NULL,
  PRIMARY KEY (type_level_id),
  INDEX fk_type_level_scheme_theme_scheme_theme_id_idx (scheme_theme_id ASC),
  INDEX fk_type_level_question_type_type_id_idx (type_id ASC),
  CONSTRAINT fk_type_level_scheme_theme_scheme_theme_id
  FOREIGN KEY (scheme_theme_id)
  REFERENCES scheme_theme (scheme_theme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_type_level_question_type_type_id
  FOREIGN KEY (type_id)
  REFERENCES question_type (type_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role (
  role_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  PRIMARY KEY (role_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table lti_version
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lti_version (
  version_id INT NOT NULL AUTO_INCREMENT,
  version VARCHAR(20) NOT NULL,
  PRIMARY KEY (version_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table lti_credentials
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lti_credentials (
  credentials_id INT NOT NULL AUTO_INCREMENT,
  lti_consumer_key VARCHAR(1000) NOT NULL,
  lti_client_secret VARCHAR(1000) NOT NULL,
  PRIMARY KEY (credentials_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table lms
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lms (
  lms_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  lti_version_id INT NOT NULL,
  org_id INT NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  credentials_id INT NULL,
  PRIMARY KEY (lms_id),
  INDEX fk_lms_lti_version_version_id_idx (lti_version_id ASC),
  INDEX fk_lms_organisation_org_id_idx (org_id ASC),
  INDEX fk_lms_oauthv1p0_credentials_credentials_id_idx (credentials_id ASC),
  CONSTRAINT fk_lms_lti_version_version_id
  FOREIGN KEY (lti_version_id)
  REFERENCES lti_version (version_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_lms_organisation_org_id
  FOREIGN KEY (org_id)
  REFERENCES organisation (org_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_lms_oauthv1p0_credentials_credentials_id
  FOREIGN KEY (credentials_id)
  REFERENCES lti_credentials (credentials_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table result
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS result (
  result_id INT NOT NULL AUTO_INCREMENT,
  scheme_id INT NOT NULL,
  user_id INT NOT NULL,
  dep_id INT NOT NULL,
  percent DOUBLE  NOT NULL,
  grade DOUBLE  NOT NULL,
  is_passed TINYINT NOT NULL DEFAULT 0,
  is_points_granted TINYINT NOT NULL DEFAULT 0,
  session_ended DATETIME NOT NULL,
  session_lasted INT NOT NULL,
  is_timeouted TINYINT NOT NULL DEFAULT 0,
  is_cancelled TINYINT NOT NULL DEFAULT 0,
  lms_id INT  NULL DEFAULT NULL,
  PRIMARY KEY (result_id),
  INDEX fk_result_scheme_scheme_id_idx (scheme_id ASC),
  INDEX fk_result_user_user_id_idx (user_id ASC),
  INDEX fk_result_lms_lms_id_idx (lms_id ASC),
  INDEX fk_result_department_dep_id_idx (dep_id ASC),
  INDEX dep_id_and_session_ended_idx (dep_id ASC, session_ended DESC),
  INDEX user_id_and_session_ended_idx (user_id ASC, session_ended DESC),
  INDEX scheme_and_user_id_idx (scheme_id ASC, user_id ASC),
  CONSTRAINT fk_result_scheme_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_result_user_user_id
  FOREIGN KEY (user_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_result_lms_lms_id
  FOREIGN KEY (lms_id)
  REFERENCES lms (lms_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_result_department_dep_id
  FOREIGN KEY (dep_id)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table result_theme
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS result_theme (
  result_id INT NOT NULL,
  theme_id INT NOT NULL,
  percent FLOAT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (result_id, theme_id),
  INDEX fk_result_theme_theme_theme_id_idx (theme_id ASC),
  CONSTRAINT fk_result_theme_result_result_id
  FOREIGN KEY (result_id)
  REFERENCES result (result_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_result_theme_theme_theme_id
  FOREIGN KEY (theme_id)
  REFERENCES theme (theme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table result_details
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS result_details (
  detail_id INT NOT NULL,
  data LONGTEXT NOT NULL,
  when_remove DATETIME NOT NULL,
  PRIMARY KEY (detail_id),
  INDEX fk_result_details_result_details_id_idx (detail_id ASC),
  CONSTRAINT fk_result_details_result_details_id
  FOREIGN KEY (detail_id)
  REFERENCES result (result_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table session_preserved
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS session_preserved (
  uuid VARCHAR(61) NOT NULL,
  scheme_id INT NOT NULL,
  user_id INT NOT NULL,
  data LONGTEXT NOT NULL,
  when_preserved DATETIME NOT NULL,
  progress DOUBLE NOT NULL DEFAULT 0,
  PRIMARY KEY (uuid),
  INDEX fk_session_preserved_scheme_scheme_id_idx (scheme_id ASC),
  INDEX fk_session_preserved_user_user_id_idx (user_id ASC),
  CONSTRAINT fk_session_preserved_scheme_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_session_preserved_user_user_id
  FOREIGN KEY (user_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table class
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS class (
  class_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  fac_id INT NOT NULL,
  PRIMARY KEY (class_id),
  INDEX fk_class_faculty_fac_id_idx (fac_id ASC),
  CONSTRAINT fk_class_faculty_fac_id
  FOREIGN KEY (fac_id)
  REFERENCES faculty (fac_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table student
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS student (
  stud_id INT NOT NULL,
  class_id INT NOT NULL,
  fac_id INT NOT NULL,
  org_id INT NOT NULL,
  entrance_year YEAR NOT NULL COMMENT 'Year when this student/employee has entered the educational organisation.',
  PRIMARY KEY (stud_id),
  INDEX fk_student_class_class_id_idx (class_id ASC),
  INDEX fk_student_user_user_id_idx (stud_id ASC),
  INDEX fk_student_faculty_fac_id_idx (fac_id ASC),
  INDEX fk_student_organisation_org_id_idx (org_id ASC),
  CONSTRAINT fk_student_class_class_id
  FOREIGN KEY (class_id)
  REFERENCES class (class_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_student_user_user_id
  FOREIGN KEY (stud_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_student_faculty_fac_id
  FOREIGN KEY (fac_id)
  REFERENCES faculty (fac_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_student_organisation_org_id
  FOREIGN KEY (org_id)
  REFERENCES organisation (org_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table question_help
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS question_help (
  question_id INT NOT NULL,
  help_id INT NOT NULL,
  PRIMARY KEY (question_id, help_id),
  INDEX fk_question_help_help_id_idx (help_id ASC),
  CONSTRAINT fk_question_help_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_question_help_help_id
  FOREIGN KEY (help_id)
  REFERENCES help (help_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table user_role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_role (
  role_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (role_id, user_id),
  INDEX fk_user_role_user_user_id_idx (user_id ASC),
  CONSTRAINT fk_user_role_role_role_id
  FOREIGN KEY (role_id)
  REFERENCES role (role_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_user_role_user_user_id
  FOREIGN KEY (user_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table four_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS four_point (
  four_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  threshold_3 INT NOT NULL,
  threshold_4 INT NOT NULL,
  threshold_5 INT NOT NULL,
  is_default TINYINT NOT NULL,
  is_deleted TINYINT NOT NULL,
  grading_id INT NOT NULL,
  created_by INT NOT NULL,
  belongs_to INT NOT NULL,
  PRIMARY KEY (four_id),
  INDEX fk_four_point_grading_grading_id_idx (grading_id ASC),
  INDEX fk_four_point_created_by_staff_id_idx (created_by ASC),
  INDEX fk_four_point_belongs_to_dep_id_idx (belongs_to ASC),
  UNIQUE INDEX four_name_dep_UNIQUE (name ASC, belongs_to ASC),
  CONSTRAINT fk_four_point_grading_grading_id
  FOREIGN KEY (grading_id)
  REFERENCES grading (grading_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_four_point_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_four_point_belongs_to_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table scheme_four_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS scheme_four_point (
  scheme_id INT NOT NULL,
  four_point_id INT NOT NULL,
  PRIMARY KEY (scheme_id),
  INDEX fk_scheme_four_point_four_point_id_idx (four_point_id ASC),
  CONSTRAINT fk_scheme_four_point_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_four_point_four_point_id
  FOREIGN KEY (four_point_id)
  REFERENCES four_point (four_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table two_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS two_point (
  two_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  threshold INT NOT NULL,
  is_default TINYINT NOT NULL,
  is_deleted TINYINT NOT NULL,
  grading_id INT NOT NULL,
  created_by INT NOT NULL,
  belongs_to INT NOT NULL,
  PRIMARY KEY (two_id),
  INDEX fk_two_point_grading_grading_id_idx (grading_id ASC),
  INDEX fk_two_point_created_by_staff_id_idx (created_by ASC),
  INDEX fk_two_point_belongs_to_dep_id_idx (belongs_to ASC),
  UNIQUE INDEX two_name_dep_UNIQUE (name ASC, belongs_to ASC),
  CONSTRAINT fk_two_point_grading_grading_id
  FOREIGN KEY (grading_id)
  REFERENCES grading (grading_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_two_point_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_two_point_belongs_to_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table scheme_two_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS scheme_two_point (
  scheme_id INT NOT NULL,
  two_point_id INT NOT NULL,
  PRIMARY KEY (scheme_id),
  INDEX fk_scheme_two_point_scheme_scheme_id_idx (scheme_id ASC),
  CONSTRAINT fk_scheme_two_point_two_point_two_point_id
  FOREIGN KEY (two_point_id)
  REFERENCES two_point (two_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_two_point_scheme_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table free_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS free_point (
  free_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  min_value INT NOT NULL,
  pass_value INT NOT NULL,
  max_value INT NOT NULL,
  is_default TINYINT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  grading_id INT NOT NULL,
  created_by INT NOT NULL,
  belongs_to INT NOT NULL,
  PRIMARY KEY (free_id),
  INDEX fk_free_point_grading_grading_id_idx (grading_id ASC),
  INDEX fk_free_point_created_by_staff_id_idx (created_by ASC),
  INDEX fk_free_point_belongs_to_dep_id_idx (belongs_to ASC),
  UNIQUE INDEX free_name_dep_UNIQUE (name ASC, belongs_to ASC),
  CONSTRAINT fk_free_point_grading_grading_id
  FOREIGN KEY (grading_id)
  REFERENCES grading (grading_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_free_point_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_free_point_belongs_to_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table scheme_free_point
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS scheme_free_point (
  scheme_id INT NOT NULL,
  free_point_id INT NOT NULL,
  PRIMARY KEY (scheme_id),
  INDEX fk_scheme_free_point_scheme_scheme_id_idx (scheme_id ASC),
  CONSTRAINT fk_scheme_free_point_free_point_id
  FOREIGN KEY (free_point_id)
  REFERENCES free_point (free_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_scheme_free_point_scheme_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table phrase_resource
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS phrase_resource (
  phrase_id INT NOT NULL,
  resource_id INT NOT NULL,
  PRIMARY KEY (phrase_id, resource_id),
  INDEX fk_phrase_resource_resource_resource_id_idx (resource_id ASC),
  INDEX fk_phrase_resource_phrase_phrase_id_idx (phrase_id ASC),
  CONSTRAINT fk_phrase_resource_resource_resource_id
  FOREIGN KEY (resource_id)
  REFERENCES resource (resource_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_phrase_resource_phrase_phrase_id
  FOREIGN KEY (phrase_id)
  REFERENCES phrase (phrase_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table user_question_starred
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_question_starred (
  user_id INT NOT NULL,
  question_id INT NOT NULL,
  star INT NOT NULL DEFAULT 0,
  when_starred DATETIME NOT NULL,
  PRIMARY KEY (user_id, question_id),
  INDEX fk_user_question_starred_user_id_idx (user_id ASC),
  INDEX user_and_when_starred (user_id ASC, when_starred DESC),
  CONSTRAINT fk_user_question_starred_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_user_question_starred_user_id
  FOREIGN KEY (user_id)
  REFERENCES user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table lms_course
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lms_course (
  course_id INT NOT NULL,
  lms_id INT NOT NULL,
  PRIMARY KEY (course_id),
  INDEX fk_lms_course_lms_lms_id_idx (lms_id ASC),
  INDEX fk_lms_course_course_id_idx (course_id ASC),
  CONSTRAINT fk_lms_course_lms_lms_id
  FOREIGN KEY (lms_id)
  REFERENCES lms (lms_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_lms_course_course1
  FOREIGN KEY (course_id)
  REFERENCES course (course_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table groups
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS u_groups (
  group_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  is_enabled TINYINT NOT NULL DEFAULT 1,
  created DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_by INT NOT NULL,
  belongs_to INT NOT NULL,
  PRIMARY KEY (group_id),
  INDEX fk_groups_created_by_staff_id_idx (created_by ASC),
  INDEX fk_groups_department_dep_id_idx (belongs_to ASC),
  UNIQUE INDEX group_name_dep_UNIQUE (name ASC, belongs_to ASC),
  CONSTRAINT fk_groups_created_by_staff_id
  FOREIGN KEY (created_by)
  REFERENCES staff (staff_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_groups_department_dep_id
  FOREIGN KEY (belongs_to)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table student_group
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS student_group (
  stud_id INT NOT NULL,
  group_id INT NOT NULL,
  PRIMARY KEY (stud_id, group_id),
  INDEX fk_student_group_group_group_id_idx (group_id ASC),
  INDEX fk_student_group_student_stud_id_idx (stud_id ASC),
  CONSTRAINT fk_student_group_group_student_id
  FOREIGN KEY (group_id)
  REFERENCES u_groups (group_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_student_group_student_stud_id
  FOREIGN KEY (stud_id)
  REFERENCES student (stud_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table group_scheme
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS group_scheme (
  group_id INT NOT NULL,
  scheme_id INT NOT NULL,
  PRIMARY KEY (group_id, scheme_id),
  INDEX fk_group_scheme_scheme_scheme_id_idx (scheme_id ASC),
  CONSTRAINT fk_group_scheme_group_group_id
  FOREIGN KEY (group_id)
  REFERENCES u_groups (group_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_group_scheme_scheme_group_id
  FOREIGN KEY (scheme_id)
  REFERENCES scheme (scheme_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table complaint_type
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS complaint_type (
  type_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description TEXT(1000) NOT NULL,
  PRIMARY KEY (type_id))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table complaint
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS complaint (
  question_id INT NOT NULL,
  ctype_id INT NOT NULL,
  dep_id INT NOT NULL,
  last_complained DATETIME NOT NULL,
  times_complained INT NOT NULL DEFAULT 1,
  INDEX fk_complaint_department_dep_id_idx (dep_id ASC),
  INDEX fk_complaint_complaint_type_ctype_id_idx (ctype_id ASC),
  PRIMARY KEY (question_id, ctype_id),
  CONSTRAINT fk_complaint_question_question_id
  FOREIGN KEY (question_id)
  REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_complaint_department_dep_id
  FOREIGN KEY (dep_id)
  REFERENCES department (dep_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_complaint_complaint_type_ctype_id
  FOREIGN KEY (ctype_id)
  REFERENCES complaint_type (type_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table game
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game (
  stud_id INT NOT NULL,
  total_points INT NOT NULL DEFAULT 0,
  total_bonuses INT NOT NULL DEFAULT 0,
  total_weeks_won INT NOT NULL DEFAULT 0,
  PRIMARY KEY (stud_id),
  CONSTRAINT fk_game_student_stud_id
  FOREIGN KEY (stud_id)
  REFERENCES student (stud_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table game_week
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game_week (
  stud_id INT NOT NULL,
  week_points INT NOT NULL DEFAULT 0,
  week_bonuses INT NOT NULL DEFAULT 0,
  week_strike INT NOT NULL DEFAULT 0,
  week_time_spent INT NOT NULL DEFAULT 0,
  PRIMARY KEY (stud_id),
  CONSTRAINT fk_game_week_student_stud_id
  FOREIGN KEY (stud_id)
  REFERENCES student (stud_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table game_bonus
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game_bonus (
  bonus_id INT NOT NULL AUTO_INCREMENT,
  stud_id INT NOT NULL,
  bonus INT NOT NULL,
  when_granted DATETIME NOT NULL,
  PRIMARY KEY (bonus_id),
  INDEX fk_bonus_student_stud_id_idx (stud_id ASC),
  CONSTRAINT fk_bonus_student_stud_id
  FOREIGN KEY (stud_id)
  REFERENCES student (stud_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table game_week_won
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game_week_won (
  won_id INT NOT NULL AUTO_INCREMENT,
  stud_id INT NOT NULL,
  won_points INT NOT NULL,
  won_bonuses INT NOT NULL,
  won_time_spent INT NOT NULL,
  won_date DATE NOT NULL,
  PRIMARY KEY (won_id),
  INDEX fk_game_week_won_student_stud_id_idx (stud_id ASC),
  INDEX won_date_and_points_and_bonuses_and_time_spent_id (won_date ASC, won_points ASC, won_bonuses ASC, won_time_spent DESC),
  CONSTRAINT fk_game_week_won_student_stud_id
  FOREIGN KEY (stud_id)
  REFERENCES student (stud_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;