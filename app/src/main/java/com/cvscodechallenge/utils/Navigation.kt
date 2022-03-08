package com.cvscodechallenge.utils

enum class Navigation (val route: String) {
    SEARCH("search"),
    DETAILS("details/{pos}")
}