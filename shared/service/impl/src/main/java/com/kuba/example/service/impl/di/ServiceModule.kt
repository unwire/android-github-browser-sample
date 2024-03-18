package com.kuba.example.service.impl.di

import android.content.Context
import androidx.room.Room
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.impl.FailureProvider
import com.kuba.example.service.impl.RealGithubService
import com.kuba.example.service.impl.data.api.GithubApi
import com.kuba.example.service.impl.data.db.GithuberDatabase
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    @Binds
    fun provideGithubService(real: RealGithubService) : GithubService

    companion object {

        @Singleton
        @Provides
        fun provideGithubApi(): GithubApi {
            val logger = HttpLoggingInterceptor.Logger {
                Timber.tag("OkHttp")
                Timber.d(it)
            }
            val loggingInterceptor = HttpLoggingInterceptor(logger).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            return Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(
                        OkHttpClient.Builder()
                                .addInterceptor(loggingInterceptor)
                                .build()
                    )
                    .addCallAdapterFactory(ApiResultCallAdapterFactory)
                    .addConverterFactory(ApiResultConverterFactory)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(GithubApi::class.java)
        }

        @Singleton
        @Provides
        fun providerFailureProvider(): FailureProvider = object : FailureProvider { override val forceFailure: Boolean = false }

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): GithuberDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = GithuberDatabase::class.java,
                name = GithuberDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        fun provideRepositoryItemDao(database: GithuberDatabase) = database.repositoryItemDao()
    }
}