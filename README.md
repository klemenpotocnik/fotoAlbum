# README projekta fotoAlbum

**Digitalni foto album**

Dokument opisuje namen sistema, uporabljene tehnologije, zgradbo kode ter postopek namestitve in zagona. Namenjen je uporabnikom, ki želijo projekt zagnati lokalno, in razvijalcem, ki ga želijo nadgrajevati.

## 1 O projektu

FotoAlbum je spletna aplikacija za organiziranje digitalnih fotografij v albume. Cilj projekta je omogočiti enostavno ustvarjanje in urejanje albumov, nalaganje fotografij ter pregledovanje zbirke na enem mestu.

### Glavne funkcionalnosti

- Ustvarjanje, pregled, urejanje in brisanje albumov.
- Nalaganje ene ali več fotografij JPG oziroma PNG v izbrani album.
- Pregled vseh fotografij ali fotografij posameznega albuma ter prikaz podrobnosti.
- Označevanje fotografij kot priljubljenih in prikaz samo priljubljenih.
- Filtriranje fotografij po obdobju nastanka in branje datuma iz metapodatkov EXIF.
- Brisanje posameznih fotografij, vseh fotografij albuma ali vseh albumov.

### Delovanje sistema

Uporabniški vmesnik React pošilja zahteve REST v Spring Boot. Backend v PostgreSQL shranjuje albume in podatke o fotografijah, slikovne datoteke pa v mapo uploads. Pri brisanju fotografije odstrani zapis iz baze in datoteko z diska.

Omejitvi velikosti sta 10 MB za posamezno fotografijo in 100 MB za celotno zahtevo. Fotografije brez prepoznanega datuma EXIF ostanejo brez datuma nastanka; pri nefiltriranem razvrščanju se uporabi datum nalaganja. Datumski filter upošteva datum nastanka, zato fotografij brez tega podatka ne vključi med rezultate.

### Kazalo

