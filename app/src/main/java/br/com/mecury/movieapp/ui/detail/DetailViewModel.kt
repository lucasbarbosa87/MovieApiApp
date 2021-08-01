package br.com.mecury.movieapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mecury.movieapp.data.local.models.Movie
import br.com.mecury.movieapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    val movie: MutableLiveData<Movie> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            loading.postValue(true)
            repository.getMovieDetail(movieId, success = {
                movie.postValue(it)
                loading.postValue(false)
            }, error = {
                loading.postValue(false)
            })
        }
    }

}