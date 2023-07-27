-- Drop the table if it exists
DROP TABLE IF EXISTS conversion_rules;

-- Create the conversion_rules table
CREATE TABLE conversion_rules (
    id SERIAL PRIMARY KEY,
    source_unit VARCHAR(50) NOT NULL,
    target_unit VARCHAR(50) NOT NULL,
    formula_constant DOUBLE PRECISION,
    formula VARCHAR(255) NOT NULL,
    is_metric_to_imperial BOOLEAN NOT NULL,
    CONSTRAINT unique_conversion_rule UNIQUE (source_unit, target_unit)
);