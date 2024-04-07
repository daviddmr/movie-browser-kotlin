package com.david.moviebrowser.injection.module

import com.david.moviebrowser.BuildConfig
import com.david.moviebrowser.api.TmdbApi
import com.david.moviebrowser.api.interceptor.DefaultRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(
    ViewModelComponent::class
)
class RetrofitModule {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(DefaultRequestInterceptor())
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)
}
