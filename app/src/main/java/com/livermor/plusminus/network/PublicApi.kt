package com.livermor.plusminus.network

import com.livermor.plusminus.model.RequestRegister
import com.livermor.plusminus.model.ResponseBase
import com.livermor.plusminus.model.ResponseState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PublicApi {

    @GET("api/game/state")
    fun gameState(@Query("id") id: String): Call<ResponseState>

    @POST("api/register")
    fun register(@Body req: RequestRegister): Call<ResponseBase>
}
