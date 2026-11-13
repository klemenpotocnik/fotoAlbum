# README projekta fotoAlbum

**Digitalni foto album**

Dokument opisuje namen sistema, uporabljene tehnologije, zgradbo kode ter postopek namestitve in zagona. Namenjen je uporabnikom, ki želijo projekt zagnati lokalno, in razvijalcem, ki ga želijo nadgrajevati.

**Člani ekipe:**

- Klemen Potočnik
- Ivan Kretov

## 1 O projektu

FotoAlbum je spletna aplikacija za organiziranje digitalnih fotografij v zasebne uporabniške albume. Cilj projekta je omogočiti enostavno ustvarjanje in urejanje albumov, nalaganje fotografij ter pregledovanje zbirke na enem mestu.

### Glavne funkcionalnosti

- Registracija z e-pošto in geslom, prijava ter odjava.
- Ločena zasebna zbirka albumov in slik za vsak uporabniški račun.
- Ustvarjanje, pregled, urejanje in brisanje albumov.
- Nalaganje ene ali več fotografij JPG oziroma PNG v izbrani album.
- Pregled vseh fotografij ali fotografij posameznega albuma ter prikaz podrobnosti.
- Označevanje fotografij kot priljubljenih in prikaz samo priljubljenih.
- Filtriranje fotografij po obdobju nastanka in branje datuma iz metapodatkov EXIF.
- Brisanje posameznih fotografij, vseh fotografij albuma ali vseh albumov.

### Delovanje sistema

Uporabniški vmesnik React pošilja zahteve REST v Spring Boot. Backend v PostgreSQL shranjuje uporabnike, njihove albume in podatke o fotografijah, slikovne datoteke pa v mapo `uploads`. Gesla shranjuje kot BCrypt hash. Prijavo ohranja strežniška seja v piškotku `HttpOnly`; zahteve, ki spreminjajo podatke, so zaščitene s CSRF. Backend preverja lastništvo tudi pri slikovnih datotekah in množičnem brisanju. Pri brisanju fotografije odstrani zapis iz baze in datoteko z diska.

Omejitvi velikosti sta 10 MB za posamezno fotografijo in 100 MB za celotno zahtevo. Fotografije brez prepoznanega datuma EXIF ostanejo brez datuma nastanka; pri nefiltriranem razvrščanju se uporabi datum nalaganja. Datumski filter upošteva datum nastanka, zato fotografij brez tega podatka ne vključi med rezultate.

Neprijavljen obiskovalec se preusmeri na `/login`. Na `/signup` ustvari račun, nato se prijavi in odpre svojo zbirko na `/`. Registracija zahteva veljavno e-pošto, geslo z najmanj 8 znaki ter ujemanje ponovitve gesla. Seja se ohrani ob osvežitvi strani, po 30 minutah nedejavnosti oziroma ponovnem zagonu backenda pa je potrebna nova prijava. Odjava počisti prikazano zbirko.

### Kazalo

