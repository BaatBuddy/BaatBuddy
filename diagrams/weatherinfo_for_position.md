Tekstlig beskrivelse av "Været for posisjon"
-
Aktører: Bruker, Metrologisk institutt
Prebetingelse: Bruker har åpnet appen før, APIene er nede
Postbetingelse: Bruker får ikke værdata.

Hovedflyt: 
1. Bruker åpner app
2. Bruker trykker på 'været' knappen i bottombaren
3. Bruker får ikke værdata

```mermaid
---
title: see weather info without internett
---
sequenceDiagram
    actor User
    participant BåtBuddy
    participant MET API

    User ->> BåtBuddy: opens app
    
    BåtBuddy ->> MET API: sends get request to MET
    MET API -->> BåtBuddy: returns 400 Error

    BåtBuddy ->> User: displays no internett access snackbar
    User ->> BåtBuddy: clicks weather button in the bottombar

    BåtBuddy -->> BåtBuddy: change screen to weather screen

    BåtBuddy ->> User: displays no internett icon with description

```