# Arkitektur — MVVM

Vi har benyttet oss av MVVM (Model - View - ViewModel) som arkitekturmønster.

- Modell: Dette laget er ansvarlig for abstraksjonen av datakildene. Model og ViewModel jobber sammen for å hente og lagre dataene.
- View: Hensikten med dette laget er å informere ViewModel om brukerens handling.
- ViewModel: Den avslører de datastrømmene som er relevante for visningen.

(Geeks for Geeks, 2021)

Ved å følge MVVM gjør vi koden vår mer strukturert og enklere å vedlikeholde. Vi har forsøkt å håndtere kompleksitet ved å bruke stabile API’er som ikke har endret seg gjennom prosjektet vår og gjenbruke koden vår.

For å redusere kompleksitet i kildekoden, kunne vi vært flinkere på seperation av concerns. Vi opplevde en læringskurve og koden vår hadde mer kompelkstitet tidligere i prosjektet men etterhvert ble vi flinkere. 

Vi har forsøkt å følge MVVM struktur så godt som mulig gjennom appen. Vi har brukt dependency injection, men enkelte steder kunne vi ha hatt godt av å tenke mer på strukturen. For eksempel kunne ProfileViewModel vært mer separert til:  RouteViewModel, ProfileViewModel og BoatProfileViewModel.

Noen steder kunne vi hatt enda tydeligere skille, men dette er noe vi er klar over og kunne håndtert dette gjennom prosessen ved å lagge til tagger i koden vår i GitHub, slik at vi kunne enklere komme tilbake til det.

■ Beskrivelse av hvordan viktige objektorienterte prinsipper som lav kobling og høy kohesjon samt design patterns som MVVM og UDF er ivaretatt i løsningen burde også være med.

### **Design Patterns**

### **MVVM (Model-View-ViewModel)**

MVVM design pattern er brukt for å skille logikken for brukergrensesnittet fra forretningslogikken. Dette gjør applikasjonen mer modulær og enkel å vedlikeholde.

- **Model:** Håndterer data og logikk, ofte ved bruk av Room for lokal datalagring og Ktor for nettverksanrop.
- **View:** Representerer UI-laget som viser data til brukeren og sender brukerhandlinger videre til ViewModel.
- **ViewModel:** Formidler mellom Model og View, og gir data til View via LiveData eller StateFlow.

### **UDF (Unidirectional Data Flow)**

UDF-prinsippet er implementert ved å sikre at dataflyten i applikasjonen er enveis. Dette bidrar til å redusere kompleksiteten og forbedre forutsigbarheten.

- **ViewModel → View:** Data flyter fra ViewModel til View via observerbare datastrømmer (LiveData/StateFlow).
- **User Actions → ViewModel:** Brukerhandlinger (f.eks. klikk) sendes fra View til ViewModel som kommandoer eller hendelser.

Dette prinsippet har vi tatt høyde for ved å bruke UI_state istedenfor remember der det trengs.

■ Beskriv løsningen beregnet på lesere som skal jobbe med drift, vedlikehold og videreutvikling av løsningen. Beskriv hvilke teknologier og arkitektur som brukes i løsningen. Beskriv hvilket
API-nivå (Android versjon) dere har valgt, og hvorfor.

### **Beskrivelse av løsningen for drift, vedlikehold og videreutvikling**

### **Teknologier brukt i appen**

- **Kotlin:** Programmeringsspråket vi har brukt sammen med JetpackCompose for UI.
- **Android Jetpack-biblioteker:** Inkluderer ViewModel, Room, og andre komponenter som hjelper med å følge anbefalte arkitekturprinsipper og beste praksis.
- **Room:** For lokal databasehåndtering. Det gir en abstraksjon over SQLite og sikrer en type-sikker måte å jobbe med data.
- **Ktor:** Et asynkront rammeverk for nettverkskommunikasjon, brukt til å håndtere API-anrop.
- **Dagger/Hilt:** For dependency injeksjon, som gjør koden mer modulær og testbar.
- **Coroutines:** For å håndtere asynkrone oppgaver på en enkel og strukturert måte.
- **Material Design:** Brukt for å sikre en konsistent UI-opplevelse.
- **GPS:** Fine location, sporing av brukere. Gir en mer nøyaktig lokasjon
- **Internett: Brukes for å hente data fra API’ene og hente kartet.**

Drift, vedlikehold og videreutviklig

Drift:

Velikehold

- robusthet

Videreutvikling:

- laget egen,, replassere SKippo med selvlaget
- gjøre brukere,,, profil,, rikere
- parkering-funksjon

### **Valg av API-nivå (Android versjon)**

- **compileSdkVersion:** 34
- **minSdkVersion:** 24
- **targetSdkVersion:** 34

Vi brukte API-nivå 24 da vi opprettet prosjektet fordi de fleste enheter støtter det. Da vi brukte boolean-verdier for Room databasen krevde det API-nivå 30, men dette løste vi fint ved å endre verdier til 0 og 1, noe som kun krever API-nivå 14. MapBox krever API-nivå 26, og Spash-Screen krever API-nivå 31. For å kjøre appen optimalt burde man da ha API-nivå 31. 

```kotlin
android {
    namespace = "no.uio.ifi.in2000.team7.boatbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.team7.boatbuddy"
        minSdk = 26 // 31 med splash-screen
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
```

### **Objektorienterte prinsipper**

### **Lav kobling**

Lav kobling oppnås ved at et komponent i appen har et begrenset samarbeid med andre komponenter. Dermed, fører lav kobling til at man kan enklere gjøre endring (Lindsjørn, 2022).

**Dette oppnås ved:**

- **Repository Pattern:** Bruk av repositorier for å isolere datakildene fra resten av applikasjonen.
- **Dependency-injeksjon:** Bruk av Dagger/Hilt for å injisere avhengigheter, slik at komponenter ikke trenger å opprette sine egne dependencies.

### **Høy kohesjon**

Målet med høy kohesjon er å sørge for å begrense komponenter til å kun ha et klart ansvar for et visst antall oppgaver og ikke alt for mange. (Lindsjørn, 2022)

**Dette oppnås ved:**

- **MVVM Arkitektur:** Hver komponent (Model, View, ViewModel) har et spesifikt ansvar og gjør bare det den er designet for.
- **Single Responsibility Principle:** Hver klasse/fil har ett og bare ett ansvar, noe som gjør koden mer vedlikeholdbar og testbar. Vi har prøvd å følge dette prinsippet så mye som mulig, men ved å analysere koden vår i etterkant og ting vi skulle gjerne løst annerledes, men som var vanskelig å endre på på grunn av teknisk gjeld.

**Kildeliste:**

Geeks for Geeks. *Android Architecture Patterns.* (2021)

https://www.geeksforgeeks.org/android-architecture-patterns/

 

Lindsjørn, Yngve. *Mer om objektorientering og UML.* (2022)

https://www.uio.no/studier/emner/matnat/ifi/IN1030/v22/forelesningsressurser/in1030_2022.04.05_objektorientert-modelleringv2.pdf
