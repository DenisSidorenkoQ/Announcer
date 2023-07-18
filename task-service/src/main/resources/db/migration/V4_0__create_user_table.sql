create table users
(
    id              BIGSERIAL NOT NULL UNIQUE PRIMARY KEY,
    name            TEXT NOT NULL,
    mail            TEXT NOT NULL UNIQUE
);