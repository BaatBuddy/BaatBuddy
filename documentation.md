Dette er de biblioteken vi har brukt:

Ktor: Ktor er et bibliotek som er svært nyttig når det kommer til å bygge serverklienter og webapplikasjoner. Ktor er et stort bibliotek med mange hjelpefunksjoner som gjør det enkelt å lage robuste og skalerbare serverkilenter. Selv om ktor er et stort bibliotek er det enkelt å jobbe med. Det gir mange muligheter og stor fleksibilitet når det kommer til hvordan webapplikasjonen skal designes og implementeres. Grunnet dette er ktor er bibliotek som er brukt verden over av mange utviklere.

Coroutine: Coroutine-bibliotek er et bibliotek som er nyttig for asynkron programmering. Coroutines fungerer slik at det er mulig å skrive kodebiter som ser synkrone ut men som utføres usynkront og dermed ikke blokkerer tråden og forsinker resten av koden. Dette gjør at appen kan kjøres mer effektivt og smidig. Spesielt nettverkskall, I/O opperasjoner og databasekall forenkles med bruk av coroutine-biblioteket.

MapBox: MapBox-biblioteket er et bibliotek som tilbyr kart. Det er ett av verdens ledene selskaper innen kartteknologi. Derfor er det et svært godt bibliotek som tilbyr kart og stedtjenester. Kartdataen de baserer seg på kommer fra OpenStreetMap-data (OSM), dette oppdateres kontinuerlig noe som fører til at kartdataen er veldig presis og pålitelig. En av det mest populere funksjonene som kan anvendes er såkalt "ruting". Ruting er noe som kan brukes for å planlegge en rute mellom to eller flere punkter. Noe som vi bruker som en av våres viktige funksjoner.

Room Database:
Room databasen til Android Studios er et bibliotek som tillater lagring av data lokalt på mobilen uavhengig om appen er åpen eller ikke. Dette er nyttig for å oppdatere UI riktig på oppstart dersom de tracker rute. Lagre profil for å beholde ruter de har gått på før er også mulig.

Dagger Hilt:
Dagger Hilt er et bibliotek som lar oss bruke Dependency Injection. Det handler om å opprette instanser av klasser en gang og bruke dem flere steder. Dette kombinert med databasen er veldig nyttig med tanke på Context som Hilt fikser. Det var også anbefalt å bruke det i kombinasjon med Room ifølge dokumentasjonen til Android.

Worker: 
Worker biblioteket til Android Studios hjelper oss i å oppdatere dataen slik at brukeren får pålitelig data. Det lar oss lage et Worker objekt som man oppretter som kjører i bakgrunnen uansett hva. Her har vi valgt at hver syklus av Workeren skal være 15min for å gi brukeren så ny data som mulig. 
