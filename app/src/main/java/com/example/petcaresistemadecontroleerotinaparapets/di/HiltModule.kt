package com.example.petcaresistemadecontroleerotinaparapets.di

import android.content.Context
import com.example.petcaresistemadecontroleerotinaparapets.data.local.AppDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.EventoRepository
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.PetRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    // --- FIREBASE ---

    // Ensina o Hilt a prover o Firebase Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Ensina o Hilt a prover o Firestore
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Ensina o Hilt a prover o seu Serviço de Autenticação
    @Provides
    @Singleton
    fun provideFirebaseAuthService(auth: FirebaseAuth): FirebaseAuthService {
        // Passamos o FirebaseAuth para o construtor (se ele precisar)
        // Se o construtor for vazio, basta: return FirebaseAuthService()
        return FirebaseAuthService()
    }

    // --- ROOM (BANCO DE DADOS LOCAL) ---

    // Ensina o Hilt a prover o Banco de Dados (AppDatabase)
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    // Ensina o Hilt a prover os DAOs (que vêm do AppDatabase)
    @Provides
    @Singleton
    fun providePetDao(db: AppDatabase): PetDao = db.petDao()

    @Provides
    @Singleton
    fun provideEventoDao(db: AppDatabase): EventoDao = db.eventoDao()

    // --- REPOSITÓRIOS ---

    // Ensina o Hilt a prover o PetRepository
    // O Hilt vê que ele precisa de PetDao e Firestore, e usa as funções acima
    @Provides
    @Singleton
    fun providePetRepository(petDao: PetDao, firestore: FirebaseFirestore): PetRepository {
        return PetRepository(petDao, firestore)
    }

    // Ensina o Hilt a prover o EventoRepository
    @Provides
    @Singleton
    fun provideEventoRepository(eventoDao: EventoDao, firestore: FirebaseFirestore): EventoRepository {
        return EventoRepository(eventoDao, firestore)
    }
}