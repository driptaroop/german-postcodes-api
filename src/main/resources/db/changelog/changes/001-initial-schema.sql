--liquibase formatted sql

--changeset dripto:001-initial-schema
CREATE TABLE german_postcode (
    postcode  VARCHAR(10)  NOT NULL,
    placename VARCHAR(255) NOT NULL,
    CONSTRAINT pk_german_postcode PRIMARY KEY (postcode)
);
