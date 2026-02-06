# Workflow & Retrospektive - 15-Tage Sprint-Iteration

## Übersicht

Das Adressbuch Projekt wurde über **15 Tage** in **5 Sprints à 3 Tage** entwickelt. Diese Beschreibung soll zeigen:
unseren agilen Prozess, die Herausforderungen mit Ultra-Short-Sprints und unsere Learnings.

## Sprint-Struktur: 3 Days of Doom :^

### Tag 1: Setup & Mini-Planning
- **Inhalt**:
  - Learning und Blocker aufarbeiten aus Tag 3
  - Sprintziele erkennen
  - Technische/Architekturelle Absprache
  - Assignments der Devs

**Ziel**: Klare, REALISTISCHE Goals

### Tag 2: Heavy Coding & Pairing
- **Inhalt**:
  - **Pair Programming Sessions** - durchgehend über Stunden hinweg
  - (manuelles) Testing during Implementation und vice versa
  - Code Reviews im Pair (sofort, nicht asynchron)
  - Diskussionen über Design in realtime
  - bei Blocker sofortiges Problem-lösen

**Ziel**: Coding und Testing als Teamwork, nichts wird gesolocarryt

### Tag 3: Reflektion, Finishing, Damage Control
- **Inhalt**:
  - **Retro**: Was funzt? Was nicht?
  - **Code Refactoring**: Focus on Clean up, Pfadfinder Regel!
  - **Board Verwaltung**: zu Done moven falls möglich und Blocker identifizieren
  - **Reprioritisierung**: neue Prios herauskristallisieren, Spillover managen
  - **Reassignment**: Neue Tasks zuordnen für nächsten Sprint
  - **Dokumentation**: vorerst auch nur skizzenhaft bis am Ende es zusammengelegt wird

**Ziel**: Code "ready" bekommen + klare next steps

## Probleme im Workflow

### Probleme
1. **Lange Sessions**: egal welcher Tag, es war sehr zeitintensiv
2. **Context-Switches**: 3 Leute, die perma syncen und 3 Tages Sprints machen mit verschiedenen User Stories
3. **Unvollendete Features**: Wegen Timeline mehrere Spillovers
4. **Exhausting**: Durch die vorher genannten Aspekte

### Mitigation

**1: Kanban**
- mehr "fliesende" Tage als viele Unterbrechungen
- Tage haben teils ihren Inhalt weitergeführt zum nächsten Tag, aber es war genug um fertig zu werden
- Weniger Sprint-Grenzen, mehr Fokus auf "Definition of Done" - Akzeptanzkriterien wurden angepeilt

**2: Intensive Pair Programming**
- Statt Code-Review asynchron - Live-Diskussionen während des Coding
- technische/architekturelle Probleme sofort gelöst, nicht später im Retro
- Knowledge Transfer passiert in realtime, nicht in Meetings - keine Knowledge Silos

**3: Prio: Qualität > Quantität**
- Weniger Features, aber sauber implementiert
- Tests, Clean Code, Dokumentation 
- *Scope Reduction*

**4: Asynchrone Dokumentation**
- Notizen fpr Dokumentation parallel zu Code, nicht nach
- ARCHITECTURE.md wird danach mit der Vorarbeit kreiert

## Retro der 15-Tage

### Planned
```
Sprint 1-2: Basis-Architektur in einem massiven Walking Skeleton was Prototyp ähnlich ist
Sprint 3-4: Füllen des Prototypen, Refactor, Besprechung zu neuen Features
Sprint 5:   Polish, Documentation
BONUS:      User?, Filtering to show of proper use of streams?
```

### Result
```
Sprint 1-2: ✅ Walking Skeleton
Sprint 3-4: ✅ Prototypen + heavy manual testing
Sprint 5:   ⚠️  Polishing, Refactor, Tests, Logging for Monitoring
DROPPED:    ❌ User Management (nie gebraucht)
DROPPED:    ❌ ContactFilterService (zu komplex, Overkill)
```

### Learning

**Entscheidungen**:
1. **ContactFilterService gedroppet**: War geplant als "Advanced Search Service". Aber ContactService.findByX() reicht völlig. Komplexität ohne Wert.
2. **User Entity gedroppet**: "Benutzer-Management" war nice-to-have. Zeit stattdessen in Tests, Exception-Handling, Clean Code investiert.
3. **CSV Import/Export auf TODO gesetzt**: Technisch interessant, aber "nice-to-have". Fokus auf Core-Funktionalität.

### Mindset Shift

**Früh** (Sprint 1): "Lass uns viel implementieren, dann fixen wir Quality später"
**Jetzt** (Sprint 5): "Lieber 1 Feature perfekt als 3 Features kaputt"

Diese Shift kam durch:
- **Pair Programming Diskussionen**
- **Tests schreiben**
- **Clean Code Fokus**
- **Architektur-Diskussionen**

### YAYs!!! ✅

**Solide Drei-Schichten-Architektur** - Presentation / Business Logic / Persistence
**SOLID-Prinzipien** - nicht nur buzzwords, sondern konsequent implementiert
**Custom Exception Hierarchie** - Typsichere, aussagekräftige Error-Handling
**Moderne Java Best Practices** - Optional Chaining, Streams, Records
**Test-Driven Development** - 46 Tests, Coverage hoch
**Clean Code Standards** - Naming Conventions
**CI/CD Ready** - `mvn clean test` lokal & in Pipeline funktioniert
**Dokumentation** - .md files wurden geschrieben
**Exception Handling** - try-catch Blöcke überall, Schönes Debugging vor dem Feinschliff

### NAYs... ❌

**ContactImportExportService** - Tests sind da, aber implementierung nur in Dummyform
**Testing**: Mehr Tests geschrieben aber spät, Coding ohne safety net

**Aber**: Tests existieren, Features sind klar definiert, Implementation ist Sache von Sprint 6+.
