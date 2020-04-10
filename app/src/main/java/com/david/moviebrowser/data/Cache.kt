package com.david.moviebrowser.data

import com.david.moviebrowser.model.Genre
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.model.RequestMoviesResponse

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
