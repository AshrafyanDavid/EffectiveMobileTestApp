package com.example.effectivemobiletest.domain.model

data class TicketInformationModel(
    val id: Long,
    val badge: String?,
    val price: String,
    val providerName: String,
    val company: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val timeStartText: String,
    val timeEndText: String,
    val estimatedDurationText: String,
    val hasTransfer: Boolean,
    val hasVisaTransfer: Boolean,
    val luggageModel: TicketLuggageModel,
    val handLuggageModel: TicketHandLuggageModel,
    val isReturnable: Boolean,
    val isExchangable: Boolean
)

data class TicketLuggageModel(
    val hasLuggage: Boolean,
    val price: String?
)

data class TicketHandLuggageModel(
    val hasHandLuggage: Boolean,
    val size: String
)
