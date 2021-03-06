package az.zero.movietime.api

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.zero.movietime.data.Response
import az.zero.movietime.data.Show
import az.zero.movietime.utils.API_KEY
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.ShowType
import az.zero.movietime.utils.exhaustive

const val STARTING_PAGE_NUMBER = 1

class MoviePagingSource(
    private val showApi: ShowApi,
    private val methodToCall: MethodToCall,
    private val showType: ShowType,
    private val movieId: Int,
    private val searchQuery: String
) : PagingSource<Int, Show>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val position = params.key ?: STARTING_PAGE_NUMBER

        return try {
            val response: Response = when (methodToCall) {
                MethodToCall.GET_POPULAR -> showApi.getPopularShows(show, API_KEY, position)
                MethodToCall.TOP_RATED -> showApi.getTopRatedShows(show, API_KEY, position)
                MethodToCall.TRENDING -> showApi.getTrendingShows(show, API_KEY, position)
                MethodToCall.GET_SIMILAR -> showApi.getSimilarShows(
                    show,
                    movieId,
                    API_KEY,
                    position
                )
                MethodToCall.GET_RECOMMENDED -> {
                    showApi.getRecommendedShows(
                        show,
                        movieId,
                        API_KEY,
                        position
                    )
                }
                MethodToCall.UPCOMING -> showApi.getUpcomingMovies(API_KEY, position)
                MethodToCall.AIRING_TODAY -> showApi.getAiringTodayTV(API_KEY, position)
                MethodToCall.SEARCH_SHOW -> showApi.searchForShows(API_KEY, searchQuery, position)
            }.exhaustive

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_NUMBER) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? = state.anchorPosition

    private val show = if (showType == ShowType.TV) "tv" else "movie"

}