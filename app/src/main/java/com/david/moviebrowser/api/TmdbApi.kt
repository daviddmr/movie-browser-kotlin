package com.david.moviebrowser.api

import com.david.moviebrowser.model.GenreResponse
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.model.RequestMoviesResponse
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
    ): Observable<RequestMoviesResponse>

    @GET("movie/top_rated")
    fun topRatedMovies(
            @Query("page") page: Long
    ): Observable<RequestMoviesResponse>

    @GET("search/movie")
    fun findMoviesByText(
            @Query("query") query: String,
            @Query("page") page: Long
    ): Observable<RequestMoviesResponse>

    @GET("movie/{id}")
    fun movie(
            @Path("id") id: Long
    ): Observable<Movie>
}
