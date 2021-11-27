-- Users schema

-- !Ups
CREATE TABLE "fee_details" (
    "id" bigint(255) NOT NULL AUTO_INCREMENT,
    "user_id" bigint(255) NOT NULL,
    "amount" int NOT NULL,
    "month" varchar NOT NULL,
    "year" int NOT NULL,
    "date" timestamp NOT NULL,
    PRIMARY KEY ("id")
);

-- !Downs
--DROP TABLE "users";


