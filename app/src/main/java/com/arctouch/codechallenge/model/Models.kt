package com.arctouch.codechallenge.model

import android.os.Parcelable
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class GenreResponse(val genres: List<Genre>)

@Parcelize
data class Genre(val id: Int, val name: String) : Parcelable

data class UpcomingMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)

@Parcelize
data class Movie(
        val id: Int,
        val title: String?,
        val overview: String?,
        val genres: List<Genre>?,
        @Json(name = "genre_ids") val genreIds: List<Int>?,
        @Json(name = "poster_path") val posterPath: String?,
        @Json(name = "backdrop_path") val backdropPath: String?,
        @Json(name = "release_date") val releaseDate: String?) : Parcelable {

    fun getBackDropUrl(): String? {
        return backdropPath?.let { MovieImageUrlBuilder.buildBackdropUrl(it) }
    }

    fun getPosterUrl(): String? {
        return posterPath?.let { MovieImageUrlBuilder.buildPosterUrl(it) }
    }

    fun getGenresNameFormatted(): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }
}
