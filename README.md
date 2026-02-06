# Adressbuch

**Note**: Unter /documentation/ sind restliche Dokumentationen zu finden, welche lesenswert sind.

Ein digitales Adressbuch zur Verwaltung von Kontakten und Gruppen.

1. Entitäten:
   - Contact (Name, Telefonnummer,Adresse, E-Mail)
   - Group (Name, Beschreibung)
2. Features:
   - Kontaktverwaltung
   - Gruppenverwaltung
   - Import/Export der Kontaktliste (als .CSV)

## Technologie-Stack

### Kern
- **Java 21** - language
- **Maven 3.9+** - Build & Dependency Management
- **SQLite** - Datenbank

### Frameworks & Libraries
- **PicoCLI** - CLI-Framework
- **jOOQ** - SQL Builder
- **SLF4J + Logback** - Logging
- **JUnit 5 (Jupiter)** - Testing
- **Mockito 5.x** - Mocking für Unittests

# Initial Setup und wichtige Befehle:
```bash
mvn clean install - Installiere alle dependencies für unser Projekt

mvn clean compile - vor dem install nochmal um neu zu kompilieren und alte Artifakte zu löschen

#jar bauen
mvn clean package

# Testing
mvn clean test - starte die Tests nach einem Cleanup

mvn surefire-report:report - Surefire report + Testing

# Execute Program/Project (via Exec)
mvn exec:java
```
