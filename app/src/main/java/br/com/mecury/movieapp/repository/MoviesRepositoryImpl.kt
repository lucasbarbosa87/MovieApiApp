package br.com.mecury.movieapp.repository

import com.google.gson.Gson
import br.com.mecury.movieapp.data.local.GenreDao
import br.com.mecury.movieapp.data.local.MovieDao
import br.com.mecury.movieapp.data.local.models.Movie
import br.com.mecury.movieapp.data.network.ApiEndPoint
import br.com.mecury.movieapp.data.network.getRetrofitInstance
import br.com.mecury.movieapp.data.network.provideApi
import br.com.mecury.movieapp.utils.callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class MoviesRepositoryImpl(val movieDao: MovieDao, val genreDao: GenreDao) : MoviesRepository {

    private val client: ApiEndPoint = provideApi(getRetrofitInstance())
    val gson = Gson()

    override fun getMovies(
        success: (reponse: List<Movie>) -> Unit,
        error: (throwable: Throwable) -> Unit
    ) {
        client.getMoviesPopular(language = "").enqueue(
            callback(
                response = { response ->
                    val results: String = getResults(response?.body())
                    success(gson.fromJson<List<Movie>>(results, Movie.myType))
                },
                failure = { exception ->
                    exception?.let {
                        error(exception)
                    }
                }
            )
        )
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        success: (reponse: Movie) -> Unit,
        error: (throwable: Throwable) -> Unit
    ) {

        val movie = movieDao.selectMovie(movieId)

        if (movie != null) {
            val genres = genreDao.selectGenre(movieId)
            movie.genres = genres
            success(movie)
            return
        }

        client.getMovieDetail(movieId = movieId,language = "").enqueue(callback(
            response = { response ->
                val json = response?.body()?.string().toString()
                val movieClass = gson.fromJson(json, Movie::class.java)
                success(movieClass)
                CoroutineScope(Dispatchers.IO).launch {
                    movieDao.insertMovie(movieClass)
                    movieClass.genres.forEach {
                        it.movieId = movieClass.id
                    }
                    genreDao.insertGenre(movieClass.genres)
                }
            },
            failure = { exception ->
                exception?.let {
                    error(exception)
                }
            }
        ))
    }

    private fun getResults(body: ResponseBody?): String {
        val json = JSONObject(body!!.string())
        return json.get("results").toString()
    }

}