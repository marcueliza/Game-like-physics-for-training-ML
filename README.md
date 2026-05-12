<<<<<<< Updated upstream
# Game-like Physics for Training ML

Platformă open-source pentru generarea, stocarea și gestionarea de volume mari de date de simulare fizică, destinate antrenării modelelor de machine learning.

## Descriere generală

Scopul acestui proiect este dezvoltarea unui sistem capabil să genereze, să salveze, să organizeze și, în viitor, să primească prin API date provenite din simulări fizice de tip „game-like”, utile pentru antrenarea modelelor de machine learning.

Ideea principală este să construim o platformă modulară care să poată produce seturi de date sintetice bazate pe simulări fizice, de exemplu:
- animații bazate pe fizică,
- interacțiuni între obiecte,
- mișcări controlate de reguli fizice,
- scenarii de joc simplificate,
- date pentru motion prediction sau behavior modeling.

În stadiul actual, proiectul include:
- o **simulare de pendul dublu** realizată în Java,
- exportul stărilor simulării într-un fișier **CSV**.

Aceasta reprezintă prima bază funcțională pentru platforma ce va fi extinsă ulterior.

---

## Stadiul actual al proiectului

În momentul de față, proiectul este într-o fază de prototip.

### Ce este deja realizat

#### 1. Simulare fizică – pendul dublu
A fost implementată o simulare de pendul dublu în Java, cu:
- parametri fizici de bază: gravitație, mase, lungimi,
- unghiuri și viteze unghiulare,
- actualizare continuă a stării sistemului,
- randare grafică folosind Swing.

#### 2. Generare dataset CSV
Simularea exportă datele într-un fișier CSV.

În forma actuală, datasetul conține valori precum:
- timpul,
- `theta1`,
- `theta2`,
- `viteza1`,
- `viteza2`,
- `x2`,
- `y2`.

Structura poate fi extinsă cu:
- `x1`,
- `y1`,
- accelerații unghiulare,
- energii,
- metadate de simulare.

#### 3. Repository Git inițial
Repository-ul GitHub a fost creat și există deja mai multe branch-uri, ceea ce oferă o bază bună pentru introducerea unui workflow Git mai corect și mai disciplinat.

---

## Obiectivul proiectului

Obiectivul pe termen lung este construirea unei platforme open-source care să poată:

1. **Genera date de simulare**
   - pentru sisteme fizice,
   - pentru mișcări de tip joc,
   - pentru interacțiuni între obiecte,
   - pentru scenarii parametrizabile.

2. **Salva și organiza datele generate**
   - local, în fișiere,
   - ulterior printr-un sistem de metadate,
   - eventual într-o bază de date pentru management.

3. **Permite upload prin API**
   - pentru simulări generate de alte aplicații,
   - pentru extinderea sistemului la surse externe,
   - pentru integrare cu alte tool-uri.

4. **Gestiona dataseturi la scară mai mare**
   - filtrare,
   - căutare,
   - versiuni de dataset,
   - clasificare pe tipuri de simulări,
   - trasabilitate.

5. **Sprijini antrenarea modelelor ML**
   - prin date consistente,
   - reproductibile,
   - organizate,
   - ușor de procesat automat.

---

## Ce am realizat până acum

### Componenta de simulare
- Am implementat o simulare de pendul dublu.
- Avem actualizare continuă a parametrilor fizici.
- Avem afișare grafică a pendulului.
- Avem calcul pentru pozițiile punctelor din sistem.

### Componenta de export
- Salvăm datele simulării într-un fișier CSV.
- Avem o schemă inițială de dataset.
- Putem genera un istoric al evoluției simulării pentru utilizare ulterioară.

### Componenta de organizare
- Există repository Git.
- Există mai multe branch-uri.
- A fost identificată nevoia de a trece la un stil de lucru mai corect:
  - branch-uri dedicate pe feature,
  - structură Maven corectă,
  - integritate Git mai bună,
  - commits mai curate.

---

## Ce mai trebuie implementat

## Etapa 1 – Curățare și organizare corectă a proiectului

### 1. Workflow Git corect
Trebuie să folosim Git mai organizat și mai disciplinat.

### Propunere de structură Git
- `main` → branch stabil, pentru versiuni curate
- `develop` → branch de integrare pentru dezvoltare curentă
- `feature/...` → câte un branch separat pentru fiecare funcționalitate
- opțional `hotfix/...` → pentru corecturi urgente

### Exemplu de branch-uri corecte
- `feature/readme-roadmap`
- `feature/maven-setup`
- `feature/csv-export-x1-y1`
- `feature/double-pendulum-refactor`
- `feature/api-upload-foundation`

### Reguli recomandate
- Nu lucrăm direct pe `main`.
- Fiecare task nou pleacă din `develop`.
- Fiecare funcționalitate are branch separat.
- Se fac commit-uri mici și clare.
- Merge în `main` doar pentru milestone-uri stabile.

---

### 2. Commit-uri mai clare
Trebuie să avem commit-uri mai curate, ușor de urmărit.

### Exemple
- `feat: adauga export CSV pentru starea pendulului`
- `fix: corecteaza timpul pentru a porni de la 0`
- `refactor: separa logica de simulare de componenta grafica`
- `docs: adauga roadmap in README`
- `build: configureaza structura Maven`

---

### 3. Structură Maven corectă
Proiectul trebuie reorganizat într-o structură standard Maven.

### Structură recomandată

```text
project-root/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ ro/tuiasi/edu/proiect_pip/
│  │  │     ├─ model/
│  │  │     ├─ simulation/
│  │  │     ├─ export/
│  │  │     ├─ ui/
│  │  │     └─ api/        (viitor)
│  │  └─ resources/
│  └─ test/
│     └─ java/
├─ pom.xml
├─ README.md
└─ .gitignore
=======
# Salutari
>>>>>>> Stashed changes
