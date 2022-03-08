package com.cvscodechallenge.utils

fun String.width(): String{
    val matches = this.split("width=")

    return if (matches.isNotEmpty())
        matches[1].split(" ")[0].replace("\"","")
    else ""
}

fun String.height(): String{


    val matches = this.split("height=")

    return if (matches.isNotEmpty())
        matches[1].split(" ")[0].replace("\"","")
    else ""
}