create TABLE IF NOT EXISTS strategies
(
    id                  uuid      NOT NULL,
    strategy_id   TEXT      NOT NULL,
    min_risk_level integer   NOT NULL,
    max_risk_level          integer      NOT NULL,
    min_years_to_retirement   integer      NOT NULL,
    max_years_to_retirement integer   NOT NULL,
    stocks_percentage          numeric      NOT NULL,
    cash_percentage          numeric      NOT NULL,
    bonds_percentage          numeric      NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);