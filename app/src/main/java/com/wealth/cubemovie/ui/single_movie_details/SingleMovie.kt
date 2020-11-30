package com.wealth.cubemovie.ui.single_movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.wealth.cubemovie.R
import com.wealth.cubemovie.data.api.MovieDBInterface
import com.wealth.cubemovie.data.api.POSTER_BASE_URL
import com.wealth.cubemovie.data.api.TheMovieDBClient
import com.wealth.cubemovie.data.pojo.CrewCredits
import com.wealth.cubemovie.data.pojo.MovieDetails
import com.wealth.cubemovie.data.repository.NetworkState
import com.wealth.cubemovie.ui.credits_crew.CrewCreditDetailsRepository
import com.wealth.cubemovie.ui.credits_crew.CrewCreditViewModel
import com.wealth.cubemovie.utils.Constant
import kotlinx.android.synthetic.main.single_movie_fragment.*
import java.text.NumberFormat
import java.util.*


class SingleMovie : Fragment() {

    companion object {
        fun newInstance() = SingleMovie()
    }

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    private lateinit var viewModelCrewCredits: CrewCreditViewModel
    private lateinit var creditDetailsRepository: CrewCreditDetailsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.single_movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //todo get movie id
        val movieId = Prefs.getString(Constant.MOVIE_ID, "0")

        val apiService: MovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        creditDetailsRepository =
            CrewCreditDetailsRepository(
                apiService
            )

        viewModel = getViewModel(movieId.toInt())
        viewModelCrewCredits = getViewModelCrewCredit(movieId.toInt())

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })
        viewModelCrewCredits.movieDetailsCrew.observe(viewLifecycleOwner, Observer {
            bindUICrew(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })

    }

    fun bindUICrew(it: CrewCredits) {
        try {
            crew_credits_txt.text =
                crew_credits_txt.text.toString() + "   " + it.character[0] + "   " + it.character[0] + "   " + it.character[0]
        } catch (ex: Exception) {
        }

    }

    fun bindUI(it: MovieDetails) {
        movie_title_txt.text = it.title
        movie_tagline_txt.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime_txt.text = it.runtime.toString() + " minutes"
        movie_overview_txt.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget_txt.text = formatCurrency.format(it.budget)
        movie_revenue_txt.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster_image);


    }


    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    private fun getViewModelCrewCredit(movieId: Int): CrewCreditViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CrewCreditViewModel(
                    creditDetailsRepository,
                    movieId
                ) as T
            }
        })[CrewCreditViewModel::class.java]
    }

}
