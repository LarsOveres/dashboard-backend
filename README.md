# Backend eindopdracht

Dit project is de backend voor een MP3-bestandsbeheerapplicatie, gebouwd met Spring Boot. Het biedt endpoints voor authenticatie, bestandsbeheer, statistieken en gebruikersbeheer. De backend maakt gebruik van JWT voor beveiliging en communiceert met een React frontend.

Link naar de repository: https://github.com/LarsOveres/dashboard-backend

# Inhoudsopgave

- Overzicht
- Functies
- Technologieën
- Installatie
- Configuratie
- Gebruik
- Database
- API Endpoints
- Testen
- Toekomstige verbeteringen
- Bijdragen
- Licentie

# Overzicht

De backend biedt een veilige en schaalbare oplossing voor het beheren en afspelen van MP3-bestanden, inclusief gebruikersauthenticatie met JWT en rol-gebaseerde toegang. Gebruikers kunnen bestanden uploaden, afspelen, en statistieken inzien. Administrators hebben uitgebreide rechten om de applicatie te beheren.

# Functies

- JWT Authenticatie: Beveiligde endpoints met token-gebaseerde toegang.
- Bestandsbeheer: MP3-bestanden uploaden, downloaden en afspelen.
- Statistieken: Houdt bij hoe vaak een MP3-bestand is afgespeeld.
- Rol-gebaseerde toegang: Verschillende rechten voor gebruikers en beheerders.
- Commentaar Systeem: Beheerders kunnen opmerkingen plaatsen bij bestanden.

# Technologieën

- Java: Voor de applicatielogica.
- Spring Boot: Framework voor snelle ontwikkeling.
- JWT: Voor authenticatie en beveiliging.
- Hibernate: ORM voor interactie met de database.
- H2/MySQL: In-memory of externe database voor gegevensopslag.
- Mp3SPI: Voor het uitlezen van MP3-tags, zoals bestandsgrootte en duur.

# Installatie

### Vereisten

- JDK 11 of hoger
- Maven
- Een database (H2 of MySQL aanbevolen)

### Stap 1: Clone de repository

`git clone https://github.com/jouwgebruikersnaam/mp3-file-management-backend.git`
`cd mp3-file-management-backend`

### Stap 2: Bouw het project

Gebruik Maven om het project te bouwen: `mvn clean install`

### Stap 3: Start de applicatie

Start de Spring Boot applicatie met: `mvn spring-boot:run`

De applicatie draait op http://localhost:8080.

# Configuratie

## Database configuratie

De applicatie maakt gebruik van PostgreSQL als database. Hieronder vind je de configuratie voor PostgreSQL.

### PostgreSQL Configuratie:

In het bestand src/main/resources/application.properties moet je de
database-instellingen als volgt configureren:

```markdown
spring.datasource.url=jdbc:postgresql://localhost:5432/dashboard
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Zorg ervoor dat je een PostgreSQL database genaamd dashboard hebt aangemaakt en dat de gebruikersnaam en het wachtwoord overeenkomen met jouw PostgreSQL-
configuratie.

### PostgreSQL database aanmaken:

Open PostgreSQL en maak de database aan:

```markdown
CREATE DATABASE dashboard;
```

# Gebruik

## Gebruikersregistratie en -inlog

Nieuwe gebruikers kunnen zich registreren via de `/user/register` endpoint en inloggen via `/login`. Na inloggen ontvangen ze een JWT-token dat moet worden gebruikt voor beveiligde API-verzoeken.

Rollen en Toegangscontrole
De applicatie maakt gebruik van twee rollen:

- **ADMIN**: Heeft volledige toegang tot alle endpoints.
- **USER**: Heeft beperkte toegang en kan alleen eigen bestanden beheren.

Wijs met het PUT endpoint http://localhost:8080/user/1/role een rol toe aan een gebruiker en zet hierna dit endpoint op `.hasAuthority("ROLE_ADMIN")` in de SecurityConfig

# Database

## Database Schema

Het schema bevat tabellen voor:

- **Users:** Voor gebruikersgegevens en rollen.
- **Mp3Files:** Voor het opslaan van geüploade MP3-bestanden.
- **Comments:** Voor opmerkingen bij MP3-bestanden.
- **Roles:** Voor gebruikersrollen (USER, ADMIN).

# API Endpoints

## Authenticatie Endpoints

`POST /user/register:` Registreer een nieuwe gebruiker.

`POST /login:` Log in en ontvang een JWT-token.

`GET /users/{email_gebruiker}` Gebruiker opvragen via email.

## Bestand Endpoints

`POST /files/upload` Upload een MP3-bestand.

`GET /files/{id}` Mp3 bestand opvragen.

`PUT /files/playcount/{id}` Verhoog de playcount van een MP3-bestand.

`DELETE /user/{id}` Verwijder een gebruiker.

`GET /files/user-files/download-stats` Aantal downloads en bestanden opvragen.

`GET /files/download/{id}` Download het bestand.

`GET /files/play/{id}` Mp3 bestand afspelen.

`GET /files/list` Lijst van alle bestanden ophalen.

## Comment Endpoints

`POST /files/{id}/comment` Plaats een opmerking bij een MP3-bestand (admin-only).

`GET /files/{id}/comments` Haal alle opmerkingen bij een MP3-bestand op.

## Rol Endpoints

`POST /roles/add` Rol toevoegen aan de database.

`PUT /user/{id}/role` Andere rol toewijzen aan gebruiker.

`GET /roles/user/role` Rol van gebruiker opvragen.