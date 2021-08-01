package br.com.mecury.movieapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mecury.movieapp.data.local.models.Movie
import br.com.mecury.movieapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class ListViewModel(private val repository: MoviesRepository) : ViewModel() {

    val movies: MutableLiveData<List<Movie>> = MutableLiveData()
    var moviesOrigin: List<Movie>? = arrayListOf()
    fun getPopularMovies() {
        viewModelScope.launch {
            repository.getMovies(success = {
                movies.postValue(it)
                moviesOrigin = it
            }, error = {

            })
        }
    }

    fun textSearch(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        if (s.isEmpty() || s.length < 2) {
            movies.value = moviesOrigin
            return
        }
        val filter =
            moviesOrigin?.filter { l -> l.title.toLowerCase().contains(s.toString().toLowerCase()) }
        movies.value = filter
    }
}
