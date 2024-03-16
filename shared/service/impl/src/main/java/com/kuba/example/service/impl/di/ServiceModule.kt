package com.kuba.example.service.impl.di

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.impl.FailureProvider
import com.kuba.example.service.impl.FakeGithubService
import com.kuba.example.service.impl.data.api.GithubApi
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideGithubService(real: FakeGithubService) : GithubService

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
    }
}