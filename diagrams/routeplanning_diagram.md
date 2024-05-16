Tekstlig beskrivelse av "Været for en rute"
-
Aktører: Bruker, Skippo, Metrologisk institutt
Prebetingelse: Bruker har ikke åpnet appen før. APIene sine servere er ikke nede.
Postbetingelse: Været for en rute generert vises på skjermen.

Hovedflyt: 
1. Bruker åpner app og starter onboarding.
2. Bruker lager profil.
3. Onboarding er ferdig.
4. Bruker legger inn minimum 2 punkter på kartet.
5. Bruker trykker generer rute når de er ferdig med å legge inn punkter.
6. Rute tegnes på kartet, kommer swipe-up med værdata og varsler for ruten de neste dagene.
7. Bruker trykker på weather cards.

Alternativ flyt:
6.1.1. Rute tegnes ikke på kartet grunnet dårlig plassert punkter.
6.1.2. Hopper til punkt 4.

6.2.1. Bruker har ikke aktivert internett.
6.2.2. Bruker aktiverer internett.
6.2.3. Hopper til punkt 4.



```mermaid
---
title: create route and see weather info
---
sequenceDiagram
    actor User
    participant BåtBuddy
    participant Skippo API
    participant MET API

    User->>BåtBuddy: opens app
    BåtBuddy ->> BåtBuddy: checks if first time
    BåtBuddy ->> BåtBuddy: starts onboarding squence
    User ->> BåtBuddy: creates user
    User ->> BåtBuddy: finishes onboarding segment

    BåtBuddy ->> MET API: sends get request to MET
    MET API -->> BåtBuddy: returns alert info

    BåtBuddy->>User: requests location

    User->>BåtBuddy: gives permission

    User ->> BåtBuddy: clicks on "create route" button

    BåtBuddy -->> BåtBuddy: enbales creating route

    loop until min 2 points or max 10 points 
        User ->> BåtBuddy: clicks on map
        BåtBuddy -->> BåtBuddy: adds points to the list
    end

    User ->> BåtBuddy: clicks on "generate route" button

    BåtBuddy ->> Skippo API: sends get request with list of points formatted  

    alt invalid points
        Skippo API -->> BåtBuddy: returns nothing
        BåtBuddy ->> User: snackbar display saying invalid route

        loop until user is satisfied or list is empty
            User ->> BåtBuddy: clicks undo button
            BåtBuddy -->> BåtBuddy: removes point from the list
        end

        loop until min 2 points or max 10 points 
            User ->> BåtBuddy: clicks on map
            BåtBuddy -->> BåtBuddy: adds points to the list
        end

        User ->> BåtBuddy: clicks on "generate route" button
        BåtBuddy ->> Skippo API: sends get request with list of points formatted 
    end

    alt invalid points
        Skippo API -->> BåtBuddy: returns nothing
        BåtBuddy ->> User: snackbar display saying no internett connection

        User ->> BåtBuddy: turns on internett

        loop until user is satisfied or list is empty
            User ->> BåtBuddy: clicks undo button
            BåtBuddy -->> BåtBuddy: removes point from the list
        end

        loop until min 2 points or max 10 points 
            User ->> BåtBuddy: clicks on map
            BåtBuddy -->> BåtBuddy: adds points to the list
        end

        User ->> BåtBuddy: clicks on "generate route" button
        BåtBuddy ->> Skippo API: sends get request with list of points formatted 
    end

    Skippo API -->> BåtBuddy: returns list of points representing a driveable route

    BåtBuddy -->> BåtBuddy: displays new route
    BåtBuddy -->> BåtBuddy: creates swipeup at the bottom of the screen

    BåtBuddy ->> MET API: sends get request for each point in a list
    MET API -->> BåtBuddy: returns weather data for ocean and air

    BåtBuddy --> BåtBuddy: displays general weather data for each day with scores

    User ->> BåtBuddy: clicks on one of the weather cards

    BåtBuddy --> BåtBuddy: displays weather data in detail on a new screen with scores

```