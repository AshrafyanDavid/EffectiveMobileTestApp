package com.example.effectivemobiletest.domain.mapper

import com.example.effectivemobiletest.data.model.responsemodel.base.TicketsInformationResponseModel
import com.example.effectivemobiletest.domain.model.TicketHandLuggageModel
import com.example.effectivemobiletest.domain.model.TicketInformationModel
import com.example.effectivemobiletest.domain.model.TicketLuggageModel
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat

class TicketsInformationMapper : Mapper<TicketsInformationResponseModel, List<TicketInformationModel>> {

    override fun map(model: TicketsInformationResponseModel?): List<TicketInformationModel>? {
        val timeFormatter = DateTimeFormat.forPattern("HH:mm")
        return model?.tickets?.map { responseModel ->

            val startDateTime = DateTime.parse(responseModel.departureModel.date)
            val endDateTime = DateTime.parse(responseModel.arrivalModel.date)

            val durationInMinutes = Duration(startDateTime, endDateTime).standardMinutes.toInt()
            var estimatedDurationText = roundMinutesToHalfHour(durationInMinutes)
            if (!responseModel.hasTransfer) {
                estimatedDurationText += " / Без пересадок"
            }

            TicketInformationModel(
                id = responseModel.id,
                badge = responseModel.badge,
                price = "${responseModel.priceModel.price} ₽",
                providerName = responseModel.providerName,
                company = responseModel.company,
                departureAirport = responseModel.departureModel.airport,
                arrivalAirport = responseModel.arrivalModel.airport,
                timeStartText = timeFormatter.print(startDateTime),
                timeEndText = timeFormatter.print(endDateTime),
                estimatedDurationText = estimatedDurationText,
                hasTransfer = responseModel.hasTransfer,
                hasVisaTransfer = responseModel.hasVisaTransfer,
                luggageModel = TicketLuggageModel(
                    hasLuggage = responseModel.luggageModel.hasLuggage,
                    price = responseModel.luggageModel.priceModel?.price?.run { "$this ₽" }
                ),
                handLuggageModel = TicketHandLuggageModel(
                    hasHandLuggage = responseModel.handLuggageModel.hasHandLuggage,
                    size = responseModel.handLuggageModel.size ?: ""
                ),
                isReturnable = responseModel.isReturnable,
                isExchangable = responseModel.isExchangable
            )
        }
    }

    private fun roundMinutesToHalfHour(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        val fractionalHours = when {
            remainingMinutes < 15 -> 0.0
            remainingMinutes < 45 -> 0.5
            else -> 1.0
        }

        val totalHours = hours + fractionalHours
        return "${totalHours.toString().removeSuffix(".0")}ч в пути"
    }

}
