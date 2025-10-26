CREATE SCHEMA IF NOT EXISTS spring_schema;

ALTER DATABASE spring_db SET timezone TO 'UTC';
ALTER DATABASE spring_db SET default_transaction_isolation TO 'read committed';
