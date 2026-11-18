# Primeri uporabe projekta fotoAlbum

Dokument opisuje cilje, predpogoje, scenarije, alternativne tokove in izjeme
pri uporabi zasebne zbirke albumov in fotografij.

## Akterji

**Neprijavljen obiskovalec** uporablja registracijo in prijavo. Ustvarjen
račun je predpogoj za prijavo.
**Prijavljen uporabnik** upravlja svojo zasebno zbirko in se lahko odjavi.

## PU-01 Registracija uporabnika

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Registracija uporabnika |
| **ID** | PU-01 |
| **Cilj** | Uporabnik ustvari račun za uporabo svojega zasebnega foto albuma. |
| **Akterji** | Neprijavljen obiskovalec. |
| **Predpogoji** | Obiskovalec ima odprto aplikacijo in ni prijavljen. |
| **Stanje sistema po PU** | Uporabniški račun je shranjen. Prikaže se prijavna stran z obvestilom o uspešni registraciji. Uporabnik še ni prijavljen. |

### Scenarij

1. Obiskovalec na prijavni strani izbere »Ustvari račun«.
2. Sistem prikaže registracijski obrazec.
3. Obiskovalec vnese e-poštni naslov, geslo in ponovitev gesla.
4. Obiskovalec izbere »Registracija«.
5. Sistem preveri veljavnost podatkov, ujemanje gesel in razpoložljivost e-poštnega naslova.
6. Sistem ustvari uporabniški račun.
7. Sistem prikaže prijavno stran z obvestilom o uspešno ustvarjenem računu.

### Alternativni tokovi

- **3a.** Obiskovalec izbere »Prijavi se«. Sistem odpre prijavno stran, račun se ne ustvari.
- **5a.** Obvezna polja so prazna ali e-pošta ni veljavna. Sistem opozori na napako. Scenarij se nadaljuje pri koraku 3 s popravkom podatkov.
- **5b.** Geslo vsebuje manj kot 8 znakov. Sistem opozori na zahtevano dolžino. Scenarij se nadaljuje pri koraku 3 z vnosom ustreznega gesla in njegove ponovitve.
- **5c.** Gesli se ne ujemata. Sistem prikaže opozorilo. Scenarij se nadaljuje pri koraku 3 s ponovnim vnosom gesel.
- **5d.** E-poštni naslov je že uporabljen. Sistem obvesti obiskovalca. Scenarij se nadaljuje pri koraku 3 z vnosom drugega naslova.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Registracije ni mogoče dokončati; sistem prikaže napako in omogoči ponovni poskus.

## PU-02 Prijava uporabnika

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Prijava uporabnika |
| **ID** | PU-02 |
| **Cilj** | Uporabnik se prijavi in pridobi dostop do svojih albumov ter fotografij. |
| **Akterji** | Neprijavljen obiskovalec. |
| **Predpogoji** | Obiskovalec ima ustvarjen račun, ni prijavljen in odpre aplikacijo. |
| **Stanje sistema po PU** | Uporabnik je prijavljen. Vzpostavljena je seja in prikazana njegova zasebna zbirka. |

### Scenarij

1. Sistem prikaže prijavni obrazec.
2. Obiskovalec vnese e-poštni naslov in geslo.
3. Obiskovalec izbere »Prijava«.
4. Sistem preveri prijavne podatke.
5. Sistem vzpostavi uporabniško sejo.
6. Sistem odpre domačo stran in prikaže uporabnikove albume ter fotografije.

### Alternativni tokovi

