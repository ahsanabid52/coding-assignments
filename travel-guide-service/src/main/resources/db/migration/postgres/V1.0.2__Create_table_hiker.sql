CREATE TABLE IF NOT EXISTS hiker
(
    id           uuid     NOT NULL,
    booking_id   uuid     NOT NULL,
    first_name   TEXT     NOT NULL,
    last_name    TEXT     NOT NULL,
    phone_number TEXT     NOT NULL,
    age          smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking (id)
);

