-- Users schema

-- !Ups

CREATE TABLE users (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    rollno varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

-- !Downs

DROP TABLE users;