- [1 O projektu](#1-o-projektu)
- [2 Uporabljene tehnologije](#2-uporabljene-tehnologije)
- [3 Struktura projekta](#3-struktura-projekta)
- [4 Navodila za nameščanje in zagon](#4-navodila-za-nameščanje-in-zagon)
- [5 Navodila za razvijalce](#5-navodila-za-razvijalce)
- [6 Standardi kodiranja](#6-standardi-kodiranja)
- [7 Besednjak](#7-besednjak)
- [Dokumentacija](#dokumentacija)

Repozitorij: [klemenpotocnik/fotoAlbum](https://github.com/klemenpotocnik/fotoAlbum)

## Vizija projekta

### Opis rešitve

FotoAlbum je spletna aplikacija za organizacijo osebnih fotografij.
Namenjena je uporabnikom, ki želijo fotografije s potovanj, družinskih
dogodkov in drugih priložnosti zbrati na enem mestu ter jih razporediti
v pregledne albume.

Z aplikacijo želimo olajšati shranjevanje, pregledovanje in iskanje
fotografij. Vsak uporabnik ima svoj račun in zasebno zbirko albumov.
Albume lahko razvrsti po kategorijah, fotografije pa označi kot
priljubljene in jih filtrira po datumu nastanka. Tako lažje najde
želene fotografije tudi v večji zbirki.

### Ključne funkcionalnosti

- Registracija, prijava in odjava uporabnika.
- Ustvarjanje, pregledovanje, urejanje in brisanje albumov.
- Nalaganje fotografij JPG in PNG v izbrani album.
- Pregled fotografij in njihovih podrobnosti.
- Označevanje fotografij kot priljubljenih in prikaz priljubljenih.
- Filtriranje fotografij po datumu nastanka.
- Brisanje posameznih fotografij ali vseh fotografij albuma.
- Izbira kategorije albuma iz pripravljenega seznama.
- Dodajanje lastne kategorije prek možnosti Drugo, ki se shrani
  za uporabo pri drugih uporabnikovih albumih.
- Filtriranje albumov po kategoriji.

### Obseg rešitve in morebitne omejitve

Aplikacija je namenjena osebni uporabi prek spletnega brskalnika.
Vsak uporabnik lahko dostopa samo do svojih albumov in fotografij
ter kategorij, povezanih s svojim računom. Javno deljenje albumov,
skupno urejanje, obdelava fotografij in nalaganje videoposnetkov
niso vključeni v obseg trenutne rešitve.

Album ima lahko eno kategorijo ali pa ostane brez nje. Na voljo so
kategorije Potovanja, Družina, Prijatelji, Dogodki, Narava, Šport,
Vsakdan in Drugo. Pri izbiri Drugo uporabnik vpiše lastno kategorijo,
ki je nato na voljo tudi pri njegovih drugih albumih. Ime kategorije
je omejeno na 100 znakov.

Podprte so fotografije JPG in PNG. Posamezna fotografija je lahko
velika največ 10 MB, celotna zahteva za nalaganje pa največ 100 MB.
Filtriranje po datumu nastanka je odvisno od metapodatkov fotografije.
Fotografije brez prepoznanega datuma nastanka niso vključene
v rezultate datumskega filtra.

Za delovanje morajo biti dosegljivi strežniški del aplikacije,
podatkovna baza in mapa s shranjenimi fotografijami.

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
| Spring Security | 7.1.1 | Prijava, seje, BCrypt in zaščita CSRF |
| Spring Boot DevTools | 4.1.1 | Samodejni ponovni zagon med razvojem |
| Spring Framework / Hibernate | 7.0.9 / 7.4.5.Final | Ogrodje in preslikava ORM |
| Maven / Maven Wrapper | 3.9.16 / 3.3.4 | Odvisnosti in gradnja |
| PostgreSQL JDBC | 42.7.13 | Povezava z bazo PostgreSQL |
| Lombok | 1.18.46 | Generiranje pomožnih metod |
| springdoc OpenAPI WebMVC UI | 3.1.1 | Dokumentacija API in Swagger UI |
| metadata-extractor | 2.19.0 | Branje metapodatkov fotografij |

Sistem uporablja PostgreSQL; preverjeno razvojno okolje ima strežnik različice 18.6. Za frontend namesti Node.js 24 z npm. Git se uporablja za nadzor različic, GitHub pa za sodelovanje in hrambo kode.

## 3 Struktura projekta

```text
fotoAlbum/
├── backend/
│   ├── database/001_accounts.sql # priprava tabel trenutne aplikacije
│   ├── .mvn/wrapper/           # nastavitev Maven Wrapper
│   ├── mvnw in mvnw.cmd        # zagon Maven
│   ├── pom.xml                 # odvisnosti in Java 25
│   ├── src/main/
│   │   ├── java/fotoAlbum/fotoAlbum/
│   │   │   ├── FotoAlbumApplication.java
│   │   │   ├── config/         # Spring Security
│   │   │   ├── controller/     # končne točke REST
│   │   │   ├── service/        # obdelava fotografij
│   │   │   ├── repository/     # dostop do baze
│   │   │   ├── model/          # AppUser, Album in Photo
│   │   │   └── dto/            # podatki zahtev in odgovorov
│   │   └── resources/application.properties
│   ├── uploads/                # slike pri zagonu iz backend
│   └── target/                 # rezultat gradnje
├── frontend/
│   ├── src/
│   │   ├── assets/             # slike in statični viri
│   │   ├── auth/               # seja in prijavljeni uporabnik
│   │   ├── components/         # kartice, galerije, obrazci
│   │   ├── pages/              # Home, AlbumDetails in AuthPage
│   │   ├── services/           # klici REST prek fetch
│   │   ├── oblika/             # slogovne datoteke CSS
│   │   ├── App.jsx             # poti aplikacije
│   │   └── main.jsx            # vstopna točka React
│   ├── package.json in package-lock.json
│   ├── vite.config.js          # razvojni proxy /api
│   └── eslint.config.js        # pravila preverjanja kode
├── .gitignore                  # izključitve iz Git
└── README.md                   # dokumentacija repozitorija
```

### Razdelitev odgovornosti

Controller sprejme zahtevo HTTP, service izvaja obdelavo fotografij, repository pa bere in shranjuje entitete v bazo. DTO določa obliko podatkov, ki jih backend izmenjuje z odjemalcem. AlbumController uporablja AlbumRepository tudi neposredno, odgovore pa oblikuje z DTO. `CurrentUser` določi prijavljenega uporabnika; lastništvo albuma določi strežnik, ne odjemalec.

Frontend ločuje prikaz v components in pages od omrežnih klicev v services. Slogi so v mapi oblika ter v App.css in index.css. Vite preusmeri razvojne zahteve /api na http://localhost:8080.

Mape target, node_modules, dist in uploads so lokalno ustvarjene in jih .gitignore izključuje. Privzeta mapa fotografij je `backend/uploads`, tako pri zagonu iz glavne mape projekta kot pri zagonu iz mape `backend`.

## 4 Navodila za nameščanje in zagon

### 4.1 Predpogoji

- Namesti JDK 25 ter nastavi JAVA_HOME na mapo JDK in dodaj njegovo mapo bin v PATH.
- Namesti Node.js 24 z npm in Git. Maven se prenese prek priloženega Maven Wrapper, zato ločena namestitev ni potrebna.
- Namesti PostgreSQL in preveri, da je zagnan. Aplikacija se povezuje z bazo na tvojem računalniku prek naslova `localhost` in vrat `5432`. Za izvajanje spodnjih ukazov potrebuješ orodje `psql`, ki je del namestitve PostgreSQL.
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

Pred prvim zagonom aplikacije ustvari bazo `fotoalbum`, kot je prikazano zgoraj, in vanjo dodaj pripravljeno shemo iz datoteke [`backend/database/001_accounts.sql`](backend/database/001_accounts.sql).

V pgAdmin izberi bazo `fotoalbum` in odpri **Query Tool**. Vanj kopiraj celotno vsebino datoteke `001_accounts.sql` ter jo izvedi z gumbom **Execute** ali tipko **F5**. S tem ustvariš tabele za uporabnike, albume in fotografije ter povezave med njimi. Nato lahko zaženeš backend.

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
.\mvnw.cmd spring-boot:run
```

Uporabi isto geslo kot pri ustvarjanju računa. Spremenljivke veljajo za trenutni terminal in njegove procese. APP_UPLOAD_DIR določi mapo za slikovne datoteke; aplikacija jo ustvari ob prvem nalaganju. Ta mapa mora biti zapisljiva.

Vtičnik `spring-boot-maven-plugin` je deklariran v `pom.xml`. Na Linuxu oziroma macOS uporabi ./mvnw namesto .\mvnw.cmd in spremenljivke nastavi z export IME=vrednost. Če je treba, prej izvedi chmod +x mvnw.

### 4.6 Zagon frontenda v drugem terminalu

Odpri nov terminal v korenski mapi fotoAlbum:

```powershell
cd frontend
npm ci
npm run dev
```

Odpri http://localhost:5173 oziroma naslov, izpisan v terminalu. Backend je na http://localhost:8080. Frontend uporablja relativne poti /api; razvojni proxy je nastavljen v frontend/vite.config.js. Trenutna koda ne zahteva frontend spremenljivk VITE_*.

### 4.7 Preverjanje delovanja

Odpri http://localhost:5173. Najprej se prikaže prijava; prek povezave Ustvari račun se registriraj, nato se prijavi. Nov račun ima prazno zbirko. Ustvari album, naloži JPG ali PNG in preveri priljubljene ter ohranitev prijave ob osvežitvi. Gumb Odjava vrne na prijavo.

Neposreden neprijavljen klic `/api/albums` vrne `401`, ne praznega seznama. Tuj ali neobstoječ album oziroma fotografija prijavljenemu uporabniku vrne `404`. Zapisovalna zahteva brez veljavnega žetona CSRF vrne `403`.

### API za prijavo

| Zahteva | Namen |
| --- | --- |
| `GET /api/auth/csrf` | Vrne žeton in ime glave za naslednje zapisovalne zahteve. |
| `POST /api/auth/signup` | JSON z `email` in `password`; ustvari račun in vrne `201`. |
| `POST /api/auth/login` | Obrazec `application/x-www-form-urlencoded` z `email` in `password`; vzpostavi sejo. |
| `GET /api/auth/me` | Vrne ID in e-pošto trenutnega uporabnika. |
| `POST /api/auth/logout` | Razveljavi sejo in vrne `204`. |

CSRF je potreben tudi pri prijavi, registraciji, odjavi in nalaganju datotek. Po prijavi in odjavi pridobi nov žeton. Frontend to ureja v `services/api.js` in `authService.js`. Gesel ali žetonov za prijavo ne shranjuje v localStorage.

Swagger UI je na http://localhost:8080/swagger-ui/index.html, opis OpenAPI pa na `/v3/api-docs`; oba zahtevata prijavo. Če frontend odpreš na `localhost:5173` in se tam prijaviš, brskalnik sejni piškotek pošlje tudi na `localhost:8080`. Naslovov `localhost` in `127.0.0.1` pri tem ne mešaj. Pri zapisovalnih klicih iz drugih odjemalcev moraš poleg seje poslati tudi žeton CSRF.

Pri objavi uporabi HTTPS, nastavi `SESSION_COOKIE_SECURE=true` in ohrani enoten izvor za frontend in `/api`. Razvojni Vite proxy ni nastavitev produkcijskega strežnika.

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

`npm run lint` preveri pravila kode, `npm run build` gradnjo frontenda, Maven pa gradnjo backenda. Projekt trenutno nima avtomatiziranih testov, zato funkcionalnosti preveri ročno.

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

`<izbrane-datoteke>` zamenjaj z dejanskimi potmi datotek, `feature/opis-spremembe` pa z imenom svoje veje iz poglavja 5.1.

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

## 7 Besednjak

Besednjak pojasnjuje izraze, ki jih uporabljamo pri organiziranju in pregledovanju fotografij v aplikaciji fotoAlbum.

| Izraz | Razlaga |
| --- | --- |
| **Album** | Skupina fotografij, ki jih uporabnik zbere pod skupnim naslovom, na primer »Poletje 2026«. Album ima lahko opis in kategorijo. |
| **Fotografija** | Slika, ki jo uporabnik naloži v izbrani album. Poleg slike so na voljo podatki o njenem imenu, datumu nalaganja, morebitnem datumu nastanka in priljubljenosti. |
| **Zasebna zbirka** | Vsi albumi in fotografije posameznega uporabnika. Do njih lahko dostopa in jih ureja samo njihov lastnik. |
| **Lastnik albuma** | Uporabnik, ki je ustvaril album. Album in fotografije v njem pripadajo njegovi zasebni zbirki. |
| **Kategorija albuma** | Tematska oznaka za razvrščanje albumov, na primer »Potovanja«, »Družina« ali »Narava«. Album ima lahko eno kategorijo ali ostane brez nje. |
| **Lastna kategorija** | Kategorija, ki jo uporabnik poimenuje sam prek možnosti »Drugo«. Po shranjevanju jo lahko izbere tudi pri drugih svojih albumih. |
| **Brez kategorije** | Oznaka za album, ki mu uporabnik ni dodelil kategorije. Takšne albume lahko prikaže z istoimenskim filtrom. |
| **Priljubljena fotografija** | Fotografija, ki jo uporabnik označi z zvezdico, da jo lažje najde. Prikaže lahko samo priljubljene fotografije, ne da bi jih prestavljal v drug album. |
| **Galerija fotografij** | Pregled fotografij v obliki mreže sličic. Omogoča ogled zbirke in izbiro posamezne fotografije za prikaz podrobnosti. |
| **Metapodatki fotografije** | Dodatni podatki, zapisani v slikovni datoteki. Aplikacija iz njih poskuša prebrati datum nastanka fotografije. |
| **Datum nastanka fotografije** | Datum in čas, ko je bila fotografija posneta. Podatek aplikacija prebere iz datoteke, če je v njej na voljo. |
| **Datum nalaganja fotografije** | Datum in čas, ko je uporabnik fotografijo dodal v aplikacijo. Lahko se razlikuje od datuma, ko je bila posneta. |
| **Datum ustvarjanja albuma** | Datum, ko je uporabnik ustvaril album v aplikaciji. Ne določa, kdaj so bile posnete fotografije v njem. |
| **Filtriranje fotografij po datumu** | Prikaz fotografij, katerih datum nastanka je v izbranem obdobju, vključno z začetnim in končnim dnem. Fotografije brez znanega datuma nastanka se pri tem ne prikažejo. |

## Dokumentacija

- [Primeri uporabe](docs/primeri-uporabe.md)
