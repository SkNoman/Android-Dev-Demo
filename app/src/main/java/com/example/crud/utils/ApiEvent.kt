package com.example.crud.utils

open class ApiEvent <out T> (private val content: T){
    var hasBenHandled= false
    private set
    fun getContentIfNotHandled(): T?{
        return if (hasBenHandled){
            null
        }else{
            hasBenHandled = true
            content
        }
    }

}