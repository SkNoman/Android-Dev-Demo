package com.example.crud.modules

import android.content.Context
import androidx.room.Room
import com.example.crud.local.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideMyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MyDatabase::class.java,
        "my_database"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDashboardMainMenuDao(db: MyDatabase) = db.dashboardDao()
}