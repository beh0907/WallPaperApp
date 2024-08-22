package com.skymilk.wallpaperapp.store.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.store.domain.model.Data
import com.skymilk.wallpaperapp.utils.Constants

class HomePagingSource(
    private val wallPaperApi: WallPaperApi
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            //키 값이 없다면 다시 첫페이지부터 (0이 시작일 수도 있다)
            val nextPage = params.key ?: Constants.FIRST_PAGE_INDEX
            val responseHome = wallPaperApi.getHomeWallPaper(nextPage)

            LoadResult.Page(
                data = responseHome.data,
                prevKey = null,
                nextKey = responseHome.paggination?.next?.page
            )


        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}