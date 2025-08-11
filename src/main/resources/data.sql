INSERT INTO member(id, name, email, password) VALUES(
                                                    1,'김동률', 'kim@naver.com', '$2a$12$/zMoM2YqPTBiL.152VdKuuAsxQ83v7.U.kIA.tN3cZpEiZWXg.4Ea'
                                                );

INSERT INTO member(id, name, email, password) VALUES(
                                                    2,
                                                     '이적',
                                                    'lee@naver.com',
                                                        '$2a$12$/zMoM2YqPTBiL.152VdKuuAsxQ83v7.U.kIA.tN3cZpEiZWXg.4Ea'
                                                );

INSERT INTO member(id, name, email, password) VALUES(
                                                    3,'존박','john@naver.com', '$2a$12$/zMoM2YqPTBiL.152VdKuuAsxQ83v7.U.kIA.tN3cZpEiZWXg.4Ea'
                                                );

INSERT INTO authority(authority, member_id) VALUES('ROLE_ADMIN', 2);

INSERT INTO article(title, description, created, updated, member_id) VALUES(
                                                                               'First', 'First central', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1
                                                                           );

INSERT INTO article(title, description, created, updated, member_id) VALUES(
                                                                               'Second', 'Second central', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2
                                                                           );

INSERT INTO article(title, description, created, updated, member_id) VALUES(
                                                                               'Third', 'Third central', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3
                                                                           );

INSERT INTO article(title, description, created, updated, member_id) VALUES(
                                                                               'Forth', 'Forth central', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);
