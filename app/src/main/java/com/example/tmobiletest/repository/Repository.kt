package com.example.tmobiletest.repository

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCards(): Flow<UIState>
}