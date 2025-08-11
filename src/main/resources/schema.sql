CREATE TABLE member
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL,
    password VARCHAR(256)
);

CREATE TABLE authority(
                          id INTEGER AUTO_INCREMENT PRIMARY KEY,
                          authority VARCHAR(256),
                          member_id INTEGER,
                          FOREIGN KEY(member_id) REFERENCES member(id)
);

CREATE TABLE article(
                        id INTEGER AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(256),
                        description VARCHAR(4096),
                        created DATE,
                        updated DATE,
                        member_id INTEGER,
                        FOREIGN KEY(member_id) REFERENCES member(id)
);
