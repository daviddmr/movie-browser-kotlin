package com.david.moviebrowser.repository

import com.david.moviebrowser.api.TmdbApi
import com.david.moviebrowser.api.response.Result
import com.david.moviebrowser.api.response.SafeResponse.safeApiCall
import com.david.moviebrowser.model.GenreResponse
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.model.RequestMoviesResponse
import javax.inject.Inject

class MovieRepository
@Inject
constructor(
        private val tmdbApi: TmdbApi
) {
    suspend fun getGenres(): Result<GenreResponse> {
        return safeApiCall(call = { tmdbApi.getGenres() })
    }

    suspend fun getUpcomingMovies(page: Long): Result<RequestMoviesResponse> {
        return safeApiCall(call = { tmdbApi.getUpcomingMovies(page) })
    }

    suspend fun getTopRatedMovies(page: Long): Result<RequestMoviesResponse> {
        return safeApiCall(call = { tmdbApi.getTopRatedMovies(page) })
    }

    suspend fun findMoviesByText(query: String, page: Long): Result<RequestMoviesResponse> {
        return safeApiCall(call = { tmdbApi.findMoviesByText(query, page) })
    }

    suspend fun getMovieDetail(id: Long): Result<Movie> {
        return safeApiCall(call = { tmdbApi.getMovieDetail(id) })
    }
}