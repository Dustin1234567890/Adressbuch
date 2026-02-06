# 🖥️ Adressbuch – Text User Interface (TUI)

Die **Text User Interface (TUI)** bietet eine interaktive, menügesteuerte Bedienung des Adressbuchs direkt im Terminal.  
Alle Aktionen erfolgen über nummerierte Menüs – **keine Befehle merken nötig** 😄⌨️

## 🚀 Start der TUI

Folgenden Command ausführen, um das Programm im Text User Interface zu  starten:

mvn exec:java

Folgende Begrüßung sollte dann erscheinen:

Willkommen im Adressbuch!
Bitte wählen Sie eine Option aus dem Hauptmenü, um fortzufahren.

## 🏠 Hauptmenü

👉 Eingabe erfolgt über die jeweilige Zahl  
❌ Ungültige Eingaben werden abgefangen

=== Hauptmenü ===
1. Kontaktverwaltung
2. Gruppenverwaltung
3. Exit


### 📋 Kontaktmenü

=== Kontaktverwaltung ===
1. Kontakte anzeigen
2. Kontakt hinzufügen
3. Kontakt aktualisieren
4. Kontakt suchen
5. Kontakt löschen
6. Zurück

## 📋 Kontakte anzeigen

**Menüpfad:**  
`Kontaktverwaltung → Kontakte anzeigen`

Zeigt Ihnen alle Kontakte an in einer Tabelle.

## ➕ Kontakt hinzufügen

**Menüpfad:**  
`Kontaktverwaltung → Kontakt hinzufügen`

⚠️ Name ist Pflichtfeld  
❌ Leerer Name → Vorgang wird abgebrochen

### 🧾 Eingabedialog:
Name :
Telefon :
Adresse :
E-Mail :

## ✏️ Kontakt aktualisieren

**Menüpfad:**  
`Kontaktverwaltung → Kontakt aktualisieren`

### 🔄 Ablauf:
1. Eingabe der Kontakt-ID
2. Aktuelle Werte werden angezeigt
3. Neue Werte können eingegeben werden
4. Leere Eingabe → alter Wert bleibt erhalten

## 🔍 Kontakt suchen

**Menüpfad:**  
`Kontaktverwaltung → Kontakt suchen`

### 🔎 Suchfeld angeben:
1. ID
2. Name
3. Telefon
4. E-Mail
5. Adresse

Danach Suchbegriff eingeben.
📤 Treffer werden tabellarisch angezeigt  
❌ Keine Treffer: Kein Kontakt mit dem Suchbegriff gefunden.

## 🗑️ Kontakt löschen

**Menüpfad:**  
`Kontaktverwaltung → Kontakt löschen`
Angeben der Kontakt ID um einen Kontakt zu löschen.

### ⚠️ Sicherheitsabfrage:
✅ Nur bei Eingabe von **"Ja"** wird gelöscht  
❌ Jede andere Eingabe → Abbruch

# 👥 Gruppenverwaltung

### 📋 Gruppenmenü
=== Gruppenverwaltung ===
1. Gruppen anzeigen
2. Gruppe hinzufügen
3. Gruppe bearbeiten
4. Gruppe suchen
5. Gruppe löschen
6. Kontakt einer Gruppe hinzufügen
7. Kontakte einer Gruppe anzeigen
8. Ist Kontakt in Gruppe?
9. Kontakt einer Gruppe entfernen
10. Zurück

## 📋 Gruppen anzeigen

**Menüpfad:**  
`Gruppenverwaltung → Gruppen anzeigen`
Zeigt Ihnen alle Gruppen an in einer Tabelle.

## ➕ Gruppe hinzufügen

**Menüpfad:**  
`Gruppenverwaltung → Gruppe hinzufügen`
⚠️ Gruppenname ist Pflichtfeld
❌ Leerer Name → Vorgang wird abgebrochen

### 🧾 Eingabedialog:
Name :
Beschreibung : 

## ✏️ Gruppe bearbeiten

**Menüpfad:**  
`Gruppenverwaltung → Gruppe bearbeiten`

### 🔄 Ablauf:
1. Eingabe der Gruppen-ID
2. Aktuelle Werte werden angezeigt
3. Neue Werte können eingegeben werden
4. Leere Eingabe → alter Wert bleibt erhalten

## 🔍 Gruppe suchen

**Menüpfad:**  
`Gruppenverwaltung → Gruppe suchen`

### 🔎 Suchfeld angeben:
1. ID
2. Name

Danach Suchbegriff eingeben.
📤 Treffer werden tabellarisch angezeigt  
❌ Keine Treffer: Kein Kontakt mit dem Suchbegriff gefunden.

## 🗑️ Gruppe löschen

**Menüpfad:**  
`Gruppenverwaltung → Gruppe löschen`
Angeben der Gruppen-ID um einen Kontakt zu löschen.

### ⚠️ Sicherheitsabfrage:
✅ Nur bei Eingabe von **"Ja"** wird gelöscht  
❌ Jede andere Eingabe → Abbruch

## ➕ Kontakt zu Gruppe hinzufügen

**Menüpfad:**  
`Gruppenverwaltung → Kontakt einer Gruppe hinzufügen`
Angeben von Gruppen-ID und Kontakt-ID.

🧠 Prüfung:
- Existiert die Gruppe?
- Existiert der Kontakt?
- Ist der Kontakt bereits in der Gruppe?

✅ Wenn diese Kriterien stimmen, wird der Kontakt zugeordnet.

## 📋 Kontakte einer Gruppe anzeigen

**Menüpfad:**  
`Gruppenverwaltung → Ist Kontakt in Gruppe?`
Angabe der Gruppen-ID und Kontak-ID.
Gibt dir eine kurze NAchricht zurück, ob der Kontakt in der Gruppe ist.

## ➖ Kontakt aus Gruppe entfernen

**Menüpfad:**  
`Gruppenverwaltung → Kontakt einer Gruppe entfernen`
Angabe der Gruppen-ID und Kontak-ID.

### ⚠️ Sicherheitsabfrage:
✅ Nur bei Eingabe von **"Ja"** wird gelöscht  
❌ Jede andere Eingabe → Abbruch

## ❤️ Fazit

Die TUI bietet:
- Intuitive Menüführung 🧭
- Sichere Bestätigungen bei Löschvorgängen ⚠️
- Vollständige Kontakt- & Gruppenverwaltung 🧑‍🤝‍🧑

Ideal für interaktive Nutzung direkt im Terminal 😎🖥️