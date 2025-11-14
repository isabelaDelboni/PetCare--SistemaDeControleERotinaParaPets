package com.example.petcaresistemadecontroleerotinaparapets.di

import android.content.Context
import androidx.room.Room
import com.example.petcaresistemadecontroleerotinaparapets.data.local.AppDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthService(): FirebaseAuthService {
        return FirebaseAuthService()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "petcare_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePetDao(appDatabase: AppDatabase): PetDao {
        return appDatabase.petDao()
    }

    @Provides
    fun provideEventoDao(appDatabase: AppDatabase): EventoDao {
        return appDatabase.eventoDao()
    }
}