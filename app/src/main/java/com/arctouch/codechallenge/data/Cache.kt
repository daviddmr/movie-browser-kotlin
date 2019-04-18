package com.arctouch.codechallenge.data

import android.databinding.ObservableList
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    fun filterMoviesWithGenres(upcomingMoviesResponse: UpcomingMoviesResponse): List<Movie> {
        return upcomingMoviesResponse.results.map { movie ->
            movie.copy(genres = Cache.genres.filter { genre -> movie.genreIds?.contains(genre.id) == true })
        }
    }
}
