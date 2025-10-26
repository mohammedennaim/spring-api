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
│   │               ├── controller/      # Contrôleurs REST + Gestion d'erreurs
│   │               ├── model/           # Entités JPA + Réponses API
│   │               ├── repository/      # Interface + Implémentation Repository
│   │               └── service/         # Interface + Implémentation Service
│   ├── resources/
│   │   ├── META-INF/
│   │   │   └── persistence.xml         # Configuration JPA
│   │   ├── application.properties      # Configuration principale
│   │   └── application-docker.properties # Configuration Docker
│   └── webapp/
│       └── WEB-INF/
│           └── web.xml                 # Configuration web.xml
└── test/
    ├── java/                           # Tests unitaires et d'intégration
    └── resources/
        └── application-test.properties # Configuration pour les tests
```

## Technologies utilisées

- **Java 17**
- **Spring Framework 6.1.5** (MVC, ORM, Context)
- **Hibernate 6.4.0** (JPA Provider)
- **PostgreSQL 16** (Base de données)
- **HikariCP** (Pool de connexions)
- **Jackson** (Sérialisation JSON)
- **Maven** (Gestion des dépendances)
- **Docker & Docker Compose** (Conteneurisation)

## Architecture du Projet

### 🏗️ **Architecture en Couches**

```
┌─────────────────────────────────────────┐
│              Controller                 │  ← REST API + Gestion d'erreurs
├─────────────────────────────────────────┤
│              Service                    │  ← Logique métier
├─────────────────────────────────────────┤
│         Repository Interface            │  ← Contrat de données
├─────────────────────────────────────────┤
│        Repository Implementation        │  ← Accès aux données (JPA)
├─────────────────────────────────────────┤
│              Database                   │  ← PostgreSQL
└─────────────────────────────────────────┘
```

### 📁 **Structure Détaillée des Couches**

#### **Controller Layer**
- `UserController.java` - Endpoints REST avec réponses JSON
- `GlobalExceptionHandler.java` - Gestion centralisée des erreurs

#### **Service Layer**
- `UserService.java` - Interface du service métier
- `UserServiceImpl.java` - Implémentation avec validation et logique métier

#### **Repository Layer**
- `IUserRepository.java` - Interface Repository avec méthodes CRUD
- `UserRepositoryImpl.java` - Implémentation JPA avec EntityManager

#### **Model Layer**
- `User.java` - Entité JPA
- `ApiResponse.java` - Classe de réponse JSON standardisée

### 🎯 **Avantages de l'Architecture**

1. **Séparation des préoccupations** : Chaque couche a une responsabilité claire
2. **Testabilité** : Interfaces mockables pour les tests unitaires
3. **Maintenabilité** : Modifications isolées par couche
4. **Évolutivité** : Ajout de nouvelles fonctionnalités facilité
5. **Réutilisabilité** : Interfaces utilisables par plusieurs services

## Configuration

### Variables d'environnement

Le projet utilise des variables d'environnement pour la configuration. **IMPORTANT** : Copiez `.env.example` vers `.env` et modifiez les valeurs sensibles :

- `DATABASE_URL`: URL de connexion à la base de données
- `DATABASE_USERNAME`: Nom d'utilisateur de la base de données
- `DATABASE_PASSWORD`: Mot de passe de la base de données (⚠️ **CHANGER EN PRODUCTION**)
- `SERVER_PORT`: Port du serveur (défaut: 8081)
- `JPA_DDL_AUTO`: Mode de création des tables (défaut: update)
- `JPA_SHOW_SQL`: Afficher les requêtes SQL (défaut: false)

### Profils Spring

- **default**: Configuration pour le développement local
- **docker**: Configuration pour l'environnement Docker
- **test**: Configuration pour les tests (H2 en mémoire)

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

- **Application**: http://localhost:8081
- **PostgreSQL**: localhost:5432
- **PgAdmin**: http://localhost:5050 (admin@example.com / admin)

## Développement local

### Prérequis

- Java 17
- Maven 3.9+
- PostgreSQL 16

### Configuration de la base de données

1. **Copiez le fichier `.env.example` vers `.env` :**
   ```bash
   cp .env.example .env
   ```

2. **⚠️ IMPORTANT - Modifiez les valeurs sensibles dans `.env` :**
   - Changez `DATABASE_PASSWORD` par un mot de passe fort
   - Changez `PGADMIN_DEFAULT_PASSWORD` par un mot de passe sécurisé
   - Adaptez `DATABASE_URL` selon votre configuration

3. Créer une base de données PostgreSQL
4. Accorder les privilèges nécessaires à l'utilisateur

**🔒 Consultez `SECURITY.md` pour les bonnes pratiques de sécurité !**

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

### 📋 **Format des Réponses JSON**

Toutes les opérations CRUD retournent des réponses JSON standardisées :

```json
{
  "success": boolean,
  "message": string,
  "data": object | array | null,
  "error": string | null
}
```

#### **Exemple de Réponse de Succès**
```json
{
  "success": true,
  "message": "Utilisateur créé avec succès",
  "data": {
    "id": 1,
    "fullName": "John Doe",
    "email": "john@example.com"
  },
  "error": null
}
```

#### **Exemple de Réponse d'Erreur**
```json
{
  "success": false,
  "message": "Utilisateur non trouvé",
  "data": null,
  "error": "Aucun utilisateur trouvé avec l'ID: 999"
}
```

### 🛡️ **Gestion d'Erreurs**

- **404** : Utilisateur non trouvé
- **400** : Données invalides (nom ou email manquant)
- **500** : Erreur interne du serveur
- **Validation** : Vérification de l'unicité de l'email

### 🧪 **Exemples d'Utilisation**

#### **Créer un utilisateur**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"fullName": "John Doe", "email": "john@example.com"}'
```

