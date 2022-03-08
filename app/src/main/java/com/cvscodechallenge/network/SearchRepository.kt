package com.cvscodechallenge.network

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.cvscodechallenge.model.Item
import com.cvscodechallenge.utils.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

private const val TAG = "SearchRepository"

class SearchRepository(private val service: APIService, private val context: Context) {

    private val SP_SEARCH_HISTORY = "SearchRepository_SP_SEARCH_HISTORY"
    private val PREVIOUS_SEARCH = "SearchRepository_PREVIOUS_SEARCH"
    private val localCache = mutableListOf<Item>()

    suspend fun getSearch(tags: String): Flow<UIState> = flow {
        emit(UIState.LOADING())

        val response = service.getSearch(tags)
        Log.d(TAG, "getSearch: $response")
        if (response.isSuccessful) {
            response.body()?.let {
                localCache.clear()
                localCache.addAll(it.items)
                emit(UIState.SUCCESS(it.items))

            } ?: emit(UIState.ERROR(response.message()))
        } else {
            emit(UIState.ERROR(response.message()))
            emit(UIState.LOADING(false))
            Log.e(TAG, "getSearch: ${response.message()}")
        }
    }

    suspend fun getDetailsListItem(position: Int) = flow<UIState> {
        if (localCache.isNotEmpty()) {
            emit(UIState.LOADING())
            delay(500)
            emit(UIState.SUCCESS(listOf(localCache[position])))
        }
    }

    suspend fun insertNewTerm(newTerm: String) {
        context.getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE).edit {
            putStringSet(PREVIOUS_SEARCH,
                getSPSearchTerms()?.let { previousSearch ->
                    if (previousSearch.size < 6)
                        previousSearch.add(newTerm)
                    else {
                        previousSearch.remove(previousSearch.first())
                    }
                    previousSearch.toCollection(mutableSetOf())
                }
            )
        }
    }


    suspend fun getSearchTerms() = flow {
        emit(
            getSPSearchTerms()?.toList().toUIState()
        )
    }

    private fun getSPSearchTerms(): LinkedHashSet<String>? {
        return context.getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE)
            .getStringSet(PREVIOUS_SEARCH, null)
            ?.toCollection(LinkedHashSet<String>())
    }

    private fun List<String>?.toUIState(): UIState {
        return if (this == null) UIState.EMPTY
        else
            UIState.PREVIOUS(this)

    }
}