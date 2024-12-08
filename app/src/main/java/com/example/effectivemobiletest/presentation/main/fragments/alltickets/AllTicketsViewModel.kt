package com.example.effectivemobiletest.presentation.main.fragments.alltickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.data.repository.IGoogleRepository
import com.example.effectivemobiletest.domain.model.TicketInformationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AllTicketsViewModel(private val googleRepository: IGoogleRepository) : ViewModel() {

    private val _ticketsInformationStateFlow = MutableStateFlow<List<TicketInformationModel>?>(null)
    val ticketsInformationStateFlow: StateFlow<List<TicketInformationModel>?> get() = _ticketsInformationStateFlow

    init {
        getTicketsInformation()
    }

    private fun getTicketsInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            googleRepository.getTicketsInformation().collect { result ->
                when (result) {
                    is BaseResult.Success -> _ticketsInformationStateFlow.emit(result.data)
                    is BaseResult.Error -> Unit
                    is BaseResult.ErrorData -> Unit
                    is BaseResult.NetworkError -> Unit
                }
            }
        }
    }
}