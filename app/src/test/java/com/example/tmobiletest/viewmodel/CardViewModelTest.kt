package com.example.tmobiletest.viewmodel

import com.example.tmobiletest.repository.Repository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CardViewModelTest{

    // when given then

    private lateinit var subject: CardViewModel
    private lateinit var mockRepository: Repository

    @Before
    fun setup(){
        subject = CardViewModel(mockRepository)
    }

    @Test
    fun testCardsContainsData(){

    }
}