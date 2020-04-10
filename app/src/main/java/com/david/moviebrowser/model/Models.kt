package com.david.moviebrowser.model

import android.os.Parcelable
import com.david.moviebrowser.util.MovieImageUrlBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GenreResponse(val genres: List<Genre>)

@Parcelize
data class Genre(val id: Int, val name: String) : Parcelable

data class RequestMoviesResponse(
        val page: Int,
        val results: List<Movie>,
        @SerializedName("total_pages") val totalPages: Int,
        @SerializedName("total_results") val totalResults: Int
)

@Parcelize
data class Movie(
        val id: Int,
        val title: String?,
        val overview: String?,
        val genres: List<Genre>?,
        @SerializedName("original_language") val originalLanguage: String?,
        @SerializedName("original_title") val originalTitle: String?,
        @SerializedName("vote_average") val voteAverage: Double?,
        @SerializedName("genre_ids") val genreIds: List<Int>?,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("release_date") val releaseDate: String?) : Parcelable {

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
