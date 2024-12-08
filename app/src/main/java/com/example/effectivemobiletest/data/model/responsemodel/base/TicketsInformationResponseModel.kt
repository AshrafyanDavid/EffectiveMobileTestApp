package com.example.effectivemobiletest.data.model.responsemodel.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketsInformationResponseModel(
    @SerialName("tickets") val tickets: List<TicketInformationResponseModel>
)

@Serializable
data class TicketInformationResponseModel(
    @SerialName("id") val id: Long,
    @SerialName("badge") val badge: String? = null,
    @SerialName("price") val priceModel: TicketInformationPriceResponseModel,
    @SerialName("provider_name") val providerName: String,
    @SerialName("company") val company: String,
    @SerialName("departure") val departureModel: TicketInformationDepartureResponseModel,
    @SerialName("arrival") val arrivalModel: TicketInformationArrivalResponseModel,
    @SerialName("has_transfer") val hasTransfer: Boolean,
    @SerialName("has_visa_transfer") val hasVisaTransfer: Boolean,
    @SerialName("luggage") val luggageModel: TicketInformationLuggageResponseModel,
    @SerialName("hand_luggage") val handLuggageModel: TicketInformationHandLuggageResponseModel,
    @SerialName("is_returnable") val isReturnable: Boolean,
    @SerialName("is_exchangable") val isExchangable: Boolean
)

@Serializable
data class TicketInformationPriceResponseModel(
    @SerialName("value") val price: Int
)

@Serializable
data class TicketInformationDepartureResponseModel(
    @SerialName("town") val town: String,
    @SerialName("date") val date: String,
    @SerialName("airport") val airport: String,
)

@Serializable
data class TicketInformationArrivalResponseModel(
    @SerialName("town") val town: String,
    @SerialName("date") val date: String,
    @SerialName("airport") val airport: String
)

@Serializable
data class TicketInformationLuggageResponseModel(
    @SerialName("has_luggage") val hasLuggage: Boolean,
    @SerialName("price") val priceModel: TicketInformationPriceResponseModel? = null
)

@Serializable
data class TicketInformationHandLuggageResponseModel(
    @SerialName("has_hand_luggage") val hasHandLuggage: Boolean,
    @SerialName("size") val size: String? = null
)