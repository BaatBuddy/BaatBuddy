Mermaidkoden ligger i diagrams-mappen: <a href="https://github.uio.no/IN2000-V24/team-7/blob/marius-feature-document/diagrams/routeplanning_diagram.md">HER</a>


- Rikt bilde
<img width="1371" alt="Rikt bilde (1)" src="https://media.github.uio.no/user/8216/files/ca8d222f-e770-4712-a667-6b822b89fe9a">


- Arkitekturskisse
![image](https://media.github.uio.no/user/8216/files/22777704-ce7b-496d-aca8-5cee6b3ed6bf)


- Klassediagram i UML

- Use case diagram
<img width="1194" alt="Screenshot 2024-05-16 at 18 51 54" src="https://media.github.uio.no/user/8216/files/7a614ee6-8107-402d-aff8-1015ac4edda0">

- Use case diagram
<img width="1018" alt="Screenshot 2024-05-16 at 20 25 14" src="https://media.github.uio.no/user/8216/files/4b459f1b-6fce-4399-8692-acf1cd98441f">



- Sekvensdiagram
<img width="906" alt="Screenshot 2024-05-16 at 18 55 48" src="https://media.github.uio.no/user/8216/files/8807c840-0984-416a-9803-a61b8e172a2b">

<img width="395" alt="Screenshot 2024-05-16 at 18 56 33" src="https://media.github.uio.no/user/8216/files/7ac77b6f-ab6a-42e4-aacf-25e0fd3b4d20">


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

    BåtBuddy --> BåtBuddy: displays weather data in detail on a new screen with score
 ```

