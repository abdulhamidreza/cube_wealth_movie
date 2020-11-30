package com.wealth.cubemovie.ui.credits_crew

import androidx.lifecycle.LiveData
import com.wealth.cubemovie.data.api.MovieDBInterface
import com.wealth.cubemovie.data.pojo.CrewCredits
import com.wealth.cubemovie.data.repository.CreditCrewDetailsNetworkDataSource
import com.wealth.cubemovie.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class CrewCreditDetailsRepository(private val apiService: MovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: CreditCrewDetailsNetworkDataSource

    fun fetchCrewCreditDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<CrewCredits> {

        movieDetailsNetworkDataSource =
            CreditCrewDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchCrewCreditDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }


}