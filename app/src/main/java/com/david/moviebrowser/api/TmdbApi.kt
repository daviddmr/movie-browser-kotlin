package com.david.moviebrowser.api

import com.david.moviebrowser.model.GenreResponse
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.model.RequestMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenreResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
            @Query("page") page: Long
    ): Response<RequestMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
            @Query("page") page: Long
    ): Response<RequestMoviesResponse>

    @GET("search/movie")
    suspend fun findMoviesByText(
            @Query("query") query: String,
            @Query("page") page: Long
    ): Response<RequestMoviesResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
            @Path("id") id: Long
    ): Response<Movie>
}
