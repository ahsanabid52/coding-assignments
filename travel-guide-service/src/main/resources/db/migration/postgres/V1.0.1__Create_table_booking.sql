CREATE TABLE IF NOT EXISTS booking
(
    id            UUID      NOT NULL,
    hike_trail_id UUID      NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);