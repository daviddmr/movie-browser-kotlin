package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.RequestMoviesResponse

object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    fun filterMoviesWithGenres(requestMoviesResponse: RequestMoviesResponse): List<Movie> {
        return requestMoviesResponse.results.map { movie ->
            movie.copy(genres = genres.filter { genre -> movie.genreIds?.contains(genre.id) == true })
        }
    }
}
