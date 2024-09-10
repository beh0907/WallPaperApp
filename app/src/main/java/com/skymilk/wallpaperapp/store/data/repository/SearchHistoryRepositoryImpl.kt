package com.skymilk.wallpaperapp.store.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.skymilk.wallpaperapp.store.domain.repository.SearchHistoryRepository
import com.skymilk.wallpaperapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SearchHistoryRepository {

    override suspend fun saveSearchHistory(history: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[PreferencesKeys.SEARCH_HISTORY_KEY]?.let {
                Json.decodeFromString<List<String>>(it)
            }?.toMutableList() ?: mutableListOf()

            // 중복 제거 후 추가 (최근 검색어가 맨 앞에 오도록)
            currentHistory.remove(history)

            //최대 20개까지 저장되도록 설정
            if (currentHistory.size >= 20) {
                currentHistory.removeAt(currentHistory.lastIndex)
            }

            //쿼리 정보를 목록 첫번째에 추가한다
            currentHistory.add(0, history)

            preferences[PreferencesKeys.SEARCH_HISTORY_KEY] = Json.encodeToString(currentHistory)
        }
    }

    override suspend fun deleteSearchHistory(history: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[PreferencesKeys.SEARCH_HISTORY_KEY]?.let {
                Json.decodeFromString<List<String>>(it)
            }?.toMutableList()

            //쿼리 정보를 목록에서 제거한다
            currentHistory?.remove(history)

            if (currentHistory != null) {
                preferences[PreferencesKeys.SEARCH_HISTORY_KEY] =
                    Json.encodeToString(currentHistory)
            }
        }
    }

    override fun getSearchHistory(): Flow<List<String>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.SEARCH_HISTORY_KEY]?.let {
                    Json.decodeFromString(it)
                } ?: emptyList()
            }
    }
}

//dataStore 설정
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.HISTORY_DATA_STORE)

object PreferencesKeys {
    val SEARCH_HISTORY_KEY = stringPreferencesKey(name = Constants.SEARCH_HISTORY_KEY)
}