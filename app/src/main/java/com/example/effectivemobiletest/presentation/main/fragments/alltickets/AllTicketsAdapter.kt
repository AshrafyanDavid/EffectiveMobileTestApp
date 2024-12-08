package com.example.effectivemobiletest.presentation.main.fragments.alltickets

import am.ggtaxi.main.shared.utils.extensions.getColor
import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.databinding.ItemFlightsBinding
import com.example.effectivemobiletest.domain.model.TicketInformationModel

class AllTicketsAdapter : ListAdapter<TicketInformationModel, AllTicketsAdapter.TicketInformationVH>(TicketInformationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketInformationVH {
        return TicketInformationVH(ItemFlightsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TicketInformationVH, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class TicketInformationVH(private val binding: ItemFlightsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(ticketInformationModel: TicketInformationModel) = with(binding) {
            recommendationTextView.apply {
                text = ticketInformationModel.badge
                recommendationView.isGone = ticketInformationModel.badge.isNullOrBlank()
            }
            priceTextView.text = ticketInformationModel.price
            codeSendTextView.text = ticketInformationModel.departureAirport
            codeArrivalTextView.text = ticketInformationModel.arrivalAirport
            timeStartTextView.text = ticketInformationModel.timeStartText
            timeEndTextView.text = ticketInformationModel.timeEndText
            estimatedTimeTextView.text = ticketInformationModel.estimatedDurationText

            if (!ticketInformationModel.hasTransfer) makeSlashGray(ticketInformationModel.estimatedDurationText)
        }

        private fun makeSlashGray(text: String): Unit = with(binding) {
            val spannableString = SpannableString(text)
            val index = text.indexOf('/')
            spannableString.setSpan(
                ForegroundColorSpan(root.getColor(R.color.grey_5)),
                index,
                index + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            estimatedTimeTextView.text = spannableString
        }
    }

    class TicketInformationDiffUtil : DiffUtil.ItemCallback<TicketInformationModel>() {
        override fun areItemsTheSame(oldItem: TicketInformationModel, newItem: TicketInformationModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TicketInformationModel, newItem: TicketInformationModel): Boolean {
            return oldItem == newItem
        }
    }
}