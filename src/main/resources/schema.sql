-- Drop tables if they exist to ensure a clean slate (optional, but good for create mode)
DROP TABLE IF EXISTS fire_event_record CASCADE;
DROP TABLE IF EXISTS entity_state_record CASCADE;

-- Create entity_state_record table
CREATE TABLE entity_state_record (
    id BIGSERIAL PRIMARY KEY,
    site INTEGER NOT NULL,
    application INTEGER NOT NULL,
    entity INTEGER NOT NULL,
    location_x DOUBLE PRECISION NOT NULL,
    location_y DOUBLE PRECISION NOT NULL,
    location_z DOUBLE PRECISION NOT NULL,
    timestamp BIGINT -- Explicitly BIGINT
);

-- Create fire_event_record table
CREATE TABLE fire_event_record (
    id BIGSERIAL PRIMARY KEY,
    firing_site INTEGER NOT NULL,
    firing_application INTEGER NOT NULL,
    firing_entity INTEGER NOT NULL,
    target_site INTEGER NOT NULL,
    target_application INTEGER NOT NULL,
    target_entity INTEGER NOT NULL,
    munition_site INTEGER NOT NULL,
    munition_application INTEGER NOT NULL,
    munition_entity INTEGER NOT NULL,
    timestamp BIGINT -- Explicitly BIGINT
);