package com.example.tmobiletest.repository

import com.example.tmobiletest.model.remote.CardApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(private val service: CardApi): Repository {

    override fun getCards(): Flow<UIState> {
        return flow {
            emit(UIState.Loading())

            val response = service.getCards()

            if (response == null) {
                emit(UIState.Loading(false))
                emit(UIState.Error("Server error"))
            }else{
                emit(UIState.Loading(false))
                emit(UIState.Response(response))
            }
        }
    }
}