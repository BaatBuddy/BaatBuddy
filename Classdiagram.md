classDiagram

    class RouteMap {
      - Route route
      - String mapURL
    }
    class Datascore {
      - String date
      - Double score
    }
    
    class Geometry {
        - List<List<Double>> coordinates
      - String type
    }
    class Properties {
      - Double distance
      - Double hoursToGo
      - Int returnCode
      - String type
      - List<String> warnings
    }
    class TimeWeatherData {
        - Double lat
        - Double lon
        - String time
        - Double? waveHeight
        - Double? waterTemperature
        - Double? waterDirection
        - Double windSpeed
        - Double windSpeedOfGust
        - Double airTemperature
        - Double cloudAreaFraction
        - Double fogAreaFraction
        - Double relativeHumidity
        - Double precipitationAmount
        - String symbolCode
    }
    class WeatherPreferences {
        - Double waveHeight
        - Double windSpeed
        - Double airTemperature
        - Double cloudAreaFraction
        - Double? waterTemperature
        - Double? relativeHumidity
        - Double precipitationAmount
        - Double fogAreaFraction
    }
    class BoatProfile {
    - String boatname
    - String username
    - String safetyDepth
    - String safetyHeight
    - String boatSpeed
    - Boolean isSelected
    }
    class Route {
    - String username
    -String boatname
    -Int routeID
    -String routename
    -String routeDescription
    -List<Point> route
    -String start
    -String finish
    }
    class UserProfile{
    -String username
    -String name
    -Boolean isSelected
    -Boolean isTracking
    -WeatherPreferences preferencene
    }
    class HomeScreen
    class HomeScreenViewModel{
    - showBottomSheet Boolean
    - showBottomSheetInitialized Boolean
    - showExplanationCard Boolean
    - showWeatherAlertInfoCard FeatureData
    }
    class FeatureData{
    - start String
    - end String
    - awarenessResponse String
    - awarenessSeriousness String
    - eventAwarenessName String
    - description String
    - awareness_level String
    - awareness_type String
    - consequences String
    - certainty String
    - geographicDomain String
    - instruction String
    - riskMatrixColor String
    - severity String
    - type String
    - event String
    - affected_area List<List<List<List<Double>>>>
    }
    class InfoScreen
    class InfoScreenViewModel{
    - _infoScreenUIState MutableStateFlow(InfoScreenUIState())
    - infoScreenUIState StateFlow<InfoScreenUIState>    
    }
    class InfoScreenUIState{
    - selectedTab Int
    }

    class ProfileScreen
    class ProfileViewModel{
    - users List<UserProfile>
    - selectedUser UserProfile
    - username String
    - name String
    - boats List<BoatProfile>
    - selectedBoat BoatProfile
    - isSelectingBoat Boolean
    - selectedWeather WeatherPreferences
    - updateWeather Boolean
    }
    class RouteScreen
    class MainActivity
    class MainScreenUIState{
    - splashScreenReady Boolean
    - showDialog Dialog
    - selectedScreen Int
    - isTrackingUser Boolean
    - showBottomBar Boolean
    - showLocationDialog Boolean
    - showNotificationDialog Boolean
    - showNoUserDialog Boolean
    }
    class database

    class AutorouteRepository{
        dataSource AutorouteDataSource 
    }
    class AutorouteDataSource{
        + getAutoRouteData()
    }
    class AutorouteData {
      - Geometry geometry
      - Properties properties
      - List<int> wayPoints
    }

    class LocationForecastRepository{
        dataSource LocationForecastDataSource
    }
    class LocationForecastDataSource{
        + getLocationForecastData()
    }
    class MapboxRepository{
        - annotationRepository AnnotationRepository
        + autorouteRepository AutorouteRepository()
        - mapView MapView
        - context Context
    }
    class AnnotationRepository{
        - mapView MapView
        + metAlertsRepository MetAlertsRepository()
        + alertPolygons mutableListOf<AlertPolygon>()
    }
    class MetAlertsRepository{
        - dataSource MetAlertsDataSource
    }
    class MetAlertsDataSource{
        + getMetAlertsData()
    }
    class OceanForecastRepository{
        - dataSource OceanForecastDataSource
    }
    class OceanForecastDataSource{
        + getOceanForecastData()
    }
    class ProfileRepository{
        - userDao UserProfileDao
        - boatDao BoatProfileDao
        - context Context
    }
    class RouteRepository{
        - routeDao RouteDao
        - context Context
        - mapRepo MapboxRepository
    }
    class SunriseRepository{
        - dataSource SunriseDataSource
    }
    class SunriseDataSource{
        + getSunriseData()
    }
    class WeatherCalculatorRepository{
        - oceanForecastRepository OceanForecastRepository
        - locationForecastRepository LocationForecastRepository
    }

    HomeScreen <|-- HomeScreenViewModel
    HomeScreenViewModel <|-- FeatureData
    InfoScreen <|-- InfoScreenViewModel
    InfoScreenViewModel <|-- InfoScreenUIState
    ProfileScreen <|-- ProfileViewModel
    ProfileViewModel <|-- WeatherPreferences
    RouteMap <|-- Route
    

    database <|-- BoatProfile
    database <|-- UserProfile

    AutorouteRepository <|-- AutorouteDataSource
    AutorouteData <|-- Geometry
    AutorouteData <|-- Properties
    LocationForecastRepository <|-- LocationForecastDataSource
    MapboxRepository <|-- AnnotationRepository
    AnnotationRepository <|-- MetAlertsRepository
    MetAlertsRepository <|-- MetAlertsDataSource
    OceanForecastRepository <|-- OceanForecastDataSource
    SunriseRepository <|-- SunriseDataSource
    WeatherCalculatorRepository <|-- OceanForecastRepository
    WeatherCalculatorRepository <|-- LocationForecastRepository
    UserProfile <|-- WeatherPreferences
  
  
