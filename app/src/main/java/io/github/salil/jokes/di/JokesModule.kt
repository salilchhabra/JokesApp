package io.github.salil.jokes.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.salil.jokes.BuildConfig
import io.github.salil.jokes.data.JokesDataSourceImpl
import io.github.salil.jokes.data.JokesRepository
import io.github.salil.jokes.domain.JokesDataSource
import io.github.salil.jokes.framework.JokesApi
import io.github.salil.jokes.framework.JokesRepositoryImpl
import io.github.salil.jokes.usecase.FetchRandomJokesUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object JokesModule {
    @Provides
    fun provideJokesRepository(api: JokesApi): JokesRepository {
        return JokesRepositoryImpl(api)
    }

    @Provides
    fun provideJokesDataSource(repository: JokesRepository): JokesDataSource {
        return JokesDataSourceImpl(repository)
    }

    @Provides
    fun provideFetchRandomJokesUseCase(dataSource: JokesDataSource): FetchRandomJokesUseCase {
        return FetchRandomJokesUseCase(dataSource)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            okHttpClient.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): JokesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://geek-jokes.sameerkumar.website/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(JokesApi::class.java)
    }
}