package com.example.effectivemobiletest.domain.model

data class AirTicketModel(
    val id: Long,
    val title: String,
    val town: String,
    val price: String,
    val imageRes: Int
)