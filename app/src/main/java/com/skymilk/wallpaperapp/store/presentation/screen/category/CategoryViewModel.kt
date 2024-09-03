package com.skymilk.wallpaperapp.store.presentation.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

//@AssistedInject를 사용한 땐 @HiltViewModel을 정의해선 안된다
class CategoryViewModel @AssistedInject constructor(
    private val wallPaperRepository: WallPaperRepository,
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

    init {
        viewModelScope.launch {
            getCategoryWallPaper(category).collect {
                _categoryWallPapers.emit(it)
            }
        }
    }

    private val _categoryWallPapers = MutableSharedFlow<PagingData<Hit>>()
    var categoryWallPapers = _categoryWallPapers.asSharedFlow()

    private fun getCategoryWallPaper(category: String): Flow<PagingData<Hit>> =
        wallPaperRepository.getCategoryWallPaper(category).cachedIn(viewModelScope)
}