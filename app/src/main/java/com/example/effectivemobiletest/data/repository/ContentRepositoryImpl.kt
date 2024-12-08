package com.example.effectivemobiletest.data.repository

import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.data.network.NetworkHelper
import com.example.effectivemobiletest.data.network.api.ContentApi
import com.example.effectivemobiletest.domain.mapper.AirTicketsMapper
import com.example.effectivemobiletest.domain.mapper.TicketOffersMapper
import com.example.effectivemobiletest.domain.model.AirTicketModel
import com.example.effectivemobiletest.domain.model.TicketOffersModel
import kotlinx.coroutines.flow.Flow

class ContentRepositoryImpl(
    private val networkHelper: NetworkHelper,
    private val contentApi: ContentApi
) : IContentRepository {

    override fun getAirTickets(): Flow<BaseResult<List<AirTicketModel>, Unit>> {
        return networkHelper.proceed(AirTicketsMapper()) {
            contentApi.getAirTickets()
        }
    }

    override fun getTicketOffers(): Flow<BaseResult<List<TicketOffersModel>, Unit>> {
        return networkHelper.proceed(TicketOffersMapper()) {
            contentApi.getTicketOffers()
        }
    }
}