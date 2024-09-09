package com.skymilk.wallpaperapp.store.presentation.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.store.domain.usecase.WallPaperUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

//@AssistedInject를 사용한 땐 @HiltViewModel을 정의해선 안된다
@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModel @AssistedInject constructor(
    private val wallPaperUseCases: WallPaperUseCases,
    @Assisted private val category: String
) : ViewModel() {

    //동적인 초기 변수를 지정하기 위해 AssistedFactory 구현
    @AssistedFactory
    interface CategoryViewModelFactory {
        fun create(category: String): CategoryViewModel
    }

    companion object {
        fun provideFactory(
            factory: CategoryViewModelFactory,
            category: String
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(category) as T
                }
            }
        }
    }

    // 외부에서 접근 가능한 검색 결과 Flow
    val categoryWallPapers: Flow<PagingData<Hit>> =
        wallPaperUseCases.getSearchWallPapers(category).cachedIn(viewModelScope)
}