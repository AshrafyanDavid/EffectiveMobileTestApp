package com.example.effectivemobiletest.data.network.api

import com.example.effectivemobiletest.data.model.responsemodel.AirTicketsResponseModel
import com.example.effectivemobiletest.data.model.responsemodel.TicketOffersResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ContentApi {

    @GET("uc?id=1o1nX3uFISrG1gR-jr_03Qlu4_KEZWhav&export=download")
    suspend fun getAirTickets(): Response<AirTicketsResponseModel>

    @GET("uc?id=13WhZ5ahHBwMiHRXxWPq-bYlRVRwAujta&export=download")
    suspend fun getTicketOffers(): Response<TicketOffersResponseModel>
}