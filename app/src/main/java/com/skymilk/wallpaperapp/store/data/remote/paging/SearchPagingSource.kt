package com.skymilk.wallpaperapp.store.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.util.Constants
import com.skymilk.wallpaperapp.util.Constants.PAGE_SIZE

class SearchPagingSource(
    private val wallPaperApi: WallPaperApi,
    private val searchQuery: String
) : PagingSource<Int, Hit>() {
    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        //키 값이 없다면 다시 첫페이지부터 (0이 시작일 수도 있다)
        val currentPage = params.key ?: Constants.FIRST_PAGE_INDEX

        return try {
            val response =
                wallPaperApi.getSearchWallPaper(page = currentPage, search = searchQuery)

            LoadResult.Page(
                data = response.hits,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (PAGE_SIZE == response.hits.size) currentPage + 1 else null,
            )


        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}