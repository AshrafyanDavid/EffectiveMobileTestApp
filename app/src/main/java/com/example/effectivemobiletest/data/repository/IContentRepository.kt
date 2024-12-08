package com.example.effectivemobiletest.data.repository

import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.domain.model.AirTicketModel
import com.example.effectivemobiletest.domain.model.TicketOffersModel
import kotlinx.coroutines.flow.Flow

interface IContentRepository {

    fun getAirTickets(): Flow<BaseResult<List<AirTicketModel>, Unit>>
    fun getTicketOffers(): Flow<BaseResult<List<TicketOffersModel>, Unit>>
}