package com.example.tmobiletest.repository

import com.example.tmobiletest.model.PageResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCards(): Flow<UIState>
}