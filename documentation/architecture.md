# Architektur - Adressbuch

## Übersicht

Das Adressbuch-Projekt implementiert eine **Drei-Schichten-Architektur** mit strikter Trennung von UI, Business Logic und Persistenz. Das Projekt folgt **SOLID-Prinzipien**.

## Inhalt

- **Projektstruktur**
- **Drei-Schichten-Architektur**
- **SOLID**
- **Custom Exceptions**
- **Dependencies**
- **Clean Code**
- **Testing Approach**
- **Erweiterbarkeit**

## Projektstruktur

```
src/main/java/de/adressbuch/
├── cli/                      # Präsentationsschicht
│   ├── AdressbuchCLI.java    # Kommandozeilen-Interface (via PicoCLI)
│   └── InteractiveTUI.java   # Interaktive Text-UI
├── service/                  # Geschäftslogik-Schicht
│   ├── ContactService.java
│   ├── GroupService.java
│   ├── ContactGroupService.java
│   └── ContactFilterService.java
├── repository/               # Persistenz-Schicht
│   ├── SQLiteContactRepo.java
│   ├── SQLiteGroupRepo.java
│   ├── SQLiteContactGroupRepo.java
│   └── interfaces/
│       ├── ContactRepo.java
│       ├── GroupRepo.java
│       └── ContactGroupRepo.java
├── models/                   # Models
│   ├── Contact.java
│   ├── Group.java
│   └── User.java
├── exception/                # Custom Exceptions
│   ├── EntityNotFoundException.java
│   ├── ContactNotFoundException.java
│   ├── GroupNotFoundException.java
│   ├── ValidationException.java
│   └── RepositoryException.java
├── injection/                # Dependency Injection & Konfiguration
│   ├── ServiceFactory.java   
│   ├── DatabaseConfig.java   
│   └── DisplayConstants.java 
└── util/                     # Utilities
    └── Utils.java

src/test/java/de/adressbuch/
├── service/                  # Service Layer Tests
└── repository/               # Repository Tests
```

## Drei-Schichten-Architektur

### 1. UI
**Klassen**: `AdressbuchCLI`, `InteractiveTUI`

**Exception-Handling**: Caught Exceptions von Services werden in Meldungen konvertiert

### 2. Business Logic (Service)
**Klassen**: `ContactService`, `GroupService`, `ContactGroupService`

- **Responsabilities**:
  - Business Logic (Validierung, Filterung, Beziehungsverwaltung)
  - Service-Interface für CLI/TUI
  - Exception-Throwing bei Validierungsfehlern
  - Keine direkten Datenbankzugriffe (nur über Repositories)

- **Best Practices**:
  - `Optional<T>` für sichere Null-Behandlung
  - Custom Exceptions werfen, nicht `RuntimeException`
  - Records für leicht zu generierenden boiler plate code unserer unveränderlichen Klassen

### 3. Persistierungs-Schicht (Repository)
**Klassen**: `SQLiteContactRepo`, `SQLiteGroupRepo`, `SQLiteContactGroupRepo`

- **Responsabilities**:
  - SQL-Queries (via JOOQ)
  - Datenbankverbindungen managen
  - Schema-Init
  - Nur SQL

- **Impl**:
  - Type-safe SQL mit jOOQ statt String-Queries
  - Interfaces für Testbarkeit und Austauschbarkeit
  - `RepositoryException` für DB-Fehler
  - Transaktionen (wo nötig)

### 4. Domänen-Modelle (Models)
**Klassen**: `Contact`, `Group`, `User` als records realisiert

- **Properties**:
  - Immutable
  - `Optional<T>` für optionale Felder
  - Pure Data Objects

## SOLID-Prinzipien

### Single Responsibility Principle (SRP)
- Jede Klasse hat eine Verantwortung
- `ContactService` = Kontakt Logik
- `ContactRepo` = Persistenz
- `AdressbuchCLI` = UI

### Open/Closed Principle (OCP)
- Repository-Interfaces ermöglichen neue Implementierungen ohne vorhandene zu ändern
- Exception-Hierarchie erweiterbar für neue Exception-Typen

### Liskov Substitution Principle (LSP)
- `SQLiteContactRepo` kann überall verwendet werden, wo `ContactRepo` erwartet wird
- Custom Exceptions erben von `Exception` und können dadurch überall gecatcht werden

### Interface Segregation Principle (ISP)
- `ContactRepo`, `GroupRepo`, `ContactGroupRepo`

### Dependency Inversion Principle (DIP)
- High-level modules (hier also Services) hängen von Abstraktionen ab (Repository-Interfaces)
- `ServiceFactory` managed Abhängigkeits-Auflösung
- Constructor Injection für Testbarkeit

## Exception-Hierarchie

```
java.lang.Exception
├── EntityNotFoundException (checked)
│   ├── ContactNotFoundException
│   └── GroupNotFoundException
├── ValidationException (checked)
└── RepositoryException (checked)
```

**Verwendung**:
- Custom Exceptions sind mostly **checked exceptions**, ergo müssen sie explizit gehandhabt werden
- dadurch typsichere Exception-Behandlung
- (bessere) Fehlerkontext-Informationen

## Dependencies

```
Benutzer-Input (CLI)
    ↓
AdressbuchCLI / InteractiveTUI
    ↓
Service Layer 
    ↓
Repository Layer
    ↓
SQLite Database
```

### Clean Code
- **Naming Conventions**: `findContactById()` statt `getC()`
- **Methoden-Länge**: versucht kleinzuhalten
- **Keine Magic Numbers**: const verwenden wo möglich
- **DRY**: Duplicated Code vermeiden und aktiv refactoren, um diesen los zu werden

## Testing-Approach

### Workflow
1. **Implementiere Code Snippet** unter Knowledge über Use-Cases
2. **Teste Codesnippet** lokales Testen der neuen Use-Cases
3. **Test schreiben**  festige die Use-Cases in Integrationtests
4. **Unit Tests schreiben**  fertige diese an um damit ein Sicherheitsnetz aufzubauen

## Erweiterbarkeit

Die Architektur ermöglicht einfache Erweiterungen:
- **Neue Persistierung**: Neue `XYRepo` Implementierung + Interface
- **Neue Services**: Neue Service-Klasse + neue Repository-Interface
- **Neue CLI-Commands**: Neue PicoCLI `@Command` Klasse
- **Neue Exception-Typen**: Erben von `EntityNotFoundException` oder `ValidationException` und allgemein erweiterbar

Alles ohne existierende Code (stark) zu ändern (Open/Closed Principle).