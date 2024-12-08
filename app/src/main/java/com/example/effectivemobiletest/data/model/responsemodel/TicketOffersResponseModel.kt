package com.example.effectivemobiletest.data.model.responsemodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffersResponseModel(
    @SerialName("tickets_offers") val ticketsOffers: List<TicketOfferResponseModel>
)

@Serializable
data class TicketOfferResponseModel(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("time_range") val timeRange: List<String>,
    @SerialName("price") val priceModel: TicketOffersPriceResponseModel
)

@Serializable
data class TicketOffersPriceResponseModel(
    @SerialName("value") val price: Int
)