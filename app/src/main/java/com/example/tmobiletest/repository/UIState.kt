package com.example.tmobiletest.repository

import com.example.tmobiletest.model.PageResponse

sealed class UIState {
    data class Response(val data: PageResponse) : UIState()
    data class Error(val errorMessage: String) : UIState()
    data class Loading(val isLoading: Boolean = true) : UIState()
}
