📚 ARCHITECTURE DOCUMENTATION COMPLÈTE
📋 1. README Principal
Localisation : README.md (racine)

Contenu : Vue d'ensemble du projet complet avec badges, démarrage Docker, liens vers tous les services

Stack : Spring Boot 3 + React 18 + Flutter + Docker

URLs : Frontend (3000), Backend (8080), Documentation (swagger-ui), Monitoring (9090)

🚀 2. README Backend
Localisation : Backend/note-bloc/README.md

Contenu : API Spring Boot 3 avec JWT, JPA, PostgreSQL, tests JUnit

Fonctionnalités : CRUD notes, authentification, partage, recherche avancée

Endpoints : /api/v1/auth/*, /api/v1/notes/*, /api/v1/shares/*

Démarrage : mvn spring-boot:run

📱 3. README Frontend
Localisation : Frontend-main/README.md

Contenu : Interface React + TypeScript + TailwindCSS + Vite

Composants : AuthPage, NotesApp, NoteEditor, NotesList, Sidebar

Démarrage : npm install && npm run dev

Build : npm run build

📱 4. README Mobile
Localisation : mobile/README.md

Contenu : App mobile Flutter avec Material 3, Provider, SQLite

Fonctionnalités : Mode hors-ligne, synchronisation, dark/light theme

Packages : dio, provider, sqflite, jwt_decoder

Démarrage : flutter pub get && flutter run

📸 5. README Captures
Localisation : captures-ecran/README.md

Contenu : Documentation visuelle, screenshots interface, diagrammes architecture

Organisation : frontend/, backend/, mobile/, architecture/

Usage : Documentation technique, présentation, rapport de stage

🐳 6. README Docker
Localisation : docker/README.md

Contenu : Configuration containers, orchestration multi-services

Services : Backend (8080), Frontend (3000), PostgreSQL (5432), PgAdmin (5050), Prometheus (9090), Grafana (3001)

Démarrage : docker-compose up --build

🎯 TECHNOLOGIES CLÉS
Backend : Java 17, Spring Boot 3.5.6, Spring Security, JPA, PostgreSQL, JWT

Frontend : React 18, TypeScript 5.0, TailwindCSS, Vite 5.x

Mobile : Flutter 3.16+, Dart 3.2+, Material 3

DevOps : Docker, Docker Compose, Prometheus, Grafana

🚀 COMMANDES RAPIDES
bash
# Projet complet
docker-compose up --build

# Backend uniquement
cd Backend/note-bloc && mvn spring-boot:run

# Frontend uniquement  
cd Frontend-main && npm install && npm run dev

# Mobile uniquement
cd mobile && flutter pub get && flutter run
🌐 URLs ESSENTIELLES
App : http://localhost:3000

API : http://localhost:8080

Docs : http://localhost:8080/swagger-ui.html

PgAdmin : http://localhost:5050

Grafana : http://localhost:3001

Cette documentation structurée en 6 README distincts offre une vue complète et organisée de votre projet full-stack Note-Sync-Vault, démontrant votre expertise technique professionnelle pour votre stage d'ingénieur et votre future carrière.
