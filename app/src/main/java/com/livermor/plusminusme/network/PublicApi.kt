package com.livermor.plusminusme.network

import com.livermor.plusminusme.model.RequestRegister
import com.livermor.plusminusme.model.ResponseBase
import com.livermor.plusminusme.model.ResponseState
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
