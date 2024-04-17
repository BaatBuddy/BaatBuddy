```mermaid
flowchart TD;

start((Start))

start --> permission

permission -->|no| explanationpopup
permission -->|yes| welcomepopup

explanationpopup -->|unselect| welcomepopup

welcomepopup --> homescreen

homescreen --> c[click];

c -->|map| homescreen

c -->|calculate button| swipeup

swipeup -->|swipup| weatherinfoswipeup


```