#### **Récupérer tous les utilisateurs**
```bash
curl http://localhost:8081/api/users
```

#### **Récupérer un utilisateur par ID**
```bash
curl http://localhost:8081/api/users/1
```

#### **Mettre à jour un utilisateur**
```bash
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"fullName": "John Updated", "email": "john.updated@example.com"}'
```

#### **Supprimer un utilisateur**
```bash
curl -X DELETE http://localhost:8081/api/users/1
```

## Configuration avancée

### Pool de connexions

Le projet utilise HikariCP avec les paramètres configurables via les variables d'environnement :

- `DB_POOL_SIZE`: Taille maximale du pool (défaut: 10)
- `DB_MIN_IDLE`: Nombre minimum de connexions inactives (défaut: 5)
- `DB_CONNECTION_TIMEOUT`: Timeout de connexion (défaut: 30000ms)
- `DB_IDLE_TIMEOUT`: Timeout d'inactivité (défaut: 600000ms)
- `DB_MAX_LIFETIME`: Durée de vie maximale (défaut: 1800000ms)

### Performance

- Configuration optimisée d'Hibernate
- Pool de connexions configurable
- Support des profils Spring pour différents environnements

## Dépannage

### Problèmes courants

1. **Erreur de connexion à la base de données**
   - Vérifier que PostgreSQL est démarré
   - Vérifier les variables d'environnement dans le fichier `.env`
   - Vérifier que le fichier `.env` existe (copier depuis `.env.example`)

2. **Problème de compilation**
   - Vérifier que Java 17 est installé
   - Exécuter `mvn clean compile`

3. **Problème Docker**
   - Vérifier que Docker est démarré
   - Vérifier que le fichier `.env` existe
   - Exécuter `docker-compose logs` pour voir les erreurs

## Tests et Documentation

### 🧪 **Tests Disponibles**

- **Tests unitaires** : `src/test/java/` - Tests des composants isolés
- **Tests d'intégration** : Tests avec base de données H2
- **Configuration de test** : `application-test.properties`

### 📚 **Documentation Technique**

- **`REPOSITORY_ARCHITECTURE.md`** : Guide détaillé de l'architecture Repository
- **API Documentation** : Format des réponses JSON et gestion d'erreurs
- **Configuration** : Variables d'environnement et profils Spring

### 🔧 **Commandes de Test**

```bash
# Exécuter tous les tests
mvn test

# Tests avec rapport de couverture
mvn test jacoco:report

# Tests d'intégration
mvn verify
```

## Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commit vos changements
4. Push vers la branche
5. Ouvrir une Pull Request

## Licence

Ce projet est sous licence MIT.


