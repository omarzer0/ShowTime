package az.zero.movietime.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import az.zero.movietime.api.MovieApi
import az.zero.movietime.api.MoviePagingSource
import az.zero.movietime.utils.MethodToCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {
    fun getPopularMovies() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(movieApi, MethodToCall.GET_POPULAR) }
    ).liveData

    fun getSimilarMovies(movieId: Int) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(movieApi, MethodToCall.GET_SIMILAR, movieId) }
    ).liveData

    fun getRecommendedMovies(movieId: Int) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(movieApi, MethodToCall.GET_RECOMMENDED, movieId) }
    ).liveData
}