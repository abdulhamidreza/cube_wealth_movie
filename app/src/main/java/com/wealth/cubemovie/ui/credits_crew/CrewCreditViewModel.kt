package com.wealth.cubemovie.ui.credits_crew

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wealth.cubemovie.data.pojo.CrewCredits
import com.wealth.cubemovie.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class CrewCreditViewModel(
    private val movieRepositoryCrew: CrewCreditDetailsRepository,
    movieId: Int
) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetailsCrew: LiveData<CrewCredits> by lazy {
        movieRepositoryCrew.fetchCrewCreditDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepositoryCrew.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}