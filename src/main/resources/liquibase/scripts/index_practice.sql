-- liquibase formatted sql

-- changeset amalashenko:1
CREATE INDEX student_name_index ON student (name);

-- changeset amalashenko:2
CREATE INDEX faculty_cn_idx ON faculty (name, color);