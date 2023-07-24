CREATE TABLE conversion_rules (
    id SERIAL PRIMARY KEY,
    source_unit VARCHAR(255) NOT NULL,
    target_unit VARCHAR(255) NOT NULL,
    conversion_rate DOUBLE PRECISION NOT NULL,
    constant int null,
    offset_action VARCHAR(255) null,
    CONSTRAINT uk_source_target_unit UNIQUE (source_unit, target_unit)
);