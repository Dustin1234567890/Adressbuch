# Adressbuch

Ein digitales Adressbuch zur Verwaltung von Kontakten.

Was es können soll:

1. Entitäten:
   - Contact (Name, Telefonnummer,Adresse, E-Mail)
   - Group (Gruppen, z. B. Familie, Arbeit)
   - User (Nutzer des Adressbuchs)
2. Features:
   - Kontakte hinzufügen, bearbeiten und löschen
   - Kontakte nach Gruppen filtern
   - Import/Export der Kontaktliste (z. B. als CSV)

# Requirements:
Java Version 17+
Maven Latest Version

# Initial Setup:
mvn clean install - Installiere alle dependencies für unser Projekt

# Fresh resetup:
mvn clean compile - vor dem install nochmal um neu zu kompilieren und alte Artifakte zu löschen

# Testing
mvn clean test - starte die Tests nach einem Cleanup

mvn surefire-report:report - Surefire report + Testing

# Execute Program/Project (via Exec)
mvn exec java
