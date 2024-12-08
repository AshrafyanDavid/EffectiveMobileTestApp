package com.example.effectivemobiletest.domain.mapper

import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.data.model.responsemodel.AirTicketsResponseModel
import com.example.effectivemobiletest.domain.model.AirTicketModel

class AirTicketsMapper : Mapper<AirTicketsResponseModel, List<AirTicketModel>> {

    override fun map(model: AirTicketsResponseModel?): List<AirTicketModel>? {
        val imageResources = arrayOf(R.drawable.ic_picture8, R.drawable.ic_picture7, R.drawable.ic_picture10)
        return model?.offers?.mapIndexed { index, responseModel ->
            AirTicketModel(
                id = responseModel.id,
                title = responseModel.title,
                town = responseModel.town,
                price = "${responseModel.priceModel.price} ₽",
                imageRes = imageResources[index]
            )
        }
    }
}