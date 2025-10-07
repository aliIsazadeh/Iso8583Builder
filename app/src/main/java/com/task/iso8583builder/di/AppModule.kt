package com.task.iso8583builder.di

import com.task.iso8583builder.repository.IsoMessageRepository
import com.task.iso8583builder.repository.IsoMessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindIsoMessageRepository(
        impl : IsoMessageRepositoryImpl
    ) : IsoMessageRepository
}