package com.example.crud.utils
import com.google.gson.annotations.SerializedName

data class ErrorJson(
    @SerializedName("error")
    val error: Error,
    @SerializedName("result_code")
    val resultCode: Int,
    @SerializedName("time")
    val time: String
) {
    data class Error(
        @SerializedName("message")
        val message: String,
        @SerializedName("title")
        val title: String
    )
}