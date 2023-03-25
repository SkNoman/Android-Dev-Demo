package com.example.crud.utils

import com.example.crud.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

object ApiSecurity {
    fun getRandomString(length: Int) : String {

        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')+('%')+('$')+('#')
        return (1..length)
            .map { allowedChars.shuffled().random() }
            .joinToString("")
    }


    fun  createSign( salt : String): String? {

        return  createMD5(BuildConfig.API_KEY+salt)

    }

     fun  createMD5(s: String): String? {


        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(s.toByteArray())).toString(16).padStart(32, '0')

    }
}