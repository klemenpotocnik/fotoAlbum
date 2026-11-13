# Primeri uporabe projekta fotoAlbum



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
6. Sistem ustvari račun in varno shrani geslo v obliki zgoščene vrednosti.
7. Sistem prikaže prijavno stran z obvestilom o uspešno ustvarjenem računu.

### Alternativni tokovi

- **3a.** Obiskovalec izbere »Prijavi se«. Sistem odpre prijavno stran, račun se ne ustvari.
- **5a.** Obvezna polja so prazna ali e-pošta ni veljavna. Sistem opozori na napako in omogoči popravek podatkov.
- **5b.** Geslo vsebuje manj kot 8 znakov. Sistem opozori na zahtevano dolžino.
- **5c.** Gesli se ne ujemata. Sistem prikaže opozorilo in omogoči ponovni vnos.
- **5d.** E-poštni naslov je že uporabljen. Sistem obvesti obiskovalca in omogoči vnos drugega naslova.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Registracije ni mogoče dokončati; sistem prikaže napako in omogoči ponovni poskus.

## PU-02 Prijava uporabnika

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Prijava uporabnika |
| **ID** | PU-02 |
| **Cilj** | Uporabnik se prijavi in pridobi dostop do svojih albumov ter fotografij. |
| **Akterji** | Registriran uporabnik. |
| **Predpogoji** | Uporabnik ima ustvarjen račun, ni prijavljen in ima odprto prijavno stran. |
| **Stanje sistema po PU** | Uporabnik je prijavljen. Vzpostavljena je seja in prikazana njegova zasebna zbirka. |

### Scenarij

1. Sistem prikaže prijavni obrazec.
2. Uporabnik vnese e-poštni naslov in geslo.
3. Uporabnik izbere »Prijava«.
4. Sistem preveri prijavne podatke.
5. Sistem vzpostavi uporabniško sejo.
6. Sistem odpre domačo stran in prikaže uporabnikove albume ter fotografije.

### Alternativni tokovi

- **2a.** Uporabnik nima računa in izbere »Ustvari račun«. Nadaljuje s primerom uporabe [PU-01 Registracija uporabnika](#pu-01-registracija-uporabnika).
- **3a.** Obvezno polje je prazno ali oblika e-pošte ni veljavna. Sistem opozori uporabnika in omogoči popravek.
- **4a.** E-pošta ali geslo ni pravilno. Sistem prikaže »Napačna e-pošta ali geslo« in omogoči ponovni poskus.
- **6a.** Uporabnik še nima albumov ali fotografij. Sistem prikaže prazno zbirko in možnost ustvarjanja albuma.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Prijave ni mogoče dokončati; sistem prikaže napako in omogoči ponovni poskus.

## PU-03 Razvrščanje albumov po kategorijah

| Polje | Vsebina |
| --- | --- |
| **Primer uporabe** | Razvrščanje albumov po kategorijah |
| **ID** | PU-03 |
| **Cilj** | Uporabnik albumu dodeli kategorijo in pregleduje svoje albume po izbrani kategoriji. |
| **Akterji** | Prijavljen uporabnik. |
| **Predpogoji** | Uporabnik je prijavljen in ima odprto domačo stran. Njegove kategorije so na voljo za izbiro. |
| **Stanje sistema po PU** | Album je shranjen z izbrano kategorijo. Na domači strani so prikazani uporabnikovi albumi, ki ustrezajo izbranemu filtru kategorije. |

### Scenarij

1. Uporabnik izbere »Ustvari album«.
2. Sistem prikaže obrazec za nov album s seznamom uporabnikovih kategorij.
3. Uporabnik vnese naslov in po želji opis albuma.
4. Uporabnik v polju »Kategorija albuma« izbere obstoječo kategorijo, na primer »Potovanja«.
5. Uporabnik izbere »Shrani album«.
6. Sistem preveri podatke in shrani album, povezan z izbrano kategorijo ter prijavljenim uporabnikom.
7. Sistem na domači strani prikaže album in njegovo kategorijo, če album ustreza trenutno izbranemu filtru.
8. Uporabnik v polju »Prikaži kategorijo« izbere kategorijo shranjenega albuma.
9. Sistem prikaže samo uporabnikove albume iz izbrane kategorije.

### Alternativni tokovi

- **1a.** Uporabnik izbere urejanje obstoječega lastnega albuma. Sistem prikaže njegove trenutne podatke in kategorijo. Uporabnik spremeni kategorijo ter nadaljuje pri koraku 5.
- **4a.** Uporabnik izbere »Brez kategorije«. Sistem ob shranjevanju album pusti brez kategorije oziroma odstrani njegovo prejšnjo povezavo s kategorijo. Takšne albume lahko uporabnik prikaže s filtrom »Brez kategorije«.
- **4b.** Uporabnik izbere »Drugo«. Sistem prikaže polje »Vpiši svojo kategorijo«. Uporabnik vnese ime z največ 100 znaki in nadaljuje pri koraku 5. Ob shranjevanju sistem ustvari kategorijo za tega uporabnika ali uporabi njegovo že obstoječo kategorijo z enakim imenom ne glede na velikost črk. Kategorija je nato na voljo tudi pri drugih njegovih albumih.
- **5a.** Pri možnosti »Drugo« ime kategorije manjka ali vsebuje samo presledke. Sistem ne omogoči shranjevanja, dokler uporabnik ne vnese imena.
- **6a.** Podatki niso veljavni, na primer manjka naslov albuma ali je kot lastno ime kategorije vpisano »Drugo«. Sistem zavrne shranjevanje, prikaže napako in omogoči popravek.
- **8a.** Uporabnik izbere »Vse kategorije«. Sistem prikaže vse njegove albume, tudi albume brez kategorije.
- **9a.** V izbrani kategoriji ni nobenega albuma. Sistem prikaže obvestilo »V izbrani kategoriji ni albumov«.

### Izjeme

- Strežnik ali podatkovna baza ni dosegljiva. Sistem prikaže napako; nalaganja kategorij ali shranjevanja albuma ni mogoče dokončati.
- Prijavna seja poteče. Sistem zahteva ponovno prijavo, preden lahko uporabnik nadaljuje z urejanjem zbirke.
- Izbrana kategorija ne obstaja več ali ne pripada prijavljenemu uporabniku. Sistem zavrne shranjevanje in prikaže obvestilo, da kategorija ni na voljo.
