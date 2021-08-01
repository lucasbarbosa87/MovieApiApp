package br.com.mecury.movieapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lucas.barbosa.desafioindra.databinding.FragmentDetailBinding
import br.com.mecury.movieapp.utils.getProgressBarIndeterminate
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setUp()
        return binding.root
    }

    private fun setUp() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getMovieDetail(movieId!!)

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.movieTitle.visibility = View.GONE
                binding.movieOverview.visibility = View.GONE
                binding.moviePoster.visibility = View.GONE
                binding.movieRuntime.visibility = View.GONE
                binding.movieGenres.visibility = View.GONE
                binding.loading.visibility = View.VISIBLE
                return@Observer
            }
            binding.movieTitle.visibility = View.VISIBLE
            binding.movieOverview.visibility = View.VISIBLE
            binding.moviePoster.visibility = View.VISIBLE
            binding.movieRuntime.visibility = View.VISIBLE
            binding.movieGenres.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE
            return@Observer

        })

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            binding.movieTitle.text = it.title
            binding.movieOverview.text = it.overview
            Picasso.get()
                .load("http://image.tmdb.org/t/p/w185/" + it.postPath)
                .placeholder(getProgressBarIndeterminate(requireContext())!!)
                .fit()
                .into(binding.moviePoster)
            binding.movieRuntime.text =
                StringBuilder().append("Tempo: ").append(it.runtime).append(" minutos")
            binding.movieGenres.text = StringBuilder().append("Genero: ").append(it.getGenres())
        })
    }
}