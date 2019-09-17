package com.livermor.plusminus.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.on(
    failure: (call: Call<T>, t: Throwable) -> Unit,
    response: (call: Call<T>, response: Response<T>) -> Unit
) {
    enqueue(
        object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                failure(call, t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                response(call, response)
            }
        })
}