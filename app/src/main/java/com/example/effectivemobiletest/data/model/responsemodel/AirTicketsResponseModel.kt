package com.example.effectivemobiletest.data.model.responsemodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirTicketsResponseModel(
    @SerialName("offers") val offers: List<AirOfferResponseModel>
)

@Serializable
data class AirOfferResponseModel(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("town") val town: String,
    @SerialName("price") val priceModel: PriceResponseModel
)

@Serializable
data class PriceResponseModel(
    @SerialName("value") val price: Int
)