Tekstlig beskrivelse av "Været for posisjon"
-
Aktører: Bruker, Metrologisk institutt. <br>
Prebetingelse: Bruker har åpnet appen før, Bruker har gitt posisjon og notifikasjons tillatelser, Bruker har internett til alle tider. <br>
Postbetingelse: Bruker lagrer rute i databasen

Hovedflyt: 
1. Bruker åpner app.
2. Bruker starter sporing av rute
3. Bruker avslutter sporing av rute
4. Bruker lagrer ruten

```mermaid
---
title: save tracked route
---
sequenceDiagram
    actor User
    participant BåtBuddy
    participant MET API

    User ->> BåtBuddy: opens app
    
    BåtBuddy ->> MET API: sends get request to MET
    MET API -->> BåtBuddy: returns alert info

    User ->> BåtBuddy: clicks start tracking button in bottom bar
    BåtBuddy ->> User: displays start tracking dialog

    User ->> BåtBuddy: clicks start

    loop
        alt if inside alert area
        BåtBuddy ->> User: sends notification
        end
        BåtBuddy ->> MET API: get request every 15min
        MET API -->> BåtBuddy: sunrise and metalert data
    end

    User ->> BåtBuddy: clicks stop tracking button in bottom bar

    BåtBuddy ->> User: displays stop tracking dialog

    User ->> BåtBuddy: clicks finish

    BåtBuddy ->> User: displays saveroute screen

    User ->> BåtBuddy: inputs route info
    User ->> BåtBuddy: clicks save
    BåtBuddy -->> BåtBuddy: saves route in database with points and route info

```