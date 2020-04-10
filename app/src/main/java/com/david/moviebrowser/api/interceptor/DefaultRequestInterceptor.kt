package com.david.moviebrowser.api.interceptor

import com.david.moviebrowser.App
import com.david.moviebrowser.R
import com.david.moviebrowser.injection.module.RetrofitModule
import okhttp3.Interceptor
import okhttp3.Response

class DefaultRequestInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", RetrofitModule.API_KEY)
                .addQueryParameter("language", App.res.getString(R.string.default_language))
                .addQueryParameter("region", App.res.getString(R.string.default_region))
                .build()

        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}