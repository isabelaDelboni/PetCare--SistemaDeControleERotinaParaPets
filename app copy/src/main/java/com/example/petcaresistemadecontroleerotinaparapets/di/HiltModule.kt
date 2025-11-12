package com.example.petcaresistemadecontroleerotinaparapets.di

import android.content.Context
import androidx.room.Room
import com.example.petcaresistemadecontroleerotinaparapets.data.local.AppDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.UsuarioDao
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

    // --- Autenticação (Já existia) ---
    @Provides
    @Singleton
    fun provideFirebaseAuthService(): FirebaseAuthService {
        return FirebaseAuthService()
    }

    // --- Banco de Dados (Já existia) ---
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "petcare_db"
        )
            .fallbackToDestructiveMigration() // (Permite recriar o banco ao mudar a versão)
            .build()
    }

    // --- DAOs (Atualizado) ---
    @Provides
    @Singleton
    fun provideUsuarioDao(db: AppDatabase): UsuarioDao {
        return db.usuarioDao()
    }

    @Provides
    @Singleton
    fun provideEventoDao(db: AppDatabase): EventoDao {
        return db.eventoDao()
    }

    // ✅ ADICIONADO: Provider para o novo PetDao
    @Provides
    @Singleton
    fun providePetDao(db: AppDatabase): PetDao {
        return db.petDao()
    }

    // TODO: Adicionar providers para os Repositórios (embora o @Inject constructor já ajude)
}