- [1 O projektu](#1-o-projektu)
- [2 Uporabljene tehnologije](#2-uporabljene-tehnologije)
- [3 Struktura projekta](#3-struktura-projekta)
- [4 Navodila za nameščanje in zagon](#4-navodila-za-nameščanje-in-zagon)
- [5 Navodila za razvijalce](#5-navodila-za-razvijalce)
- [6 Standardi kodiranja](#6-standardi-kodiranja)

Repozitorij: [klemenpotocnik/fotoAlbum](https://github.com/klemenpotocnik/fotoAlbum)

## 2 Uporabljene tehnologije

Različice frontenda so povzete iz package-lock.json. Datoteka package.json določa dovoljene razpone; npm ci namesti zaklenjene različice. Različice backenda določa pom.xml in upravljanje odvisnosti Spring Boot 4.1.1.

### Frontend

| Tehnologija | Različica | Namen |
| --- | --- | --- |
| React in React DOM | 19.3.1 | Komponente in izris vmesnika |
| React Router | 8.4.0 | Navigacija med stranmi |
| Vite | 8.3.4 | Razvojni strežnik in gradnja |
| @vitejs/plugin-react | 6.1.2 | Podpora React v Vite |
| ESLint / @eslint/js | 10.12.0 / 10.0.1 | Statično preverjanje kode |
| eslint-plugin-react-hooks | 7.1.1 | Pravila za React Hooks |
| eslint-plugin-react-refresh | 0.5.7 | Pravila za osveževanje komponent |
| globals | 17.13.0 | Globalna imena za ESLint |
| @types/react in @types/react-dom | 19.3.0 | Deklaracije tipov za orodja |
| JavaScript, JSX, HTML in CSS | — | Jeziki vmesnika in oblikovanja |

### Backend in podatkovna baza

| Tehnologija | Različica | Namen |
| --- | --- | --- |
| Java JDK | 25 | Izvajanje in prevajanje |
| Spring Boot in njegovi starterji | 4.1.1 | Web MVC, JPA, validacija in podpora za teste |
| Spring Boot DevTools | 4.1.1 | Samodejni ponovni zagon med razvojem |
| Spring Framework / Hibernate | 7.0.9 / 7.4.5.Final | Ogrodje in preslikava ORM |
| Maven / Maven Wrapper | 3.9.16 / 3.3.4 | Odvisnosti in gradnja |
| PostgreSQL JDBC | 42.7.13 | Povezava z bazo PostgreSQL |
| Lombok | 1.18.46 | Generiranje pomožnih metod |
| springdoc OpenAPI WebMVC UI | 3.1.1 | Dokumentacija API in Swagger UI |
| metadata-extractor | 2.19.0 | Branje metapodatkov fotografij |

Sistem uporablja podatkovno bazo PostgreSQL. Različica strežnika PostgreSQL v projektu ni pripeta. Za frontend namesti Node.js 24 z npm. Git se uporablja za nadzor različic, GitHub pa za sodelovanje in hrambo kode.

V `pom.xml` so še vključene testne odvisnosti `spring-boot-starter-data-jpa-test`, `spring-boot-starter-validation-test` in `spring-boot-starter-webmvc-test` različice 4.1.1. Projekt trenutno nima napisanih avtomatiziranih testov.

## 3 Struktura projekta

```text
fotoAlbum/
├── backend/
│   ├── .mvn/wrapper/           # nastavitev Maven Wrapper
│   ├── mvnw in mvnw.cmd        # zagon Maven
│   ├── pom.xml                 # odvisnosti in Java 25
│   ├── src/main/
│   │   ├── java/fotoAlbum/fotoAlbum/
│   │   │   ├── FotoAlbumApplication.java
│   │   │   ├── controller/     # končne točke REST
│   │   │   ├── service/        # obdelava fotografij
│   │   │   ├── repository/     # dostop do baze
│   │   │   ├── model/          # entiteti Album in Photo
│   │   │   └── dto/            # podatki zahtev in odgovorov
│   │   └── resources/application.properties
│   └── target/                 # rezultat gradnje
├── frontend/
│   ├── src/
│   │   ├── assets/             # slike in statični viri
│   │   ├── components/         # kartice, galerije, obrazci
│   │   ├── pages/              # Home in AlbumDetails
│   │   ├── services/           # klici REST prek fetch
│   │   ├── oblika/             # slogovne datoteke CSS
│   │   ├── App.jsx             # poti aplikacije
│   │   └── main.jsx            # vstopna točka React
│   ├── package.json in package-lock.json
│   ├── vite.config.js          # razvojni proxy /api
│   └── eslint.config.js        # pravila preverjanja kode
├── uploads/                    # lokalno naložene fotografije
├── .gitignore                  # izključitve iz Git
└── README.md                   # dokumentacija repozitorija
```

### Razdelitev odgovornosti

Controller sprejme zahtevo HTTP, service izvaja obdelavo fotografij, repository pa bere in shranjuje entitete v bazo. DTO določa obliko podatkov, ki jih backend izmenjuje z odjemalcem. AlbumController trenutno uporablja AlbumRepository tudi neposredno.

Frontend ločuje prikaz v components in pages od omrežnih klicev v services. Slogi so v mapi oblika ter v App.css in index.css. Vite preusmeri razvojne zahteve /api na http://localhost:8080.

Mape target, node_modules, dist in uploads so lokalno ustvarjene in jih .gitignore izključuje. Relativna pot uploads je odvisna od delovne mape backenda: ob zagonu iz backend je to backend/uploads.

## 4 Navodila za nameščanje in zagon

### 4.1 Predpogoji

- Namesti JDK 25 ter nastavi JAVA_HOME na mapo JDK in dodaj njegovo mapo bin v PATH.
- Namesti Node.js 24 z npm in Git. Maven se prenese prek priloženega Maven Wrapper, zato ločena namestitev ni potrebna.
- Namesti in zaženi PostgreSQL. Pri lokalnem zagonu je pričakovana povezava na localhost:5432.
- Za prvi prenos Maven in npm odvisnosti potrebuješ dostop do interneta. Vrati 8080 za backend in 5173 za frontend naj bosta prosti.

V PowerShellu preveri namestitve:

```powershell
java -version
node --version
npm --version
git --version
```

### 4.2 Kloniranje repozitorija

```powershell
git clone https://github.com/klemenpotocnik/fotoAlbum.git
cd fotoAlbum
```

### 4.3 Priprava PostgreSQL

V pgAdmin Query Tool ali psql se kot skrbnik poveži z obstoječo bazo postgres. Naslednja ukaza izvedi ločeno, zunaj transakcije. Primer ustvari namenski razvojni račun in prazno bazo:

```sql
CREATE ROLE fotoalbum_user WITH LOGIN PASSWORD 'izberi_lokalno_geslo';
CREATE DATABASE fotoalbum OWNER fotoalbum_user;
```

Geslo v primeru zamenjaj z lastnim. Če baza ali račun že obstajata, uporabi njune obstoječe podatke. Aplikacijski uporabnik mora imeti pravice za ustvarjanje in spreminjanje tabel v svoji bazi.

Tabel ni treba ročno ustvarjati: nastavitev spring.jpa.hibernate.ddl-auto=update ob zagonu posodobi shemo. Podatki o albumih in fotografijah se shranjujejo v tabeli albums in photos.

### 4.4 Nastavitve baze in shranjevanja

Osnovne nastavitve so v backend/src/main/resources/application.properties. Trenutne privzete vrednosti so baza fotoalbum, uporabnik postgres, lokalno geslo root ter mapa uploads. Za svoj račun jih preglasi z okoljskimi spremenljivkami v terminalu, iz katerega boš zagnal backend.

### 4.5 Zagon backenda v prvem terminalu

Iz korenske mape repozitorija v PowerShellu izvedi:

```powershell
cd backend
$env:SPRING_DATASOURCE_URL = 'jdbc:postgresql://localhost:5432/fotoalbum'
$env:SPRING_DATASOURCE_USERNAME = 'fotoalbum_user'
$env:SPRING_DATASOURCE_PASSWORD = 'izberi_lokalno_geslo'
$env:APP_UPLOAD_DIR = (Join-Path (Get-Location) 'uploads')
.\mvnw.cmd org.springframework.boot:spring-boot-maven-plugin:4.1.1:run
```

Uporabi isto geslo kot pri ustvarjanju računa. Spremenljivke veljajo za trenutni terminal in njegove procese. APP_UPLOAD_DIR določi mapo za slikovne datoteke; aplikacija jo ustvari ob prvem nalaganju. Ta mapa mora biti zapisljiva.

Uporabljen je polni Maven cilj, ker pom.xml trenutno ne deklarira vtičnika spring-boot-maven-plugin. Na Linuxu oziroma macOS uporabi ./mvnw namesto .\mvnw.cmd in spremenljivke nastavi z export IME=vrednost. Če je treba, prej izvedi chmod +x mvnw.

### 4.6 Zagon frontenda v drugem terminalu

Odpri nov terminal v korenski mapi fotoAlbum:

```powershell
cd frontend
npm ci
npm run dev
```

Odpri http://localhost:5173 oziroma naslov, izpisan v terminalu. Backend je na http://localhost:8080. Frontend uporablja relativne poti /api; razvojni proxy je nastavljen v frontend/vite.config.js. Trenutna koda ne zahteva frontend spremenljivk VITE_*.

Datoteka .env se v Spring Boot ne naloži samodejno. Za backend uporabi prikazane okoljske spremenljivke ali nastavitve v razvojnem okolju. Gesel in lokalnih .env datotek ne vključuj v Git.

### 4.7 Preverjanje delovanja

Odpri http://localhost:8080/api/albums; v prazni bazi pričakuj []. Dokumentacija API je na http://localhost:8080/swagger-ui/index.html, opis OpenAPI pa na /v3/api-docs. V vmesniku ustvari album, naloži JPG ali PNG, označi fotografijo kot priljubljeno in preveri prikaz po ponovnem nalaganju strani.

### Pogoste težave

- Povezava z bazo ne uspe: preveri delovanje PostgreSQL, ime baze, vrata ter uporabniško ime in geslo.
- Fotografije se po spremembi načina zagona ne prikažejo: preveri APP_UPLOAD_DIR in mapo z obstoječimi datotekami.
- Frontend ne doseže API: backend mora delovati na naslovu iz vite.config.js. Pri spremembi vrat uskladi proxy.
- Zagon ustaviš s Ctrl+C v obeh terminalih. Za ohranitev zbirke potrebuješ tako bazo PostgreSQL kot mapo s fotografijami.

## 5 Navodila za razvijalce

Spremembe pripravljaj na ločeni veji in jih pred združitvijo v main preglej v pull requestu. Veja naj vsebuje eno smiselno funkcionalnost ali popravek.

### 5.1 Priprava veje

```powershell
git switch main
git pull --ff-only origin main
git switch -c feature/opis-spremembe
```

Za popravke lahko uporabiš predpono fix/. Če nimaš pravic za pisanje v repozitorij, ustvari fork in pull request iz svoje kopije.

### 5.2 Preverjanje sprememb

V mapi frontend zaženi preverjanje kode in produkcijsko gradnjo:

```powershell
npm run lint
npm run build
```

V mapi backend preveri prevajanje in izdelavo paketa:

```powershell
.\mvnw.cmd package
```

Projekt trenutno nima avtomatiziranih testov na backendu ali frontendu. Ukaz Maven preveri gradnjo backenda; `npm run lint` preveri pravila kode, `npm run build` pa gradnjo frontenda. Uspešna gradnja sama po sebi ne potrdi pravilnega delovanja funkcionalnosti.

Po zagonu obeh delov aplikacije in PostgreSQL ročno preveri:

- Ustvarjanje, urejanje in brisanje albuma.
- Nalaganje JPG in PNG fotografij ter prikaz njihovih podrobnosti.
- Označevanje in odstranjevanje priljubljenosti ter prikaz samo priljubljenih fotografij.
- Filtriranje po datumu nastanka s fotografijami, ki vsebujejo datum EXIF.
- Ohranitev podatkov po ponovnem nalaganju strani.
- Brisanje fotografij ter odstranitev pripadajočih datotek iz mape `uploads`.

### 5.3 Commit in pull request

```powershell
git add <izbrane-datoteke>
git commit -m "feat: kratek opis spremembe"
git push -u origin feature/opis-spremembe
```

V GitHubu odpri pull request proti main. Opiši namen, spremenjeno vedenje in izvedena preverjanja; pri spremembah vmesnika priloži sliko. Po pregledu in uspešnih preverjanjih združi spremembo ter lokalno posodobi main.

### 5.4 Datoteke v repozitoriju

Vključi izvorno kodo, Maven Wrapper, pom.xml, package.json, package-lock.json in dokumentacijo. Ob spremembi odvisnosti posodobi tudi zaklenjene različice. Ne vključuj node_modules, target, dist, uploads, dnevnikov, gesel ali osebnih nastavitev IDE. Upoštevaj `.gitignore`.

Ob spremembi poti API, predpogojev ali konfiguracije posodobi tudi README.md v korenu repozitorija, da navodila ostanejo uporabna za druge razvijalce.

## 6 Standardi kodiranja

Pri novih prispevkih uporabljaj naslednja pravila. Obstoječi ESLint preverja JavaScript in JSX; pravila za Javo in CSS so dogovor za razvoj ter niso samodejno preverjena s formatterjem.

### 6.1 Splošna pravila

- Uporabljaj opisna imena v angleščini. Besedila vmesnika naj bodo jasna in jezikovno usklajena.
- Datoteke shrani v UTF-8. Ohrani obstoječi slog datoteke in ne spreminjaj oblikovanja nepovezane kode.
- Odstrani neuporabljene importe, spremenljivke in začasne izpise. Komentarji naj pojasnijo namen ali razlog.
- Funkcije in komponente naj imajo jasno odgovornost. Skupno logiko izloči, da se izogneš podvajanju.
- Gesla in lokalne poti nastavljaj prek okolja; občutljivih podatkov ne zapisuj v kodo ali dnevnike.

### 6.2 Java in Spring Boot

- Razrede poimenuj v PascalCase, metode in polja v camelCase, konstante v UPPER_SNAKE_CASE. Pri novi kodi uporabljaj štiri presledke za zamik.
- Ohranjaj sloje controller, service, repository, model in dto. Poslovno logiko novih funkcionalnosti umeščaj v service, dostop do baze pa v repository.
- Odvisnosti podajaj prek konstruktorjev. Vhodne podatke preveri na strežniku in uporabljaj ustrezne statuse HTTP.
- Za spremembe stanja upoštevaj transakcije in usklajenost baze z datotekami. Odgovori API naj uporabljajo jasne DTO, kadar ne potrebujejo celotne entitete.

### 6.3 React in JavaScript

- Komponente in njihove datoteke poimenuj v PascalCase, npr. PhotoCard.jsx; funkcije in spremenljivke v camelCase.
- Uporabljaj funkcijske komponente in React Hooks. Hooke kliči na vrhu komponente ali lastnega hooka in pravilno določi odvisnosti useEffect.
- Stanje posodabljaj brez neposrednega spreminjanja obstoječih objektov. Elementom seznamov dodeli stabilen key, na primer id.
- Omrežne klice umeščaj v src/services. Preveri response.ok ter uporabniku prikaži stanje nalaganja in razumljivo napako.
- Upoštevaj eslint.config.js. Pri novi kodi uporabljaj dosleden zamik; ohrani slog okoliške datoteke.

### 6.4 CSS in preverjanje kakovosti

Uporabljaj opisna imena razredov CSS, ohrani povezavo sloga s komponento in preveri prikaz na različnih širinah zaslona. Obrazci naj imajo oznake, slike opisni alt, gumbi pa razumljiv namen. Pred oddajo izvedi preverjanja iz poglavja 5 in za novo poslovno logiko dodaj smiselne teste.
