package com.wealth.cubemovie.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wealth.cubemovie.data.api.MovieDBInterface
import com.wealth.cubemovie.data.pojo.CrewCredits
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreditCrewDetailsNetworkDataSource(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedMovieDetailsResponse = MutableLiveData<CrewCredits>()
    val downloadedMovieResponse: LiveData<CrewCredits>
        get() = _downloadedMovieDetailsResponse

    fun fetchCrewCreditDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            compositeDisposable.add(
                apiService.getCreditsCrewCast(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message.toString())
                        }
                    )
            )

        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message.toString())
        }


    }
}