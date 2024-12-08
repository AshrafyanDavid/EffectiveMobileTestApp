package com.example.effectivemobiletest.domain.mapper

import com.example.effectivemobiletest.data.model.responsemodel.TicketOffersResponseModel
import com.example.effectivemobiletest.domain.model.TicketOffersModel

class TicketOffersMapper : Mapper<TicketOffersResponseModel, List<TicketOffersModel>> {

    override fun map(model: TicketOffersResponseModel?): List<TicketOffersModel>? {
        return model?.ticketsOffers?.map { responseModel ->
            TicketOffersModel(
                id = responseModel.id,
                title = responseModel.title,
                timeRange = responseModel.timeRange.joinToString(separator = " "),
                price = "${responseModel.priceModel.price} ₽"
            )
        }
    }
}