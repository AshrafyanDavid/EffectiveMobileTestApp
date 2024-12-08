package com.example.effectivemobiletest.data.repository

import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.domain.model.TicketInformationModel
import kotlinx.coroutines.flow.Flow

interface IGoogleRepository {

    fun getTicketsInformation(): Flow<BaseResult<List<TicketInformationModel>, Unit>>
}