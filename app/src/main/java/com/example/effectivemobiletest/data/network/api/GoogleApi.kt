package com.example.effectivemobiletest.data.network.api

import com.example.effectivemobiletest.data.model.responsemodel.base.TicketsInformationResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface GoogleApi {

    @GET("uc?export=download&id=1HogOsz4hWkRwco4kud3isZHFQLUAwNBA")
    suspend fun getTicketsInformation(): Response<TicketsInformationResponseModel>
}