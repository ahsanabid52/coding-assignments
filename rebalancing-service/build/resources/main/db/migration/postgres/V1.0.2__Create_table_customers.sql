create TABLE IF NOT EXISTS customers
(
    id           UUID      NOT NULL,
    customer_id  TEXT      NOT NULL,
    email TEXT      NOT NULL,
    risk_level integer,
    retirement_age integer,
    date_of_birth DATE NOT NULL,
    selected_strategy  UUID,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_strategy FOREIGN KEY(selected_strategy) REFERENCES strategies(id)
);