package com.arctouch.codechallenge.api

import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("genre/movie/list")
    fun genres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Observable<GenreResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
            @Query("page") page: Long
    ): Observable<UpcomingMoviesResponse>

    @GET("movie/top_rated")
    fun topRatedMovies(
            @Query("page") page: Long
    ): Observable<UpcomingMoviesResponse>

    @GET("movie/{id}")
    fun movie(
            @Path("id") id: Long
    ): Observable<Movie>
}
