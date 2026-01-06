# Bookies

Bookies ist eine mobile Buchplattform, die Nutzerinnen und Nutzer bei der Entdeckung, Verwaltung und Empfehlung von Büchern unterstützt.  
Die Anwendung kombiniert eine moderne Android-App mit einem skalierbaren Backend und ermöglicht personalisierte Leselisten sowie soziale Interaktionen.

---

## Features

- Benutzerregistrierung und Authentifizierung (JWT)
- Buchsuche und -verwaltung
- Persönliche Leselisten (gelesen, geplant, favorisiert)
- Bewertungen und Kommentare
- Soziale Funktionen (Folgen anderer Nutzer)
- Reaktive Aktualisierung der Inhalte

---

## Architektur

Bookies folgt einer klaren Client-Server-Architektur:

- Mobile Anwendung (Frontend) nach dem MVVM-Pattern
- REST-basiertes Backend mit Spring Boot
- Relationale Datenbank (PostgreSQL)
- Containerisiertes Deployment mit Docker

Die Kommunikation zwischen Frontend und Backend erfolgt über definierte REST-Endpunkte.  
DTOs und Mapper sorgen für eine saubere Trennung zwischen interner Geschäftslogik und externen Datenstrukturen.

---

## Tech Stack

**Frontend**
- Kotlin
- MVVM
- Retrofit
- LiveData / StateFlow

**Backend**
- Java
- Spring Boot
- REST API
- JWT-basierte Authentifizierung

**Infrastructure**
- PostgreSQL
- Docker & Docker Compose
- Maven
- Git

---

## Setup & Run

### Voraussetzungen
- Docker
- Docker Compose

### Start der Anwendung
```bash
docker-compose up --build
