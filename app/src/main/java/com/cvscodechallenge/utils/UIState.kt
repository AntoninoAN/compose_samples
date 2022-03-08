package com.cvscodechallenge.utils

import com.cvscodechallenge.model.Item

sealed class UIState {
    data class SUCCESS(val response: List<Item>): UIState()
    data class LOADING(val isLoading: Boolean = true): UIState()
    data class ERROR(val errorMessage: String): UIState()
    data class PREVIOUS(val previousSearch: List<String>): UIState()
    object EMPTY: UIState()
}