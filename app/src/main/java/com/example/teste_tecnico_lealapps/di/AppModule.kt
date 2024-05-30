package com.example.teste_tecnico_lealapps.di

import com.example.teste_tecnico_lealapps.data.repository.AuthRepositoryImpl
import com.example.teste_tecnico_lealapps.data.repository.TrainingRepositoryImpl
import com.example.teste_tecnico_lealapps.domain.repository.AuthRepository
import com.example.teste_tecnico_lealapps.domain.repository.TrainingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun providesTrainingRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): TrainingRepository {
        return TrainingRepositoryImpl(firebaseAuth, firebaseFirestore, firebaseStorage)
    }

}