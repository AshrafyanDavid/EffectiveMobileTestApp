package com.example.effectivemobiletest.data.repository

import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.data.network.NetworkHelper
import com.example.effectivemobiletest.data.network.api.GoogleApi
import com.example.effectivemobiletest.domain.mapper.TicketsInformationMapper
import com.example.effectivemobiletest.domain.model.TicketInformationModel
import kotlinx.coroutines.flow.Flow

class GoogleRepositoryImpl(
    private val networkHelper: NetworkHelper,
    private val googleApi: GoogleApi
) : IGoogleRepository {

    override fun getTicketsInformation(): Flow<BaseResult<List<TicketInformationModel>, Unit>> {
        return networkHelper.proceed(TicketsInformationMapper()) {
            googleApi.getTicketsInformation()
        }
    }
}