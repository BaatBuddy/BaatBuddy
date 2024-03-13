# Bruk av APIs

- OceanForecast API
- Sunrise API
- LocationForecast

## OceanForecast API

En instans av OceanForecast API'et består av et område (geometry) og områdets målinger (properties)
over tid (timeseries).

### Områder (Geometry)

Områdene har forskjellige typer (point, polygon, osv.), og har hver sine koordinater (longitude and
latitude). I dette API'et er det hovedsaklig områder som er av typen "point". Ettersom kvaliteten av
API'et ikke er perfekt, 1km+, vil den automatisk "snappe" seg til de nærmeste koordinatene den har
data på.<br>
Noen eksempler kan være:

1. `https://api.met.no/weatherapi/oceanforecast/2.0/complete?lat=60.10&lon=5`
2. `https://api.met.no/weatherapi/oceanforecast/2.0/complete?lat=55.20&lon=2`

Her vil 1. "snappe" til lon: 5.0012 og lat: 60.1023 og 2. til lon: 2.0297 og lat: 55.2005.<br>
Det er også påkrevd å ha med lat og lon i api-kallet. Hvor denne grensen er, er jeg uusikker på men
vises på kartet nedenfor:

![asd](https://docs.api.met.no/doc/assets/oceanforecast_area.png)<br>
Datamodellen finnes [her](https://docs.api.met.no/doc/oceanforecast/datamodel).

### Målinger (Propterties)

Målingene i en instans vil bestå av en meta som sier noe om hva slags type måleenheter som blir
brukt.
| NAVN | MÅLEENHET | BESKRIVELSE |
|--------|--------|-------|
| sea_surface_wave_height | meter (m) | Significant wave height defined as the average of the
highest one-third (33%) of waves (measured from trough to crest) |
| sea_surface_wave_from_direction | vinkel (degree) | Wave direction follows meteorological
convention. It is given as the direction the waves are coming from (0° is north, 90° east, etc.) |
| sea_water_speed | fart (m/s) | Speed of sea water (current) |
| sea_water_to_direction | vinkel (degree) | Sea water (current) direction follows oceanographic
convention. It is given as the direction the sea water is moving towards (0° is north, 90° east,
etc.) |
| sea_water_temperature | temperatur (celcius) | Surface temperature of sea water |

### Flowchart of the API

```mermaid
---
title: OceanForecast API
---
flowchart TB
    API((API-endpoint))


    API -->|String|type1[type]
    API --> geometry & properties

    geometry -->|String| type2[type]
    geometry --> coordinates
    
    coordinates -->|double| lat & lon

    properties --> meta
    properties -->|Array| timeseries...

    timeseries... -->|time| time
    timeseries... --> data

    data --> instant

    instant --> details

    details -->|double| d1[sea_surface_wave_from_direction]
    details -->|double| d2[sea_surface_wave_height]
    details -->|double| d3[sea_water_speed]
    details -->|double| d4[sea_water_temperature]
    details -->|double| d5[sea_water_to_direction]

    meta -->|time| updated_at
    meta --> units

    units -->|degrees| u1[sea_surface_wave_from_direction]
    units -->|m| u2[sea_surface_wave_height]
    units -->|m/s| u3[sea_water_speed]
    units -->|celsius| u4[sea_water_temperature]
    units -->|degrees| u5[sea_water_to_direction]
```

## Sunrise API

Sunrise
Sunrise 3.0 er en tjeneste som leverer data om sol og måne. Backenden er skrevet i Python, og
kildekoden er tilgjengelig på GitHub med fri lisens.

Tjenesten leverer henholdsvis opp- og nedgangstider, samt når det to legemene krysser meridian og
antimeridian. Nedenfor følger en beskrivelse av disse fenomenene.

### Oppgang/Nedgang for Solen

Oppgang for sola er det tidspunktet der det øverste punktet på soldisken krysser horisonten.

Tilsvarende for nedgang har vi tidspunktet der det øverste punktet soldisken igjen krysser
horisonten på vei ned. Den astronomiske horisonten er et imaginært plan som står 90° på den aksen
som peker rett opp fra observatøren, også kalt zenith.

Den visuelle solradien når den befinner seg ved horisonten er omtrent 0.266°. Ideelt sett kunne man
da sagt at soloppgang inntreffer når senteret til solskiven er 0.266° under horisonten. En svært
viktig faktor for når soloppgang visuelt intreffer er refraksjon grunnet Jordas atmosfære. Grunnet
jordas atmosfære vil lys fra objekter i verdensrommet avbøyes og deres visuelle posisjon vil avvike
fra deres faktiske posisjon. Nedenfor vises en illustrasjon av fenomenet.

![a](https://in2000.met.no/images/examples/atmosphere_refrac.png)

Selv om lysavbøyning grunnet atmosfærens refraksjon ikke har en konstant verdi, men varierer grunnet
forhold som blant varierende temperatur, har man kommet frem til en gjennomsnittsverdi på omtrent
0.566°. Legger man til den visuelle Solradien og atmosfærens refraksjon, har man har da kommet frem
til en definisjon der soloppgang inntreffer når Solen er 0.833° under horisonten. For videre
lesning, der vi har basert mye av vår informasjon, anbefales United States Naval Observatory sine
nettsider:

https://aa.usno.navy.mil/faq/RST_defs

For månen har vi en tilsvarende definisjon. Siden Månen er nærmere enn Solen så vil dens visuelle
størrelse variere i større grad der den beveger seg i sin elliptiske bane rundt jorden. Vi har
likevel valgt å bruke en gjennomsnittsverdi på 0.266° for månens visuelle radius. Tatt i betrakting
verdien for atmosfærens refraksjon har da månen og sola har samme definisjon på opp- og nedgang i
Sunrise 3.0.

### Flowchart of the API

```mermaid
---
title: Sunrise API
---
graph TD

API((API-endpoint))

API --> |String| type & geometry & when & properties

    geometry -->|String| type2[type]
    geometry --> |Array|coordinates
    coordinates --> |double|longitude & latitude

    when --> |Array|interval
    interval --> |String| start & slutt

    properties --> 


  A[Sun] -->|String| B(Sunrise)
  A -->|String| C(Sunset)
  A -->|String| D(Solarnoon)
  A -->|String| E(Solarmidnight)

  B --> |String| time1[time]
  B --> |double| azimuth1[azimuth]
  C --> |String| time2[time]
  C --> |double| azimuth2[azimuth]
  D --> |String| time3[time]
  D --> |double| disc_centre_elevation1[disc_centre_elevation]
  D --> |boolean| visible1[visible]

  E --> |String| time4[time]
  E --> |double| disc_centre_elevation2[disc_centre_elevation]
  E --> |boolean| visible2[visible]

```

## LocationForecast API

Kildene til LocationForecast er delt inn i tre separate regioner, vi skal kun bruke de nordiske
dataene.

Det finnes ikoner som hører til API'et [her](https://github.com/metno/weathericons/).
<br>

### Hvordan regne ut hva temperaturen føles som(Effektiv temperatur)?

Yr viser også hva temperaturen 'føles som'. Den følte temperaturen eller vindavkjølingsindeksen er
den temperaturen vi måtte hatt i vindstille forhold for å oppleve samme kulde som ved de
eksisterende vind- og temperaturforhold.

Varmeindeksen brukes bare når temperaturen er over 26°C og fuktigheten er over 40 %, ellers brukes
den faktiske temperaturen.
Vindkjølingsfaktoren brukes bare når temperaturen er under 10°C og vindhastigheten er over 1,33 m/s,
ellers brukes den faktiske temperaturen.
<br>

### Instansvariabler

| Navn                      | Enhet   | Beskrivelse                                             |
|---------------------------|---------|---------------------------------------------------------|
| air_pressure_at_sea_level | hPa     | Lufttrykk på havnivå                                    |
| air_temperature           | celsius | Lufttemperatur 2m over bakken                           |
| cloud_area_fraction       | %       | Total skydekke for alle høyder                          |
| relative_humidity         | %       | Relativ fuktighet 2m over bakken                        |
| wind_from_direction       | degrees | Retningen vinden kommer fra (0° er nord, 90° øst, osv.) |
| wind_speed                | m/s     | Vindhastighet 10m over bakken (10 min gjennomsnitt)     |






  

