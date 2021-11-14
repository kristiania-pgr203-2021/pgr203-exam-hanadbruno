DROP TABLE IF EXISTS answers;
DROP SEQUENCE IF EXISTS question_db.public.answers_id_seq;
create table answers(
                        id serial primary key ,
                        answer_text varchar(100),
                        answer_alternative varchar(100)


);


