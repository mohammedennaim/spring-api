-- Script d'initialisation de la base de données
-- Ce script sera exécuté automatiquement lors du premier démarrage du conteneur PostgreSQL

-- Création de la base de données si elle n'existe pas
-- (PostgreSQL crée automatiquement la base spécifiée dans POSTGRES_DB)

-- Création d'un schéma pour l'application
CREATE SCHEMA IF NOT EXISTS spring_schema;

-- Création d'utilisateur spécifique pour l'application (optionnel)
-- CREATE USER spring_user WITH PASSWORD 'spring_password';
-- GRANT ALL PRIVILEGES ON DATABASE spring_db TO spring_user;
-- GRANT ALL PRIVILEGES ON SCHEMA spring_schema TO spring_user;

-- Configuration des paramètres de la base de données
ALTER DATABASE spring_db SET timezone TO 'UTC';
ALTER DATABASE spring_db SET default_transaction_isolation TO 'read committed';

-- Commentaires
COMMENT ON DATABASE spring_db IS 'Base de données pour l''application Spring MVC REST API';
COMMENT ON SCHEMA spring_schema IS 'Schéma principal de l''application Spring';
