create table tasks
(
    id              BIGSERIAL NOT NULL UNIQUE PRIMARY KEY,
    mail            TEXT NOT NULL,
    text            TEXT NOT NULL,
    time            TIMESTAMP NOT NULL
);