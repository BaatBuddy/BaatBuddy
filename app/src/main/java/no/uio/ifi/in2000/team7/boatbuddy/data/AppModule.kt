package no.uio.ifi.in2000.team7.boatbuddy.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.autoroute.AutorouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.setting.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherCalculatorRepository
import no.uio.ifi.in2000.team7.boatbuddy.database.ProfileDatabase
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
    fun fetchProfileRepository(db: ProfileDatabase): ProfileRepository {
        return ProfileRepository(db.userDao(), db.boatDao())
    }

    @Singleton
    @Provides
    fun fetchAutorouteRepository(db: ProfileDatabase): AutorouteRepository {
        return AutorouteRepository()
    }

    @Singleton
    @Provides
    fun fetchLocationForecastRepository(): LocationForecastRepository {
        return LocationForecastRepository()
    }

    @Singleton
    @Provides
    fun fetchWeatherCalculatorRepository(): WeatherCalculatorRepository {
        return WeatherCalculatorRepository()
    }

    @Singleton
    @Provides
    fun fetchMapboxRepository(): MapboxRepository {
        return MapboxRepository()
    }

    @Singleton
    @Provides
    fun fetchOceanForecastRepository(): OceanForecastRepository {
        return OceanForecastRepository()
    }

    @Singleton
    @Provides
    fun fetchSunriseRepository(): SunriseRepository {
        return SunriseRepository()
    }

    @Singleton
    @Provides
    fun fetchMetAlertsRepository(): MetAlertsRepository {
        return MetAlertsRepository()
    }
}