package com.example.effectivemobiletest.presentation.main.fragments.airtickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.data.repository.IContentRepository
import com.example.effectivemobiletest.domain.model.AirTicketModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirTicketsViewModel(private val contentRepository: IContentRepository) : ViewModel() {

    private val _airTicketsStateFlow = MutableStateFlow<List<AirTicketModel>?>(null)
    val airTicketsStateFlow : StateFlow<List<AirTicketModel>?> get() = _airTicketsStateFlow

    init {
        getAirTickets()
    }

    private fun getAirTickets() {
        viewModelScope.launch(Dispatchers.IO) {
            contentRepository.getAirTickets().collect { result ->
                when (result) {
                    is BaseResult.Success -> _airTicketsStateFlow.emit(result.data)
                    is BaseResult.Error -> Unit
                    is BaseResult.ErrorData -> Unit
                    is BaseResult.NetworkError -> Unit
                }
            }
        }
    }
}