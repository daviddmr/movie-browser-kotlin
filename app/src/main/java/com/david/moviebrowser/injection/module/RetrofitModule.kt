package com.david.moviebrowser.injection.module

import com.david.moviebrowser.BuildConfig
import com.david.moviebrowser.api.TmdbApi
import com.david.moviebrowser.api.interceptor.DefaultRequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

    private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

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
