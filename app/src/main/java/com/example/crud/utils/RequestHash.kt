package com.example.crud.utils

object RequestHash {
    fun create(salt: String, sign: String) : String{
        return "$salt.$sign"
    }
}