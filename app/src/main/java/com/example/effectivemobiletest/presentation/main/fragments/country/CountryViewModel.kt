package com.example.effectivemobiletest.presentation.main.fragments.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.data.repository.IContentRepository
import com.example.effectivemobiletest.domain.model.TicketOffersModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel(private val contentRepository: IContentRepository) : ViewModel() {

    private val _ticketOffersStateFlow = MutableStateFlow<List<TicketOffersModel>?>(null)
    val ticketOffersStateFlow: StateFlow<List<TicketOffersModel>?> get() = _ticketOffersStateFlow

    init {
        getTicketOffers()
    }

    private fun getTicketOffers() {
        viewModelScope.launch(Dispatchers.IO) {
            contentRepository.getTicketOffers().collect { result ->
                when (result) {
                    is BaseResult.Success -> _ticketOffersStateFlow.emit(result.data)
                    is BaseResult.Error -> Unit
                    is BaseResult.ErrorData -> Unit
                    is BaseResult.NetworkError -> Unit
                }
            }
        }
    }
}