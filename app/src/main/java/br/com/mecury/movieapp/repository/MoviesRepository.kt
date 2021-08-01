package br.com.mecury.movieapp.repository

import br.com.mecury.movieapp.data.local.models.Movie

interface MoviesRepository {

    fun getMovies(
        success: (reponse: List<Movie>) -> Unit,
        error: (throwable: Throwable) -> Unit
    )

    suspend fun getMovieDetail(
        movieId: Int,
        success: (reponse: Movie) -> Unit,
        error: (throwable: Throwable) -> Unit
    )
}