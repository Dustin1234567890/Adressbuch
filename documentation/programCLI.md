# 📒 Adressbuch – CLI & TUI Dokumentation

Willkommen beim **Adressbuch**!  
Dieses Programm ermöglicht die Verwaltung von **Kontakten** und **Gruppen** über eine **Command Line Interface (CLI)**  

## 🆘 Support & Hilfe

Wenn Sie nicht wissen was sie tun können, geben Sie hinter den Commands --help an. Dies wird Ihnen zeigen was der Command macht oder welche Sie ausführen können.

Beispiele:

mvn exec:java "-Dexec.args=--help"
mvn exec:java "-Dexec.args=group --help"
mvn exec:java "-Dexec.args=group add --help"

## Versionsinfo

mvn exec:java "-Dexec.args=--version"

## 👤 Kontaktverwaltung

Alle Kontaktbefehle starten mit "contact"

### ➕ Kontakt hinzufügen

✅ Pflichtfeld: --name
📌 Alle anderen Felder sind optional

mvn exec:java "-Dexec.args=contact add --id <id> --name <name> --phone <phone> --address <address> --email <email>"

Beispieleingabe:

mvn exec:java "-Dexec.args= contact add --name Max Mustermann"

mvn exec:java "-Dexec.args= contact add --name Max Mustermann --phone 0123456789 --address Musterstraße 1 --email max@example.de"

### 📋 Kontakte auflisten

Zeigt Ihnen alle Kontakte in einer Tabelle an.

mvn exec:java "-Dexec.args= contact list"

### 🔍 Kontakt suchen

Sie können eine Kontakt suchen in dem Sie zuerst das Suchfeld angeben und dann den Suchbegriff.
Ihnen wird eine Tabelle angezeigt, die mit dem Suchbegriff übereinstimmen.

mvn exec:java "-Dexec.args=contact search <feld> <suchbegriff>"

Mögliche Suchfelder:
 - name
 - phone
 - email
 - address
 - id

Beispiel:

mvn exec:java "-Dexec.args= contact search id 17d4d900-f69a-41df-bcf1-d98a52bd1c7d"
mvn exec:java "-Dexec.args= contact search name Max"
mvn exec:java "-Dexec.args= contact search phone +49 12345678"

### ✏️ Kontakt aktualisieren

✅ Pflichtfeld: --id
🔄 Nur die angegebenen Felder werden geändert, alle anderen bleiben unverändert.

mvn exec:java "-Dexec.args=contact update --id <id> --name <name> --phone <phone> --address <address> --email <email>"

Beispiel:

mvn exec:java "-Dexec.args=contact update --id 4c60309c-0979-4177-a21d-0d71f46135bd --phone 123456 --email testmail@gmail.com"

### 🗑️ Kontakt löschen

✅ Pflichtfeld: --id
⚠️ Der Kontakt wird dauerhaft entfernt!

mvn exec:java "-Dexec.args=contact delete --id <id>"

Beispiel:

mvn exec:java "-Dexec.args=contact delete --id 4c60309c-0979-4177-a21d-0d71f46135bd"

## 👥 Gruppenverwaltung

Alle Gruppenbefehle starten mit "group"

### ➕ Gruppe hinzufügen

✅ Pflichtfeld: --name
📌 Alle anderen Felder sind optional

mvn exec:java "-Dexec.args=contact group --name <name> --description <description>"

Beispiel:

mvn exec:java "-Dexec.args=contact group --name Lerngruppe --description Zusammen lernen"

### 📋 Gruppen anzeigen

Zeigt Ihnen alle Gruppen in einer Tabelle an.

mvn exec:java "-Dexec.args= group list"

### 🔍 Gruppe suchen

Sie können eine Gruppe suchen in dem Sie zuerst das Suchfeld angeben und dann den Suchbegriff.
Ihnen wird eine Tabelle angezeigt, die mit dem Suchbegriff übereinstimmen.

mvn exec:java "-Dexec.args=group search <feld> <suchbegriff>"

Mögliche Suchfelder:
 - name
 - id

Beispiel:

mvn exec:java "-Dexec.args= group search id 17d4d900-f69a-41df-bcf1-d98a52bd1c7d"
mvn exec:java "-Dexec.args= group search name Lerngruppe"

### ✏️ Gruppe aktualisieren

✅ Pflichtfeld: --id
🔄 Nur die angegebenen Felder werden geändert, alle anderen bleiben unverändert.

mvn exec:java "-Dexec.args=group update --id <id> --name <name> --description <description>"

Beispiel:

mvn exec:java "-Dexec.args=group update --id 4c60309c-0979-4177-a21d-0d71f46135bd --description zusammen für Prog3 lernen"

### 🗑️ Gruppe löschen

✅ Pflichtfeld: --id
⚠️ Die Gruppe wird dauerhaft entfernt!

mvn exec:java "-Dexec.args=group delete --id <id>"

Beispiel:

mvn exec:java "-Dexec.args=group delete --id 4c60309c-0979-4177-a21d-0d71f46135bd"

## 🔗 Kontakte & Gruppen verknüpfen

### ➕ Kontakt zu Gruppe hinzufügen

✅ Pflichtfeld: --idGroup, ----idContact

mvn exec:java "-Dexec.args=group add-contact-to-group --idGroup <idGroup> --idContact <idContact>"

Beispiel:

mvn exec:java "-Dexec.args=group add-contact-to-group --idGroup 4c60309c-0979-4177-a21d-0d71f46135bd --idContact 35325324-0979-4177-a21d-0d71f46135bd"

### ❓ Prüfen, ob Kontakt in Gruppe ist

✅ Pflichtfeld: --idGroup, ----idContact
Gibt Ihnen die Rückmeldung, der gesuchte Kontakt der Gruppe zugeteilt wurde.

mvn exec:java "-Dexec.args=group is-contact-in-group --idGroup <idGroup> --idContact <idContact>"

Beispiel:

mvn exec:java "-Dexec.args=group is-contact-in-group --idGroup 4c60309c-0979-4177-a21d-0d71f46135bd --idContact 35325324-0979-4177-a21d-0d71f46135bd"

### 📋 Kontakte in einer Gruppe anzeigen

✅ Pflichtfeld: --idGroup
Zeigt Ihnen alle Kontakte einer Gruppe in einer Tabelle an.

mvn exec:java "-Dexec.args=group show-contacts-in-group --idGroup <idGroup>"

Beispiel:

mvn exec:java "-Dexec.args=group show-contacts-in-group --idGroup 4c60309c-0979-4177-a21d-0d71f46135bd"

### ➖ Kontakt aus Gruppe entfernen

✅ Pflichtfeld: --idGroup, ----idContact
⚠️ Der Kontakt wird aus der Gruppe dauerhaft entfernt, bis er wieder neu zugeordnet wird!

mvn exec:java "-Dexec.args=group remove-contact-from-group --idGroup <idGroup> --idContact <idContact>"

Beispiel:

mvn exec:java "-Dexec.args=group remove-contact-from-group --idGroup 4c60309c-0979-4177-a21d-0d71f46135bd --idContact 35325324-0979-4177-a21d-0d71f46135bd"

## ❤️ Fazit

Das Adressbuch bietet:

- Mächtige CLI für Automatisierung 🤖
- Saubere Struktur für Kontakte & Gruppen 📂

Viel Spaß beim Verwalten deiner Kontakte! 😎📒