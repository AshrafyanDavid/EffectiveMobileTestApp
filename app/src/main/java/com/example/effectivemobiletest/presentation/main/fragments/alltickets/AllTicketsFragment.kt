package com.example.effectivemobiletest.presentation.main.fragments.alltickets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.effectivemobiletest.databinding.FragmentAllTicketsBinding
import com.example.effectivemobiletest.presentation.utils.extensions.serializable
import com.example.effectivemobiletest.shared.utils.PreferencesManager
import com.example.effectivemobiletest.shared.utils.extensions.launchViewLifecycleScope
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class AllTicketsFragment : Fragment() {

    companion object {
        private const val DESTINATION_PLACE = "destination_place"
        private const val SELECTED_DATE = "selected_date"

        fun getInstance(destinationPlaceName: String, selectedDate: LocalDate) = AllTicketsFragment().apply {
            arguments = Bundle().apply {
                putString(DESTINATION_PLACE, destinationPlaceName)
                putSerializable(SELECTED_DATE, selectedDate)
            }
        }
    }

    private var binding: FragmentAllTicketsBinding? = null
    private val allTicketsViewModel: AllTicketsViewModel by viewModel()

    private var allTicketsAdapter: AllTicketsAdapter? = null

    private val destinationPlaceName: String get() = requireArguments().getString(DESTINATION_PLACE) ?: throw NullPointerException("Destination place can't be null")
    private val selectedDate: LocalDate get() = requireArguments().serializable<LocalDate>(SELECTED_DATE) ?: throw NullPointerException("Selected date can't be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllTicketsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setCollectors()
        getCountriesText()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding?.backImageView?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupAdapter() {
        binding?.flightsRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AllTicketsAdapter().also { allTicketsAdapter = it }
        }
    }

    private fun setCollectors() {
        launchViewLifecycleScope {
            allTicketsViewModel.ticketsInformationStateFlow.collectLatest { tickets ->
                allTicketsAdapter?.submitList(tickets)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCountriesText() {
        val cachedText = PreferencesManager.fromCountryText
        binding?.apply {
            routeTextView.text = "$cachedText - $destinationPlaceName"
            val formatter = selectedDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))
            val formattedDate = formatter.format(formatter).replace(".", ",")
            routeInfoTextView.text = "${formattedDate} 1 пассажир"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        allTicketsAdapter = null
    }
}
