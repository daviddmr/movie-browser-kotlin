package com.arctouch.codechallenge.injection.module

import com.arctouch.codechallenge.BuildConfig
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.interceptor.DefaultRequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {

    companion object {
        const val API_KEY = "1f54bd990f1cdfb230adb312546d765d"
        const val DEFAULT_LANGUAGE = "pt-BR"
        const val DEFAULT_REGION = "BR"
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

    private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())

    init {
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(DefaultRequestInterceptor())
    }

    @Provides
    @Singleton
    fun provideTmdbApiService(): TmdbApi {
        return builder.client(httpClient.build()).build().create(TmdbApi::class.java)
    }

}
