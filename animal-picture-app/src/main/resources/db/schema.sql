CREATE TABLE IF NOT EXISTS picture_entity
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    data       OID                                     NOT NULL,
    type       SMALLINT                                NOT NULL,
    CONSTRAINT pk_pictureentity PRIMARY KEY (id)
);
