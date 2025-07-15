# UNO Konsolenspiel – Java-Projekt

Dies ist ein Java-Konsolenprojekt zur Simulation des UNO-Kartenspiels. Das Projekt beinhaltet:
- Spiel-Logik (mit Regeln, Zugverlauf, Strafkarten etc.)
- Bot-Unterstützung
- Datenbankanbindung via SQLite
- Punktevergabe und Rundenverwaltung
- Eine vollständige JavaDoc-Dokumentation im Ordner `docs/`

---

## 📁 Projektstruktur

```bash
UNO_DOCS/
├── src/                  # Java-Quellcode
├── docs/                 # Generierte JavaDoc HTML-Dokumentation
├── test/                 # Testklassen (optional)
├── sqlite/               # SQLite-Dateien
├── README.md             # Dieses Dokument
└── ...
```

---

## ▶️ Ausführen des Spiels

Zum Kompilieren und Starten benötigt man ein installiertes Java JDK (Version 17 oder höher).

### Kompilieren:

```bash
javac -d out src/*.java
```

### Starten:

```bash
java -cp out Main
```

---

## 📝 JavaDoc-Dokumentation

### Lokale Nutzung:

Die JavaDoc-Dokumentation wurde mit IntelliJ / JDK 21 generiert und liegt im Ordner `docs/`.

➡️ Öffne im Browser:
```
docs/index.html
```
(Dies leitet dich automatisch auf `package-summary.html` weiter.)

### Neu generieren (optional):

Falls du die Doku selbst neu erzeugen möchtest, stelle sicher, dass das JDK installiert ist, und führe im Projektordner aus:

```bash
javadoc -d docs src/*.java
```

> 📌 Alternativ kannst du in IntelliJ `Tools > Generate JavaDoc...` verwenden und als Scope das Modul `UNO` wählen.

---

## 🌍 JavaDocs online anzeigen (GitHub Pages)

Wenn GitHub Pages aktiviert ist, kannst du deine Doku auch online aufrufen:

**📄 [Hier klicken](https://<dein-benutzername>.github.io/UNO_DOCS/docs/index.html)**  
(Einfach `<dein-benutzername>` durch deinen GitHub-Namen ersetzen)

So aktivierst du GitHub Pages:
1. Gehe auf dein GitHub-Repository
2. Klicke auf „Settings“ > „Pages“
3. Wähle den Branch (z.B. `main`) und den Ordner `/docs`
4. Speichern → GitHub erstellt automatisch die Seite

---

## 📌 Hinweis zur Doku-Qualität

Einige Methoden und Felder enthalten noch keine vollständigen JavaDoc-Kommentare. Warnungen beim Generieren sind dokumentiert, beeinträchtigen aber die Lesbarkeit **nicht**.

---

## 👩‍💻 Entwicklerinnen

Dieses Projekt wurde im Rahmen des akademischen Lehrgangs **Software Engineering** gemeinsam entwickelt von:

- **Inna Burlaka**
- **Kaja Kocar**
- **Anna Bacanau**

📚 *UNO-Spiel als Abschlussprojekt des Academic Software Engineering Program*


---

## ✅ Lizenz

keine notwendig
