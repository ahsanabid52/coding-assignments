CREATE TABLE IF NOT EXISTS hike_trails
(
    id          uuid      NOT NULL,
    name        TEXT      NOT NULL,
    start_at    time NOT NULL ,
    end_at      time NOT NULL ,
    minimum_age smallint  NOT NULL,
    maximum_age smallint  NOT NULL,
    unit_price  numeric,
    PRIMARY KEY (id)
);

insert into hike_trails(id, name, minimum_age, maximum_age, unit_price, start_at, end_at)
VALUES ('05933bea-ae6b-4f0d-9abf-a43a3381766a', 'Shire', 5, 100, 29.90, '07:00','09:00');

insert into hike_trails(id, name, minimum_age, maximum_age, unit_price, start_at, end_at)
VALUES ('3a5431cf-4c15-4d88-a91c-b21c68455362', 'Gondor', 11, 50, 59.90, '10:00','13:00');

insert into hike_trails(id, name, minimum_age, maximum_age, unit_price, start_at, end_at)
VALUES ('9444077f-ceab-47d8-a351-1909ba1bab39', 'Mordor', 18, 40, 99.90, '14:00','19:00');