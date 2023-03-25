package com.example.crud.model

data class GetBaseUrlResponse(
    val result: Result,
    val result_code: Int,
    val time: String
){
    data class Result(
        val base_url: String
    )
}
