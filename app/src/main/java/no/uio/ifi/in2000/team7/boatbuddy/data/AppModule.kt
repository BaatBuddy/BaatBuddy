package no.uio.ifi.in2000.team7.boatbuddy.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.team7.boatbuddy.data.setting.ProfileRepository
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
        .build()

    @Singleton
    @Provides
    fun fetchUserProfileDao(db: ProfileDatabase) = db.userDao()

    @Singleton
    @Provides
    fun fetchProfileRepository(db: ProfileDatabase): ProfileRepository {
        return ProfileRepository(db.userDao())
    }
}