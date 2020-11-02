![Java CI with Maven](https://github.com/kristiania/pgr203eksamen-eirik124/workflows/Java%20CI%20with%20Maven/badge.svg)
# pgr203eksamen-eirik124

#### Gruppemedlemmer:
Pål Anders Byenstuen, Eirik Lundanes og Vibeke Opgård.

-----------------------------------------------------------

## Beskrivelse av prosjekt

Til eksamen i PGR203 Avansert Java har vi laget en webapplikasjon som kan lagre og liste ut prosjektmedlemmer, og opprette prosjekter og prosjektdeltakere fra databasen. Det er mulig å legge til et "member" med fornavn, etternavn og e-postadresse og se den på websiden. Oppgaven er utviklet med test-drevet parprogrammering.

Programmet kan brukes ved å besøke localhost:8080 i nettleseren. Her kan du velge member, project, task, og endre status på oppgaver.


### Github Actions
Link til github actions
[Github Actions](https://github.com/kristiania/pgr203eksamen-eirik124/actions)

-----------------------------
### UML Modeller

#### Database struktur

![](docs/database_structure.png)

#### Server struktur

![](docs/server_structure.png)

-----------------------------
### Hvordan det bygges
Baseres på programmet IntelliJ fremgangsmåte kan variere fra program til program

Man bygger .jar-filen ved å velge View -> Tool Windows -> Maven. Da åpnes det et Maven-vindu under ```Lifecycle```. Man velger så ```Package``` og deretter kjører den pakking og bygging på et par sekunder.

-----------------------------
### Hvordan det kjøres

Vær sikker på at du har en PostgreSQL-database satt opp som er mulig å koble seg til. Deretter lager du en ```pgr203.properties``` som skal inneholde følgende
```
dataSource.url=jdbc:postgresql://server:port/databasename   //urlen til serveren
dataSource.username=username   //brukernavnet som har access til SQL serveren
dataSource.password=passord //her setter du et passord  som er sikkert som ingen vet
```

Man kjører serveren med .jar-filen ved å kjøre ```java -jar target/http-server.jar``` eller det man renamer filen til etter build. 
Default port er 8080. Nettsiden kjøres default på localhost:8080 eller den porten som blir satt av bruker.

## Eksamen sjekklist

### Sjekkliste for innlevering
- [ ] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
- [X] Koden er sjekket inn på github.com/kristiania-repository
- [ ] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)
README.md
- [X] README.md inneholder en korrekt link til Github Actions
- [X] README.md beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
- [ ] README.md beskriver eventuell ekstra leveranse utover minimum
- [ ] README.md inneholder et diagram som viser datamodellen
- [ ] Dere har gitt minst 2 positive og 2 korrektive GitHub issues til en annen gruppe og inkluderer link til disse fra README.md
Koden
- [X] mvn package bygger en executable jar-fil
- [X] Koden inneholder et godt sett med tester
- [X] java -jar target/...jar (etter mvn package ) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
- [X] Programmet leser dataSource.url , dataSource.username og dataSource.password fra pgr203.properties for å connecte til databasen
- [X] Programmet bruker Flywaydb for å sette opp databaseskjema
- [X] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart
Funksjonalitet
- [X] Programmet kan liste prosjektdeltagere fra databasen
- [X] Programmet lar bruker opprette nye prosjektdeltagere i databasen
- [X] Programmet kan opprette og liste prosjektoppgaver fra databasen
- [ ] Programmet lar bruker tildele prosjektdeltagere til oppgaver
- [ ] Flere prosjektdeltagere kan tildeles til samme oppgave
- [ ] Programmet lar bruker endre status på en oppgave

### Ekstra poeng sjekklist
- [ ] Håndtering av request target "/"
- [ ] Avansert datamodell (mer enn 3 tabeller)
- [ ] Avansert funksjonalitet (redigering av prosjektmedlemmer, statuskategorier, prosjekter)
- [ ] Implementasjon av cookies for å konstruere sesjoner: https://tools.ietf.org/html/rfc6265#section-3
- [X] UML diagram som dokumenterer datamodell og/eller arkitektur (presentert i README.md)
- [ ] Rammeverk rundt Http-håndtering (en god HttpMessage class med HttpRequest og HttpResponse subtyper) som gjenspeiler RFC7230
- [ ] God bruk av DAO-pattern
- [ ] God bruk av Controller-pattern
- [X] Korrekt håndtering av norske tegn i HTTP
- [ ] Link til video med god demonstrasjon av ping-pong programmering
- [ ] Automatisk rapportering av testdekning i Github Actions
- [ ] Implementasjon av Chunked Transfer Encoding: https://tools.ietf.org/html/rfc7230#section-4.1
- [ ] Annet
