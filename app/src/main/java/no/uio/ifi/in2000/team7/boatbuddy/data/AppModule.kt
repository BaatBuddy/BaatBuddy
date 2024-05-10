package no.uio.ifi.in2000.team7.boatbuddy.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.team7.boatbuddy.data.autoroute.AutorouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.database.ProfileDatabase
import no.uio.ifi.in2000.team7.boatbuddy.data.location.userlocation.UserLocationRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastDataSource
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastDataSource
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.RouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherCalculatorRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun fetchDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, ProfileDatabase::class.java, "users")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun fetchUserProfileDao(db: ProfileDatabase) = db.userDao()

    @Singleton
    @Provides
    fun fetchRouteDao(db: ProfileDatabase) = db.routeDao()

    @Singleton
    @Provides
    fun fetchProfileRepository(
        db: ProfileDatabase,
        @ApplicationContext context: Context
    ): ProfileRepository =
        ProfileRepository(db.userDao(), db.boatDao(), context = context)

    @Singleton
    @Provides
    fun fetchAutorouteRepository(): AutorouteRepository = AutorouteRepository()

    @Singleton
    @Provides
    fun fetchLocationForecastRepository(): LocationForecastRepository = LocationForecastRepository()

    @Singleton
    @Provides
    fun fetchWeatherCalculatorRepository(db: ProfileDatabase): WeatherCalculatorRepository =
        WeatherCalculatorRepository(
            userDao = db.userDao(),
            oceanForecastRepository = OceanForecastRepository(),
            locationForecastRepository = LocationForecastRepository(),
        )

    @Singleton
    @Provides
    fun fetchMapboxRepository(): MapboxRepository = MapboxRepository()

    @Singleton
    @Provides
    fun fetchOceanForecastRepository(): OceanForecastRepository = OceanForecastRepository()

    @Singleton
    @Provides
    fun fetchSunriseRepository(): SunriseRepository = SunriseRepository()

    @Singleton
    @Provides
    fun fetchMetAlertsRepository(): MetAlertsRepository = MetAlertsRepository()

    @Singleton
    @Provides
    fun fetchRouteRepository(
        db: ProfileDatabase,
        @ApplicationContext context: Context
    ): RouteRepository = RouteRepository(
        context = context,
        routeDao = db.routeDao(),
        mapRepo = MapboxRepository()
    )

    @Singleton
    @Provides
    fun fetchUserLocationRepository(
        @ApplicationContext context: Context
    ): UserLocationRepository = UserLocationRepository(context = context)

}