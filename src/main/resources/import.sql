INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES (1, 'jimin', '1234', 'test', 'test@naver.com');
INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES (2, 'test2', '1234', 'test2', 'test2@naver.com');

INSERT INTO QUESTION (ID, WRITER_ID, TITLE, CONTENTS, CREATE_DATE, COUNT_OF_ANSWER) VALUES (1, 1,'TEST','TEST 글입니다.', CURRENT_TIMESTAMP(), 0);
INSERT INTO QUESTION (ID, WRITER_ID, TITLE, CONTENTS, CREATE_DATE, COUNT_OF_ANSWER) VALUES (2, 2,'TEST2','TEST 글입니다.', CURRENT_TIMESTAMP(), 0);

INSERT INTO ANSWER (ID, WRITER_ID, QUESTION_ID, CONTENTS, CREATE_DATE) VALUES (1, 2, 1, 'TEST답글입니다', CURRENT_TIMESTAMP());