package com.example.effectivemobiletest.presentation.main.fragments.airtickets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletest.databinding.ItemAirTicketBinding
import com.example.effectivemobiletest.domain.model.AirTicketModel

class AirTicketsAdapter : ListAdapter<AirTicketModel, AirTicketsAdapter.AirTicketVH>(AirTicketDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirTicketVH {
        return AirTicketVH(ItemAirTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AirTicketVH, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class AirTicketVH(private val binding: ItemAirTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(airTicketModel: AirTicketModel) = with(binding) {
            nameTextView.text = airTicketModel.title
            countryTextView.text = airTicketModel.town
            priceTextView.text = airTicketModel.price
            singerImageView.setImageResource(airTicketModel.imageRes)
        }
    }

    class AirTicketDiffUtil : DiffUtil.ItemCallback<AirTicketModel>() {
        override fun areItemsTheSame(oldItem: AirTicketModel, newItem: AirTicketModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AirTicketModel, newItem: AirTicketModel): Boolean {
            return oldItem == newItem
        }
    }
}