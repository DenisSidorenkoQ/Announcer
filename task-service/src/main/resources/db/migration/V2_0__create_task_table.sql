create table tasks
(
    id              BIGSERIAL NOT NULL UNIQUE PRIMARY KEY,
    title           TEXT,
    text            TEXT NOT NULL,
    time            TIMESTAMP NOT NULL,
    user_mail       TEXT NOT NULL,
    FOREIGN KEY (user_mail) REFERENCES users (mail)  ON DELETE CASCADE ON UPDATE CASCADE
);