- **2a.** Obiskovalec namesto prijave izbere »Ustvari račun«. Nadaljuje s primerom uporabe [PU-01 Registracija uporabnika](#pu-01-registracija-uporabnika).
- **3a.** Obvezno polje je prazno ali oblika e-pošte ni veljavna. Sistem opozori obiskovalca. Scenarij se nadaljuje pri koraku 2 s popravkom podatkov.
- **4a.** E-pošta ali geslo ni pravilno. Sistem prikaže »Napačna e-pošta ali geslo«. Scenarij se nadaljuje pri koraku 2 s ponovnim vnosom podatkov.
- **6a.** Uporabnik še nima albumov ali fotografij. Sistem prikaže prazno zbirko in možnost ustvarjanja albuma.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Prijave ni mogoče dokončati; sistem prikaže napako in omogoči ponovni poskus.

## PU-03 Razvrščanje albumov po kategorijah

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Razvrščanje albumov po kategorijah |
| **ID** | PU-03 |
| **Cilj** | Uporabnik obstoječemu albumu dodeli ali spremeni kategorijo. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima v okviru PU-04 odprt obrazec za urejanje svojega albuma. Njegove kategorije so na voljo za izbiro. |
| **Stanje sistema po PU** | Album je shranjen z izbrano kategorijo ali brez nje. |

### Scenarij

1. Sistem v obrazcu prikaže trenutno kategorijo albuma in seznam razpoložljivih kategorij.
2. Uporabnik v polju »Kategorija albuma« izbere obstoječo kategorijo, na primer »Potovanja«.
3. Uporabnik izbere »Shrani album«.
4. Sistem preveri podatke in shrani izbrano kategorijo pri albumu.
5. Sistem zapre obrazec in osveži seznam albumov. Album je viden, če ustreza trenutno izbranemu filtru kategorije.

### Alternativni tokovi

- **2a.** Uporabnik izbere »Brez kategorije«. Scenarij se nadaljuje pri koraku 3; ob shranjevanju sistem odstrani povezavo albuma s kategorijo.
- **2b.** Uporabnik izbere »Drugo«. Nadaljuje s primerom uporabe [PU-10 Dodajanje lastne kategorije](#pu-10-dodajanje-lastne-kategorije), ki vključuje shranjevanje albuma. Nato se vrne na seznam albumov in ta scenarij je zaključen.
- **3a.** Uporabnik zapre obrazec brez shranjevanja. Kategorija albuma ostane nespremenjena in scenarij se zaključi.
- **4a.** Podatki albuma niso veljavni, na primer manjka naslov. Sistem zavrne shranjevanje in opozori na napako. Uporabnik popravi podatke ter nadaljuje pri koraku 3.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Sistem prikaže napako; nalaganja kategorij ali shranjevanja albuma ni mogoče dokončati.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo, preden lahko uporabnik nadaljuje z urejanjem zbirke.
- Izbrana kategorija ne obstaja več ali ne pripada prijavljenemu uporabniku. Sistem zavrne shranjevanje in prikaže obvestilo, da kategorija ni na voljo.

## PU-04 Upravljanje lastnih albumov

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Upravljanje lastnih albumov |
| **ID** | PU-04 |
| **Cilj** | Uporabnik ustvari album za organizacijo fotografij ali spremeni oziroma odstrani obstoječi album. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprto domačo stran. |
| **Stanje sistema po PU** | Ustvarjen ali posodobljen album je shranjen v zasebni zbirki; izbrisani album in njegove fotografije niso več na voljo. |

### Scenarij

1. Uporabnik izbere »Ustvari album«.
2. Sistem prikaže obrazec za nov album.
3. Uporabnik vnese naslov in po želji opis ter izbere kategorijo. Album lahko pusti brez kategorije.
4. Uporabnik izbere »Shrani album«.
5. Sistem preveri podatke in shrani album v uporabnikovo zasebno zbirko.
6. Sistem zapre obrazec in osveži seznam albumov glede na trenutni filter.

### Alternativni tokovi

- **1a.** Uporabnik izbere urejanje svojega albuma. Sistem prikaže njegove trenutne podatke. Uporabnik jih spremeni in nadaljuje pri koraku 4. Dodelitev ali sprememba kategorije je opisana v [PU-03](#pu-03-razvrščanje-albumov-po-kategorijah).
- **1b.** Uporabnik izbere brisanje albuma ali »Izbriši vse«. Sistem zahteva potrditev in opozori, da dejanja ni mogoče razveljaviti. Ob potrditvi odstrani izbrani album oziroma vse uporabnikove albume skupaj z njihovimi fotografijami ter osveži seznam. Scenarij se zaključi.
- **1c.** Uporabnik prekliče potrditev brisanja. Zbirka ostane nespremenjena in scenarij se zaključi.
- **1d.** Uporabnik odpre svoj album. Sistem prikaže naslov, opis, kategorijo, datum ustvarjanja in galerijo fotografij. Pregled fotografij se nadaljuje v PU-07.
- **3a.** Uporabnik izbere »Drugo«. Nadaljuje s [PU-10](#pu-10-dodajanje-lastne-kategorije), ki vključuje shranjevanje albuma, nato se ta scenarij zaključi.
- **4a.** Uporabnik zapre obrazec brez shranjevanja. Spremembe se ne shranijo in scenarij se zaključi.
- **5a.** Naslov manjka, vsebuje samo presledke ali presega 255 znakov. Sistem zavrne shranjevanje. Scenarij se nadaljuje pri koraku 3 s popravkom podatkov.

### Izjeme

- Nalaganje, shranjevanje ali brisanje ne uspe. Sistem prikaže napako. Pri prekinitvi množičnega brisanja so lahko nekateri albumi ali fotografije že odstranjeni; preostali so vidni ob ponovnem nalaganju zbirke.
- Album ni na voljo ali ne pripada prijavljenemu uporabniku. Sistem zavrne dostop.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-05 Nalaganje fotografij

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Nalaganje fotografij |
| **ID** | PU-05 |
| **Cilj** | Uporabnik doda eno ali več fotografij v svoj album. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprt svoj album. |
| **Stanje sistema po PU** | Uspešno naložene fotografije so shranjene v albumu in prikazane v galeriji. |

### Scenarij

1. Uporabnik izbere »Dodaj fotografije«.
2. Sistem prikaže okno za izbiro fotografij.
3. Uporabnik izbere eno ali več fotografij JPG ali PNG, vsako do 10 MB, skupaj do 100 MB na zahtevo.
4. Sistem prikaže predogled izbranih fotografij. Preverjanje ustreznosti je opisano v [PU-06](#pu-06-preverjanje-ustreznosti-fotografije).
5. Uporabnik potrdi nalaganje.
6. Sistem shrani fotografije in prebere datum nastanka, kadar je ta na voljo v datoteki.
7. Sistem zapre okno in osveži galerijo albuma.

### Alternativni tokovi

- **3a.** Datoteka ni dovoljenega tipa ali presega 10 MB. Sistem jo izloči iz izbora in prikaže opozorilo. Uporabnik nadaljuje pri koraku 3 ali naloži preostale veljavne fotografije pri koraku 5.
- **4a.** Uporabnik odstrani fotografijo iz izbora. Sistem posodobi predogled; scenarij se nadaljuje pri koraku 5, če izbor ni prazen.
- **5a.** Nobena fotografija ni izbrana. Nalaganja ni mogoče potrditi; scenarij se nadaljuje pri koraku 3.
- **5b.** Uporabnik prekliče nalaganje. Okno se zapre brez nalaganja in scenarij se zaključi.
- **6a.** Datum nastanka ni na voljo. Sistem shrani fotografijo brez tega podatka in nadaljuje pri koraku 7.

### Izjeme

- Zahteva presega skupno omejitev velikosti ali vsebuje prazno datoteko. Sistem zavrne nalaganje in prikaže napako. Uporabnik popravi izbor ter ponovno poskusi.
- Nalaganje ali shranjevanje ne uspe. Sistem prikaže napako in ohrani okno za nalaganje odprto. Dejansko shranjene fotografije so vidne ob ponovnem odprtju albuma.
- Album ni na voljo ali seja poteče. Sistem zavrne nalaganje oziroma zahteva ponovno prijavo.

## PU-06 Preverjanje ustreznosti fotografije

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Preverjanje ustreznosti fotografije |
| **ID** | PU-06 |
| **Cilj** | Uporabnik v album doda fotografije dovoljenega tipa in velikosti. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in je v okviru PU-05 izbral datoteke za nalaganje v svoj album. |
| **Stanje sistema po PU** | Neustrezne datoteke so zavrnjene, ustrezne lahko nadaljujejo nalaganje. |

### Scenarij

1. Uporabnik izbere datoteke za nalaganje.
2. Sistem preveri, da so fotografije JPG ali PNG in da posamezna ne presega 10 MB.
3. Uporabnik potrdi nalaganje.
4. Sistem preveri omejitev 100 MB za celotno zahtevo, dovoljeni tip in nepraznost datotek.
5. Ob uspešnem preverjanju se nadaljuje shranjevanje v PU-05 pri koraku 6.

### Alternativni tokovi

- **2a.** Datoteka ni dovoljenega tipa ali presega 10 MB. Sistem je ne doda v izbor in prikaže opozorilo. Uporabnik nadaljuje pri koraku 1 ali pri koraku 3 s preostalimi ustreznimi datotekami.
- **3a.** Izbor je prazen. Sistem ne omogoči potrditve nalaganja; scenarij se nadaljuje pri koraku 1.
- **4a.** Zahteva presega 100 MB ali vsebuje prazno datoteko oziroma nedovoljen tip. Sistem zavrne nalaganje in prikaže napako. Uporabnik popravi izbor ter nadaljuje pri koraku 1.

### Izjeme

- Preverjanja ni mogoče dokončati zaradi napake pri komunikaciji. Sistem prikaže napako in nalaganja ne potrdi kot uspešnega.
- Album ni na voljo ali seja poteče. Sistem zavrne nalaganje oziroma zahteva ponovno prijavo.

## PU-07 Pregled fotografij

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Pregled fotografij |
| **ID** | PU-07 |
| **Cilj** | Uporabnik pregleduje fotografije in podatke o izbrani fotografiji. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen. |
| **Stanje sistema po PU** | Prikazana je galerija oziroma podrobnosti izbrane fotografije; zbirka ostane nespremenjena. |

### Scenarij

1. Uporabnik odpre domačo stran ali svoj album.
2. Sistem prikaže galerijo njegovih fotografij oziroma fotografij izbranega albuma. Na domači strani lahko uporabnik omeji prikaz na priljubljene fotografije (PU-08) ali izbrano obdobje nastanka (PU-12).
3. Uporabnik izbere fotografijo.
4. Sistem prikaže naslov, opis, izvirno ime datoteke, pripadajoči album, datum nastanka in datum nalaganja. Če naslov manjka, se namesto njega prikaže izvirno ime datoteke; pri manjkajočem opisu se prikaže »Ni opisa«. Urejanje naslova in opisa je opisano v [PU-14](#pu-14-dodajanje-in-urejanje-naslova-ter-opisa-fotografije).
5. Uporabnik zapre podrobnosti in se vrne v galerijo.

### Alternativni tokovi

- **2a.** V zbirki oziroma albumu ni fotografij. Sistem prikaže obvestilo o prazni galeriji in scenarij se zaključi.
- **4a.** Datum nastanka ni znan. Sistem pri tem podatku prikaže »Ni podatka«; scenarij se nadaljuje pri koraku 5.

### Izjeme

- Nalaganje galerije ali podrobnosti ne uspe. Sistem prikaže napako.
- Fotografija ni na voljo ali ne pripada prijavljenemu uporabniku. Sistem zavrne dostop do njenih podatkov in slikovne datoteke.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-08 Označevanje fotografij kot priljubljenih

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Označevanje fotografij kot priljubljenih |
| **ID** | PU-08 |
| **Cilj** | Uporabnik označi fotografije, ki jih želi pozneje najti v prikazu priljubljenih. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima v galeriji vsaj eno fotografijo. |
| **Stanje sistema po PU** | Oznaka priljubljenosti je shranjena; ob vključenem filtru so prikazane samo priljubljene fotografije, ki ustrezajo tudi morebitnemu datumskemu filtru. |

### Scenarij

1. Uporabnik pri fotografiji izbere prazno zvezdico.
2. Sistem shrani oznako in prikaže zapolnjeno zvezdico.
3. Uporabnik na domači strani vključi »Samo priljubljene fotografije«.
4. Sistem prikaže samo njegove priljubljene fotografije ob upoštevanju trenutnega datumskega filtra.

### Alternativni tokovi

- **1a.** Uporabnik izbere zapolnjeno zvezdico. Sistem odstrani oznako. Če je vključen prikaz samo priljubljenih, odstrani fotografijo iz prikaza. Fotografija ostane v albumu in scenarij se zaključi.
- **3a.** Uporabnik izključi prikaz samo priljubljenih. Sistem znova prikaže tudi ostale fotografije ob upoštevanju datumskega filtra in scenarij se zaključi.
- **4a.** Nobena fotografija ne ustreza izbranim filtrom. Sistem prikaže obvestilo o praznem rezultatu in scenarij se zaključi.

### Izjeme

- Shranjevanje oznake ne uspe. Sistem prikaže napako in prikazane oznake ne spremeni.
- Nalaganje priljubljenih ne uspe. Sistem prikaže napako.
- Fotografija ni na voljo ali seja poteče. Sistem zavrne spremembo oziroma zahteva ponovno prijavo.

## PU-09 Brisanje fotografij

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Brisanje fotografij |
| **ID** | PU-09 |
| **Cilj** | Uporabnik iz albuma trajno odstrani neželene fotografije. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprt svoj album z vsaj eno fotografijo. |
| **Stanje sistema po PU** | Uspešno izbrisane fotografije niso več v albumu ali zbirki priljubljenih in jih ni mogoče več odpreti. Album ostane shranjen. |

### Scenarij

1. Uporabnik pri fotografiji izbere brisanje.
2. Sistem zahteva potrditev in opozori, da dejanja ni mogoče razveljaviti.
3. Uporabnik potrdi brisanje.
4. Sistem odstrani fotografijo in osveži galerijo albuma.

### Alternativni tokovi

- **1a.** Uporabnik izbere »Izbriši vse« v albumu. Sistem zahteva potrditev brisanja vseh fotografij albuma. Scenarij se nadaljuje pri koraku 3 in nato odstrani vse fotografije tega albuma.
- **3a.** Uporabnik izbere »Prekliči«. Sistem zapre potrditev brez brisanja in scenarij se zaključi.
- **4a.** Po brisanju album nima več fotografij. Sistem prikaže obvestilo o praznem albumu in scenarij se zaključi.

### Izjeme

- Brisanje ne uspe. Sistem prikaže napako. Pri prekinitvi množičnega brisanja so lahko nekatere fotografije že odstranjene; sistem poskusi osvežiti galerijo s preostalimi fotografijami.
- Fotografija ali album ni na voljo oziroma ne pripada uporabniku. Sistem zavrne brisanje.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-10 Dodajanje lastne kategorije

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Dodajanje lastne kategorije |
| **ID** | PU-10 |
| **Cilj** | Uporabnik album razvrsti v kategorijo z lastnim imenom in jo lahko uporabi tudi pri drugih svojih albumih. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprt obrazec za ustvarjanje ali urejanje svojega albuma. |
| **Stanje sistema po PU** | Album je shranjen z lastno kategorijo, ki je na voljo tudi pri drugih uporabnikovih albumih. |

### Scenarij

1. Uporabnik v polju »Kategorija albuma« izbere »Drugo«.
2. Sistem prikaže polje »Vpiši svojo kategorijo«.
3. Uporabnik vnese ime kategorije z največ 100 znaki.
4. Uporabnik izbere »Shrani album«.
5. Sistem preveri podatke albuma in ime kategorije, shrani novo kategorijo ter jo dodeli albumu.
6. Sistem zapre obrazec in osveži seznam albumov. Kategorija je na voljo v seznamu kategorij.

### Alternativni tokovi

- **3a.** Uporabnik namesto »Drugo« izbere obstoječo kategorijo ali »Brez kategorije«. Nove kategorije ne ustvari; nadaljuje s shranjevanjem albuma v PU-03 oziroma PU-04.
- **4a.** Uporabnik zapre obrazec brez shranjevanja. Nova kategorija in spremembe albuma se ne shranijo; scenarij se zaključi.
- **5a.** Ime je prazno, vsebuje samo presledke, presega 100 znakov ali je enako »Drugo« ne glede na velikost črk. Sistem ne dovoli shranjevanja in opozori na napako. Scenarij se nadaljuje pri koraku 3 s popravkom imena.
- **5b.** Uporabnik že ima kategorijo z enakim imenom ne glede na velikost črk. Sistem uporabi obstoječo kategorijo in ne ustvari podvojene. Scenarij se nadaljuje pri koraku 6.
- **5c.** Podatki albuma niso veljavni. Sistem zavrne shranjevanje in opozori na napako. Uporabnik popravi podatke albuma in nadaljuje pri koraku 4.

### Izjeme

- Shranjevanje ne uspe. Sistem prikaže napako in omogoči ponovni poskus.
- Album ali izbrana možnost kategorije ni na voljo oziroma ne pripada uporabniku. Sistem zavrne shranjevanje.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-11 Filtriranje albumov po kategoriji

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Filtriranje albumov po kategoriji |
| **ID** | PU-11 |
| **Cilj** | Uporabnik prikaže samo albume izbrane kategorije. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima naložen seznam svojih albumov ter kategorij. |
| **Stanje sistema po PU** | Prikaz albumov ustreza izbranemu filtru; shranjeni albumi in njihove kategorije ostanejo nespremenjeni. |

### Scenarij

1. Uporabnik v polju »Prikaži kategorijo« izbere kategorijo.
2. Sistem prikaže samo njegove albume izbrane kategorije.

### Alternativni tokovi

- **1a.** Uporabnik izbere »Vse kategorije«. Sistem prikaže vse njegove albume in scenarij se zaključi.
- **1b.** Uporabnik izbere »Brez kategorije«. Sistem prikaže samo njegove albume brez kategorije in scenarij se zaključi.
- **2a.** Izbrani kategoriji ne ustreza noben album. Sistem prikaže obvestilo »V izbrani kategoriji ni albumov« in scenarij se zaključi.
- **2b.** Uporabnikova zbirka je prazna. Sistem prikaže obvestilo, da še nima albumov, in možnost ustvarjanja albuma. Scenarij se zaključi.

### Izjeme

- Začetno nalaganje albumov ali kategorij ne uspe. Sistem prikaže napako; uporabnik lahko po odpravi težave ponovno naloži stran.

## PU-12 Filtriranje fotografij po datumu nastanka

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Filtriranje fotografij po datumu nastanka |
| **ID** | PU-12 |
| **Cilj** | Uporabnik poišče fotografije, posnete v izbranem obdobju. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprto domačo stran z razdelkom fotografij. |
| **Stanje sistema po PU** | Prikazane so fotografije z znanim datumom nastanka v izbranem obdobju, ob upoštevanju morebitnega filtra priljubljenih. |

### Scenarij

1. Uporabnik vnese začetni in končni datum nastanka.
2. Uporabnik izbere »Filtriraj«.
3. Sistem preveri, da sta izpolnjena oba datuma in da začetni ni za končnim.
4. Sistem prikaže fotografije iz izbranega obdobja, vključno z začetnim in končnim dnem. Fotografij brez znanega datuma nastanka ne prikaže.

### Alternativni tokovi

- **3a.** Datum manjka ali je začetni datum za končnim. Sistem opozori na napako in ne uporabi neveljavnega filtra. Scenarij se nadaljuje pri koraku 1.
- **4a.** Nobena fotografija ne ustreza filtrom. Sistem prikaže obvestilo o praznem rezultatu in scenarij se zaključi.
- **4b.** Uporabnik izbere »Počisti filter«. Sistem počisti datumski filter in filter priljubljenih ter prikaže vse njegove fotografije. Scenarij se zaključi.

### Izjeme

- Nalaganje rezultatov ne uspe. Sistem prikaže napako.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-13 Odjava uporabnika

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Odjava uporabnika |
| **ID** | PU-13 |
| **Cilj** | Uporabnik zaključi dostop do svoje zasebne zbirke v trenutni prijavni seji. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen. |
| **Stanje sistema po PU** | Uporabnik ni več prijavljen. Prikazana je prijavna stran, dostop do zasebne zbirke zahteva novo prijavo. |

### Scenarij

1. Uporabnik izbere »Odjava«.
2. Sistem zaključi prijavno sejo in odstrani prikaz uporabnikove zbirke.
3. Sistem prikaže prijavno stran.

### Alternativni tokovi

- **2a.** Seja je medtem že potekla. Sistem uporabnika obravnava kot neprijavljenega in nadaljuje pri koraku 3.

### Izjeme

- Zahteve za odjavo ni mogoče dokončati. Sistem prikaže napako in omogoči ponovni poskus. Odjava ni potrjena.

## PU-14 Dodajanje in urejanje naslova ter opisa fotografije

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Dodajanje in urejanje naslova ter opisa fotografije |
| **ID** | PU-14 |
| **Stanje izvedbe** | Implementirano. |
| **Cilj** | Uporabnik fotografiji doda ali spremeni naslov in opis, da si zabeleži njeno vsebino ter jo pozneje lažje najde. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprte podrobnosti svoje že naložene fotografije v PU-07. |
| **Stanje sistema po PU** | Naslov in opis sta shranjena ter prikazana pri fotografiji. Slikovna datoteka, njeno izvirno ime in pripadajoči album ostanejo nespremenjeni. |

### Scenarij

1. Uporabnik v podrobnostih fotografije izbere »Uredi podatke«.
2. Sistem prikaže polji za naslov in opis z obstoječima vrednostma. Pri fotografiji brez teh podatkov sta polji prazni.
3. Uporabnik vnese ali spremeni naslov z največ 100 znaki in opis z največ 2000 znaki. Obe polji sta neobvezni.
4. Uporabnik izbere »Shrani«.
5. Sistem odstrani presledke na začetku in koncu obeh vrednosti ter preveri njuno dolžino.
6. Sistem shrani podatke in prikaže posodobljene podrobnosti fotografije. Shranjene spremembe so vidne tudi po ponovnem odprtju fotografije.

### Alternativni tokovi

- **3a.** Uporabnik izpolni samo eno polje. Drugo ostane prazno; scenarij se nadaljuje pri koraku 4.
- **3b.** Uporabnik izbriše obstoječi naslov ali opis oziroma vnese samo presledke. Ob shranjevanju se ustrezni podatek odstrani. Scenarij se nadaljuje pri koraku 4; pri fotografiji brez naslova se za prikaz uporabi izvirno ime datoteke.
- **4a.** Uporabnik izbere »Prekliči«. Sistem zapre urejanje brez shranjevanja in prikaže prejšnje podatke. Scenarij se zaključi.
- **5a.** Naslov presega 100 znakov ali opis presega 2000 znakov. Sistem ne shrani nobene spremembe, označi predolgo polje in sporoči dovoljeno dolžino. Vneseni podatki ostanejo v obrazcu; scenarij se nadaljuje pri koraku 3.

### Izjeme

- Shranjevanje ne uspe. Sistem prikaže napako in ohrani vnesene podatke v obrazcu za ponovni poskus. Uspešnega shranjevanja ne potrdi.
- Fotografija ne obstaja več ali ne pripada prijavljenemu uporabniku. Sistem zavrne dostop in ne dovoli spremembe njenih podatkov.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo.

## PU-15 Iskanje fotografij po naslovu in opisu

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Iskanje fotografij po naslovu in opisu |
| **ID** | PU-15 |
| **Stanje izvedbe** | Načrtovano. |
| **Cilj** | Uporabnik poišče fotografije po besedilu v naslovu ali opisu, da najde želene posnetke brez pregledovanja celotne zbirke. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima na domači strani odprt pregled svojih fotografij v PU-07. |
| **Stanje sistema po PU** | Prikazane so uporabnikove fotografije, ki ustrezajo iskalnemu nizu in morebitnima filtroma datuma ter priljubljenih. Podatki fotografij ostanejo nespremenjeni. |

### Scenarij

1. Uporabnik v iskalno polje vnese besedilo z največ 100 znaki.
2. Uporabnik izbere »Išči«.
3. Sistem odstrani presledke na začetku in koncu iskalnega niza ter preveri njegovo dolžino.
4. Sistem v uporabnikovi zasebni zbirki poišče fotografije, katerih naslov ali opis vsebuje celoten vneseni niz, ne glede na velikost črk. Fotografije brez naslova se lahko ujemajo po opisu in obratno.
5. Sistem prikaže rezultate in njihovo število. Če sta vključena datumski filter ali prikaz samo priljubljenih, rezultati ustrezajo tudi tema filtroma. Fotografija, ki se ujema v obeh poljih, je prikazana samo enkrat.
6. Uporabnik izbere rezultat in nadaljuje pregled podrobnosti v PU-07.

### Alternativni tokovi

- **1a.** Uporabnik spremeni iskalni niz. Scenarij se nadaljuje pri koraku 2; sistem ob uspešnem iskanju prejšnje rezultate nadomesti z novimi.
- **3a.** Iskalni niz je prazen ali vsebuje samo presledke. Sistem odstrani besedilno omejitev in prikaže uporabnikove fotografije ob upoštevanju trenutnih filtrov datuma ter priljubljenih. Scenarij se zaključi.
- **3b.** Iskalni niz presega 100 znakov. Sistem opozori na dovoljeno dolžino in ne uporabi neveljavnega iskanja. Scenarij se nadaljuje pri koraku 1.
- **5a.** Nobena fotografija ne ustreza iskalnemu nizu in trenutnim filtrom. Sistem prikaže »Ni fotografij, ki ustrezajo iskanju« in število rezultatov 0. Uporabnik lahko nadaljuje pri koraku 1 z drugim nizom.
- **5b.** Uporabnik izbere »Počisti iskanje«. Sistem izprazni iskalno polje in odstrani besedilno omejitev. Datumski filter in filter priljubljenih ostaneta nespremenjena; scenarij se zaključi.
- **5c.** Fotografija nima naslova in opisa. Pri nepraznem iskalnem nizu ni vključena med rezultate. Izvirno ime datoteke se pri iskanju ne upošteva.

### Izjeme

- Iskanje ne uspe. Sistem prikaže napako in ohrani iskalni niz za ponovni poskus. Napake ne prikaže kot iskanje brez zadetkov.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo; fotografije drugih uporabnikov niso dostopne prek iskanja.
