package com.example.crud.modules

import com.example.crud.BuildConfig
import com.example.crud.network.APIInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        val interceptor = HttpLoggingInterceptor()

        val clientInterceptor = Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()
            val url = request.url.newBuilder().build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(clientInterceptor)
            .addInterceptor(interceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIInterface =  retrofit.create(APIInterface::class.java)

}