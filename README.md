# Spring MVC REST API avec PostgreSQL

Ce projet est une application Spring MVC REST API qui utilise PostgreSQL comme base de données, sans Spring Boot.

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── springapi/
│   │               ├── config/          # Configuration Spring
│   │               ├── controller/      # Contrôleurs REST
│   │               ├── model/           # Entités JPA
│   │               ├── repository/      # Repositories
│   │               └── service/         # Services métier
│   ├── resources/
│   │   ├── META-INF/
│   │   │   └── persistence.xml         # Configuration JPA
│   │   ├── application.properties      # Configuration principale
│   │   ├── application-docker.properties # Configuration Docker
│   │   └── messages.properties         # Messages internationaux
│   └── webapp/
│       └── WEB-INF/
│           └── web.xml                 # Configuration web.xml
```

## Technologies utilisées

- **Java 17**
- **Spring Framework 6.1.5** (MVC, ORM, Context)
- **Hibernate 6.4.0** (JPA Provider)
- **PostgreSQL 16** (Base de données)
- **Apache Commons DBCP2** (Pool de connexions)
- **Jackson** (Sérialisation JSON)
- **Maven** (Gestion des dépendances)
- **Docker & Docker Compose** (Conteneurisation)

## Configuration

### Variables d'environnement

Le projet utilise les variables d'environnement suivantes :

- `DB_NAME`: Nom de la base de données (défaut: spring_db)
- `DB_USER`: Utilisateur de la base de données (défaut: user)
- `DB_PASSWORD`: Mot de passe de la base de données (défaut: password)
- `DB_HOST`: Hôte de la base de données (défaut: db)
- `DB_PORT`: Port de la base de données (défaut: 5432)

### Profils Spring

- **default**: Configuration pour le développement local
- **docker**: Configuration pour l'environnement Docker

## Démarrage avec Docker

### Prérequis

- Docker
- Docker Compose

### Commandes

```bash
# Construction et démarrage des services
docker-compose up --build

# Démarrage en arrière-plan
docker-compose up -d

# Arrêt des services
docker-compose down

# Visualisation des logs
docker-compose logs -f app
```

### Services disponibles

- **Application**: http://localhost:8080
- **PostgreSQL**: localhost:5432
- **PgAdmin**: http://localhost:5050 (admin@example.com / admin)

## Développement local

### Prérequis

- Java 17
- Maven 3.9+
- PostgreSQL 16

### Configuration de la base de données

1. Créer une base de données PostgreSQL nommée `spring_db`
2. Créer un utilisateur `user` avec le mot de passe `password`
3. Accorder les privilèges nécessaires à l'utilisateur

### Commandes

```bash
# Compilation
mvn clean compile

# Tests
mvn test

# Construction du WAR
mvn clean package

# Démarrage avec un serveur d'application (Tomcat, etc.)
```

## API Endpoints

### Utilisateurs

- `GET /api/users` - Liste tous les utilisateurs
- `GET /api/users/{id}` - Récupère un utilisateur par ID
- `POST /api/users` - Crée un nouvel utilisateur
- `PUT /api/users/{id}` - Met à jour un utilisateur
- `DELETE /api/users/{id}` - Supprime un utilisateur

## Configuration avancée

### Pool de connexions

Le projet utilise Apache Commons DBCP2 avec les paramètres suivants :

- Taille initiale du pool: 5 connexions
- Taille maximale du pool: 20 connexions
- Taille maximale des connexions inactives: 10
- Taille minimale des connexions inactives: 5
- Timeout d'attente: 60 secondes

### Performance

- Batch processing activé pour Hibernate
- Optimisation des requêtes SQL
- Configuration du pool de connexions

## Dépannage

### Problèmes courants

1. **Erreur de connexion à la base de données**
   - Vérifier que PostgreSQL est démarré
   - Vérifier les credentials dans application.properties

2. **Problème de compilation**
   - Vérifier que Java 17 est installé
   - Exécuter `mvn clean compile`

3. **Problème Docker**
   - Vérifier que Docker est démarré
   - Exécuter `docker-compose logs` pour voir les erreurs

## Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commit vos changements
4. Push vers la branche
5. Ouvrir une Pull Request

## Licence

Ce projet est sous licence MIT.

