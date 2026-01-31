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

TODO weiter Infos zum Projekt

Befehle:

mvn clean install
mvn clean test
mvn clean package
mvn clean compile
# mvn exec:java "-Dexec.mainClass=de.adressbuch.Main"
mvn surefire-report:report
mvn clean verify

mvn exec:java
