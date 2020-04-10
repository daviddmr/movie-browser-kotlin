package com.david.moviebrowser.util

import com.david.moviebrowser.injection.module.RetrofitModule

private const val POSTER_URL = "https://image.tmdb.org/t/p/w154"
private const val BACKDROP_URL = "https://image.tmdb.org/t/p/w780"

class MovieImageUrlBuilder {

    companion object {
        fun buildPosterUrl(posterPath: String): String {
            return "$POSTER_URL$posterPath?api_key=${RetrofitModule.API_KEY}"
        }

        fun buildBackdropUrl(backdropPath: String): String {
            return "$BACKDROP_URL$backdropPath?api_key=${RetrofitModule.API_KEY}"
        }
    }
}
