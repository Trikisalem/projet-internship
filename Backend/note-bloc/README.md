
<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![Tests](https://img.shields.io/badge/Tests-JUnit%205-green)

**API REST moderne pour gestion de notes avec authentification JWT, partage et collaboration**

</div>

---

## 📋 **À Propos**

**Note-Bloc Backend** est une API REST complète développée avec Spring Boot 3, offrant un système de gestion de notes avancé avec authentification sécurisée, partage collaboratif et documentation automatique.

### ✨ **Fonctionnalités Principales**
- 🔐 **Authentification JWT** avec refresh tokens
- 📝 **CRUD complet** des notes avec Markdown
- 🏷️ **Système de tags** intelligent
- 👥 **Partage entre utilisateurs** avec permissions
- 🔗 **Liens publics** avec expiration contrôlée
- 🔍 **Recherche avancée** et filtres
- 📄 **Pagination** optimisée
- 📚 **Documentation OpenAPI** automatique
- 🧪 **Tests** unitaires et d'intégration
- 🐳 **Docker** ready avec compose

---

## 🏗️ **Architecture**

src/main/java/com/projectnote/note_bloc/
├── 🛡️ config/
│ ├── SecurityConfig.java # Configuration Spring Security
│ ├── OpenAPIConfig.java # Configuration Swagger/OpenAPI
│ └── WebConfig.java # Configuration CORS
├── 🎮 controller/
│ ├── AuthController.java # Endpoints authentification
│ ├── NoteController.java # CRUD des notes
│ ├── ShareController.java # Partage et collaboration
│ └── PublicController.java # Accès public
├── 📦 dto/
│ ├── request/
│ │ ├── LoginRequest.java # Requête de connexion
│ │ ├── SignupRequest.java # Requête d'inscription
│ │ ├── NoteRequest.java # Requête de note
│ │ └── ShareRequest.java # Requête de partage
│ └── response/
│ ├── JwtResponse.java # Réponse JWT
│ ├── NoteResponse.java # Réponse note
│ └── ApiResponse.java # Réponse générique
├── 🗄️ entity/
│ ├── User.java # Entité utilisateur
│ ├── Note.java # Entité note
│ ├── NoteShare.java # Partage entre utilisateurs
│ └── PublicLink.java # Liens publics
├── 🔍 repository/
│ ├── UserRepository.java # Repository utilisateurs
│ ├── NoteRepository.java # Repository notes
│ ├── NoteShareRepository.java # Repository partages
│ └── PublicLinkRepository.java # Repository liens publics
├── ⚙️ service/
│ ├── AuthService.java # Service authentification
│ ├── NoteService.java # Service notes
│ ├── ShareService.java # Service partage
│ ├── JwtService.java # Service JWT
│ └── UserService.java # Service utilisateurs
├── 🛡️ security/
│ ├── JwtAuthenticationFilter.java # Filtre JWT
│ ├── JwtAuthenticationEntryPoint.java
│ └── UserDetailsServiceImpl.java # Implémentation UserDetails
├── ❌ exception/
│ ├── GlobalExceptionHandler.java # Gestionnaire d'erreurs global
│ ├── UserNotFoundException.java # Exceptions métier
│ └── NoteNotFoundException.java
text

---

## 🛠️ **Technologies Utilisées**

| Composant | Technologie | Version | Usage |
|-----------|-------------|---------|-------|
| **Framework** | Spring Boot | 3.5.6 | Framework principal |
| **Sécurité** | Spring Security | 6.x | Authentification & Autorisation |
| **Persistance** | Spring Data JPA | 3.x | ORM et accès données |
| **Base de données** | PostgreSQL | 15 | Base de données production |
| **Base de données** | H2 | 2.x | Base de données développement |
| **JWT** | JJWT | 0.12.3 | Tokens d'authentification |
| **Documentation** | SpringDoc OpenAPI | 2.6.0 | Documentation API automatique |
| **Tests** | JUnit 5 | 5.x | Tests unitaires |
| **Mocking** | Mockito | 5.x | Mocks pour tests |
| **Validation** | Bean Validation | 3.x | Validation des données |
| **Monitoring** | Spring Actuator | 3.x | Monitoring et métriques |
| **Build** | Maven | 3.9+ | Gestion des dépendances |
| **Containerisation** | Docker | Latest | Déploiement conteneurisé |

---

## 🚀 **Démarrage Rapide**

### **📋 Prérequis**
- ☕ **Java 17 ou supérieur**
- 🐘 **PostgreSQL 15+** (optionnel, H2 par défaut)
- 🐳 **Docker & Docker Compose** (optionnel)
- 🔧 **Maven 3.6+** ou utiliser le wrapper

### **⚡ Lancement Local**

Cloner le projet
git clone https://github.com/Trikisalem/projet-internship.git
cd projet-internship/Backend/note-blo

Option 1 : Maven Wrapper (recommandé)
./mvnw spring-boot:run

Option 2 : Maven installé
mvn spring-boot:run

Option 3 : JAR compilé
mvn clean package
java -jar target/note-bloc-

text

### **🐳 Lancement Docker**

Docker Compose (avec PostgreSQL)
docker-compose up --build

Docker Compose en arrière-plan
docker-compose up -d --build

Voir les logs
docker-compose logs -f app

Arrêter
docker-compose down

text

### **🔧 Configuration des Profils**

Profil développement (H2)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

Profil Docker (PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=docker

Profil production
mvn spring-boot:run -Dspring-boot.run.profiles=prod

Profil test
mvn test -Dspring.profiles.active=test

text

---

## 🌐 **URLs et Accès**

### **📊 Environnement Développement**
- **🚀 API Base :** http://localhost:8090
- **📚 Documentation Swagger :** http://localhost:8090/swagger-ui.html
- **📋 OpenAPI JSON :** http://localhost:8090/v3/api-docs
- **💾 Console H2 :** http://localhost:8090/h2-console
- **📊 Health Check :** http://localhost:8090/actuator/health
- **📈 Métriques :** http://localhost:8090/actuator/metrics

### **🐳 Environnement Docker**
- **🚀 API Base :** http://localhost:8080
- **🗄️ PostgreSQL :** localhost:5432
- **🔧 PgAdmin :** http://localhost:5050

---

## 📈 **API Endpoints**

### **🔐 Authentification**
Inscription utilisateur
POST /api/v1/auth/register
Content-Type: application/json
{
user@example.com",
"password": "password12
Connexion
POST /api/v1/auth/login
Content-Type: application/json
{
user@example.com",
"password": "password12
Refresh token
POST /api/v1/auth/refresh
Authorizati

text

### **📝 Gestion des Notes**
Lister les notes (avec pagination et filtres)
GET /api/v1/notes?page=0&size=10&query=recherche&tag=important

Créer une note
POST /api/v1/notes
Authorization: Bearer {access_token}
Content-Type: application/json
{
"title": "Ma note
Markdown",
"tags": ["important", "travail"
, "visibility": "PRIV
Récupérer une note
GET /api/v1/notes/{id}
Authorization

Modifier une note
PUT /api/v1/notes/{id}
Authorization

Supprimer une note
DELETE /api/v1/notes/{id}
Authorizat

text

### **👥 Partage et Collaboration**
Partager avec un utilisateur
POST /api/v1/notes/{id}/share/user
Authorization: Bearer {access_token}
{
destinataire@example.com",
"permission": "REA
Créer un lien public
POST /api/v1/notes/{id}/share/public
Authorization: Bearer {access_token}
{
"expiresAt": "2025-12-31T23:59:5
Accéder à une note publique
GET /api/v1/p/{urlToken}

text

---

## 🧪 **Tests**

### **📊 Structure des Tests**
src/test/java/com/projectnote/note_bloc/
├── 🧪 service/
│ ├── AuthServiceTest.java # Tests service auth
│ ├── NoteServiceTest.java # Tests service notes
│ └── ShareServiceTest.java # Tests service partage
├── 🎮 controller/
│ ├── AuthControllerTest.java # Tests REST auth
│ ├── NoteControllerTest.java # Tests REST notes
│ └── NoteControllerIntegrationTest.java # Tests intégration
├── 🔍 repository/
│ ├── NoteRepositoryTest.java # Tests repository
│ └── UserRepositoryTest.java
└── 🛡️ security/
text