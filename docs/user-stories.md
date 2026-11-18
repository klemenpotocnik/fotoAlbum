# Uporabniška zgodba: naslov in opis fotografije

Dokument opisuje dodajanje, urejanje in odstranjevanje neobveznega naslova
ter opisa fotografije. Povezan je s primerom uporabe
[PU-14](primeri-uporabe.md#pu-14-dodajanje-in-urejanje-naslova-ter-opisa-fotografije).

## US-01: Dodajanje in urejanje naslova ter opisa fotografije

**Odgovorni član:** Klemen Potočnik

**Uporabniška zgodba:** Kot prijavljen uporabnik želim svoji fotografiji
dodati ali urediti naslov in opis, da jo v zbirki prepoznam po vsebini
ter ob njej ohranim podatke o dogodku, kraju in drugih podrobnostih.

### Sprejemni kriteriji

#### AC-01-01: Dodajanje naslova in opisa

- **Given:** Uporabnik je prijavljen in ima odprte podrobnosti svoje fotografije brez naslova in opisa.
- **When:** Izbere »Uredi podatke«, vnese naslov z od 1 do 100 znaki in opis z od 1 do 2000 znaki ter izbere »Shrani«.
- **Then:** Sistem shrani oba podatka brez začetnih in končnih presledkov ter ju prikaže v podrobnostih. Naslov se prikaže tudi na kartici fotografije, prelomi vrstic znotraj opisa pa se ohranijo.

#### AC-01-02: Urejanje in ohranitev podatkov

- **Given:** Uporabnikova fotografija ima shranjena naslov »Izlet« in opis »Družinski izlet«.
- **When:** Uporabnik naslov spremeni v »Izlet v Piran« in opis v »Družinski izlet na obalo«, shrani spremembe in ponovno odpre fotografijo.
- **Then:** Sistem prikaže novi naslov in opis. Izvirno ime datoteke, slikovna vsebina in pripadajoči album ostanejo nespremenjeni.

#### AC-01-03: Neodvisno urejanje polj

- **Given:** Uporabnik ureja svojo fotografijo z veljavnima naslovom in opisom.
- **When:** Spremeni samo eno od polj in izbere »Shrani«.
- **Then:** Sistem shrani spremenjeno polje in ohrani prejšnjo vrednost drugega. Fotografija lahko ima tudi samo naslov ali samo opis.

#### AC-01-04: Predolg naslov

- **Given:** Uporabnik ureja naslov svoje fotografije in je opis veljaven.
- **When:** Vnese naslov s 101 znakom brez začetnih in končnih presledkov ter poskusi shraniti spremembe.
- **Then:** Sistem zavrne shranjevanje, označi polje naslova in prikaže omejitev 100 znakov. Vnos ostane v obrazcu, prej shranjena naslov in opis pa ostaneta nespremenjena.

#### AC-01-05: Predolg opis

- **Given:** Uporabnik ureja opis svoje fotografije in je naslov veljaven.
- **When:** Vnese opis z 2001 znakom brez začetnih in končnih presledkov ter poskusi shraniti spremembe.
- **Then:** Sistem zavrne shranjevanje, označi polje opisa in prikaže omejitev 2000 znakov. Vnos ostane v obrazcu, prej shranjena naslov in opis pa ostaneta nespremenjena.

#### AC-01-06: Odstranitev neobveznih podatkov

- **Given:** Uporabnikova fotografija ima shranjena naslov in opis.
- **When:** Uporabnik izprazni eno ali obe polji oziroma vanju vnese samo presledke in izbere »Shrani«.
- **Then:** Sistem odstrani izpraznjena podatka in ohrani morebitno nespremenjeno polje. Pri manjkajočem naslovu v podrobnostih prikaže izvirno ime datoteke, pri manjkajočem opisu pa »Ni opisa«. Odstranjena podatka sta ob ponovnem urejanju prikazana kot prazni polji.

#### AC-01-07: Preklic urejanja

- **Given:** Uporabnik ureja svojo fotografijo z že shranjenima naslovom in opisom.
- **When:** Spremeni vnos in izbere »Prekliči«.
- **Then:** Sistem prikaže prej shranjena podatka. Ko uporabnik ponovno odpre urejanje, sta v poljih prej shranjeni vrednosti.

#### AC-01-08: Urejanje tuje fotografije

- **Given:** Fotografija pripada drugemu uporabniku.
- **When:** Prijavljen uporabnik poskusi spremeniti njen naslov ali opis.
- **Then:** Sistem zavrne dostop, ne spremeni podatkov in ne razkrije podatkov fotografije.

#### AC-01-09: Napaka pri shranjevanju

- **Given:** Uporabnik ima v obrazcu vnesena veljavna naslov in opis svoje fotografije.
- **When:** Izbere »Shrani«, vendar zahteve ni mogoče dokončati zaradi prekinitve povezave.
- **Then:** Sistem prikaže napako in ne potrdi uspešnega shranjevanja. Vnesena podatka ostaneta v obrazcu, uporabnik pa lahko ponovi oddajo.

### Podrobnosti implementacije

- Model `Photo` ima neobvezni polji `title` in `description`, v PostgreSQL shranjeni kot `varchar(100)` in `varchar(2000)`. Migracija `backend/database/005_photo_details.sql` ju doda obstoječi tabeli brez brisanja fotografij.
- `PUT /api/photos/{id}` sprejme JSON z obema poljema `title` in `description`. Ob urejanju samo enega polja obrazec pošlje tudi obstoječo vrednost drugega, zato se ta ohrani. Odgovor vsebuje posodobljeni `PhotoResponse`; obe polji vrnejo tudi zahteve za pregled fotografije in galerij.
- `PhotoDetailsRequest` odstrani začetne in končne presledke. Prazne vrednosti in vrednosti s samimi presledki pretvori v `null`; notranji prelomi vrstic v opisu ostanejo.
- `PhotoService.updateDetails` poišče fotografijo po ID-ju in lastniku trenutne seje. Pred spreminjanjem preveri dolžini obeh polj; neveljaven vnos vrne `400`, tuja ali neobstoječa fotografija pa `404`. Obe vrednosti se shranita v isti transakciji.
- Dostop zahteva prijavo in veljaven žeton CSRF. Slikovna datoteka, pripadajoči album, datuma in priljubljenost se pri urejanju ohranijo.
- `PhotoDetailsForm` vsebuje polje naslova, večvrstično polje opisa in opozorili o dolžini. `PhotoDetailsModal` omogoča shranjevanje in preklic; ob napaki ohrani vnos ter prikaže sporočilo. Med shranjevanjem so dejanja onemogočena.
- Podrobnosti prikazujejo naslov in opis z ohranjenimi prelomi vrstic. Pri manjkajočem naslovu se uporabi izvirno ime datoteke, pri manjkajočem opisu pa »Ni opisa«.
- `PhotoCard` prikaže shranjen naslov na fotografiji ter ga uporabi v opisnih oznakah. Galeriji na domači strani in v albumu ob uspešnem shranjevanju posodobita podatke fotografije.
- `PhotoDetailsTest` preverja normalizacijo, dolžine, prazne vrednosti, večvrstično besedilo, lastništvo, ohranitev drugih podatkov ter odgovore za shranjevanje in ponovno branje.

### Naloge za sub-issues

#### US-01-T1: Shranjevanje in preverjanje naslova ter opisa fotografije

**Odgovorni član:** Klemen Potočnik

**Opis:** Dodati polji naslova in opisa v model, SQL migracijo ter odgovor
s podatki fotografije. Izdelati zahtevo za urejanje, normalizirati vrednosti,
preveriti omejitvi 100 in 2000 znakov ter lastništvo fotografije. Omogočiti
odstranitev neobveznih podatkov in ohraniti ostale podatke fotografije.
Dodati teste za veljaven vnos, mejne dolžine, predolg in prazen vnos ter
zavrnjen dostop.

#### US-01-T2: Obrazec in prikaz naslova ter opisa fotografije

**Odgovorni član:** Klemen Potočnik

**Opis:** V podrobnostih omogočiti vnos in urejanje naslova ter večvrstičnega
opisa, shranjevanje, preklic in prikaz napak. Ob neuspešni zahtevi ohraniti
vnos za ponovni poskus. Posodobiti podatke v obeh galerijah in prikaz
naslova na kartici fotografije. Pri manjkajočih podatkih prikazati izvirno
ime datoteke oziroma »Ni opisa«. Preveriti kriterije AC-01-01–AC-01-09.

## Definition of Ready

Zgodba je pripravljena za implementacijo, ko so izpolnjeni vsi pogoji:

- Uporabniška zgodba ima določenega odgovornega člana ter jasno vlogo, cilj in korist.
- Sprejemni kriteriji so preverljivi in vključujejo uspešen ter neuspešen scenarij.
- Namen in kontekst zgodbe sta jasna odgovornemu članu in ekipi.
- Podrobnosti implementacije in naloge so zapisane.

## Preverjanje implementacije

| Preverjanje | Rezultat zadnjega preverjanja |
| --- | --- |
| Avtomatizirani testi `PhotoDetailsTest` | 7 uspešnih testov: normalizacija, dolžine, prazne vrednosti, lastništvo, ohranitev podatkov ter shranjevanje in ponovno branje prek API. |
| Preizkus API z ločeno PostgreSQL bazo | Potrjeni so trajno shranjevanje, odstranjevanje praznih vrednosti, ohranitev slike in drugih podatkov ter zavrnitve nepooblaščenih zahtev. |
| Preverjanje frontenda | `npm run lint` in `npm run build` sta uspešna. |
| Ročni pregled vseh sprejemnih kriterijev v brskalniku | Še ni potrjen; dosedanje preverjanje ne dokazuje delovanja preklica in vseh stanj uporabniškega vmesnika. |
