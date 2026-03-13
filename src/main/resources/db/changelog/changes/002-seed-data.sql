--liquibase formatted sql

--changeset dripto:002-seed-data
INSERT INTO german_postcode (postcode, placename) VALUES ('12107', 'Berlin');
INSERT INTO german_postcode (postcode, placename) VALUES ('52062', 'Aachen');
INSERT INTO german_postcode (postcode, placename) VALUES ('15837', 'Klasdorf');
