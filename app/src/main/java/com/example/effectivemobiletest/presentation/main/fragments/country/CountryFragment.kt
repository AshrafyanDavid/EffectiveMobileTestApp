package com.example.effectivemobiletest.presentation.main.fragments.country

import am.ggtaxi.main.shared.utils.extensions.getColor
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.databinding.FragmentCountryBinding
import com.example.effectivemobiletest.domain.model.TicketOffersModel
import com.example.effectivemobiletest.presentation.main.fragments.alltickets.AllTicketsFragment
import com.example.effectivemobiletest.presentation.utils.extensions.setOnDoneListener
import com.example.effectivemobiletest.presentation.utils.openFragment
import com.example.effectivemobiletest.shared.utils.PreferencesManager
import com.example.effectivemobiletest.shared.utils.extensions.launchViewLifecycleScope
import kotlinx.coroutines.flow.collectLatest
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CountryFragment : Fragment() {

    companion object {
        private const val DESTINATION_PLACE = "destination_place"
        fun getInstance(destinationPlaceName: String) = CountryFragment().apply {
            arguments = Bundle().apply {
                putString(DESTINATION_PLACE, destinationPlaceName)
            }
        }
    }


    private var binding: FragmentCountryBinding? = null
    private val countryViewModel: CountryViewModel by viewModel()

    private var selectedDate: LocalDate? = null
    private val destinationPlaceName: String get() = requireArguments().getString(DESTINATION_PLACE) ?: throw NullPointerException("Destination place can't be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        getTexts()
        setTodayDate()
        setCollectors()
        setClickListeners()
        doneListeners()
        observeToKeyboardVisibilityEvent()
    }

    private fun setCollectors() {
        launchViewLifecycleScope {
            countryViewModel.ticketOffersStateFlow.collectLatest { tickets ->
                tickets?.let { setTicketOffers(tickets) }
            }
        }
    }

    private fun doneListeners() {
        binding?.apply {
            fromCountryEditText.setOnDoneListener {
                fromCountryEditText.clearFocus()
            }
            destinationEditText.setOnDoneListener {
                destinationEditText.clearFocus()
            }
        }
    }

    private fun observeToKeyboardVisibilityEvent() {
        KeyboardVisibilityEvent.setEventListener(requireActivity()) { isOpened ->
            if (!isOpened) {
                binding?.destinationEditText?.clearFocus()
            }
        }
    }

    private fun setTicketOffers(tickets: List<TicketOffersModel>) {
        binding?.apply {
            val ticket1 = tickets[0]
            val ticket2 = tickets[1]
            val ticket3 = tickets[2]

            redCompanyTextView.text = ticket1.title
            redPriceTextView.text = ticket1.price
            redFlightTime.text = ticket1.timeRange

            blueCompanyTextView.text = ticket2.title
            bluePriceTextView.text = ticket2.price
            blueFlightTime.text = ticket2.timeRange

            whiteCompanyTextView.text = ticket3.title
            whitePriceTextView.text = ticket3.price
            whiteFlightTime.text = ticket3.timeRange
        }
    }


    private fun getTexts() {
        binding?.apply {
            destinationEditText.setText(destinationPlaceName)
            loadCachedText()
        }
    }

    private fun setTodayDate() {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d MMM EE", Locale("ru"))
        val formattedDate = currentDate.format(formatter).replace(".", ",")
        setFormattedDate(binding?.selectedDateTextView, formattedDate)
        selectedDate = currentDate
    }

    private fun openDatePickerForReturnDate() {
        val currentDate = LocalDate.now()
        openDatePicker(currentDate) { selectedDate ->
            val formatter = DateTimeFormatter.ofPattern("d MMM EE", Locale("ru"))
            val selectedFormattedDate = selectedDate.format(formatter).replace(".", ",")
            setFormattedDate(binding?.returnDateTextView, selectedFormattedDate)
        }
    }

    private fun openDatePickerForSelectedDate() {
        openDatePicker(LocalDate.now()) { selectedDate ->
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("d MMM EE", Locale("ru"))).replace(".", ",")
            setFormattedDate(binding?.selectedDateTextView, formattedDate)
            this@CountryFragment.selectedDate = selectedDate
        }
    }

    private fun setFormattedDate(targetTextView: TextView?, formattedDate: String) {
        val parts = formattedDate.split(",")
        if (parts.size == 2) {
            val spannable = SpannableString(formattedDate)

            val startOfGray = parts[0].length
            spannable.setSpan(
                ForegroundColorSpan(getColor(R.color.grey_5)),
                startOfGray,
                formattedDate.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(Color.WHITE),
                0,
                startOfGray,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            targetTextView?.text = spannable
        }
    }


    private fun openDatePicker(date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
        val year = date.year
        val month = date.monthValue - 1
        val day = date.dayOfMonth

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                onDateSelected(selectedDate ?: return@DatePickerDialog)
            }, year, month, day
        ).show()
    }


    private fun setClickListeners() {
        binding?.apply {
            seeAllTicketsButton.setOnClickListener {
                val destinationPlace: String = destinationEditText.text?.toString()?.takeIf { it.isNotBlank() } ?: return@setOnClickListener
                val fragment = AllTicketsFragment.getInstance(destinationPlace, selectedDate ?: return@setOnClickListener)
                parentFragmentManager.openFragment(fragment, containerId = R.id.fragment_container)
            }

            selectedDateTextView.setOnClickListener {
                openDatePickerForSelectedDate()
            }

            returnDateTextView.setOnClickListener {
                openDatePickerForReturnDate()
            }
            backImageView.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun loadCachedText() {
        val cachedText = PreferencesManager.fromCountryText
        binding?.fromCountryEditText?.setText(cachedText)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        selectedDate = null
    }
}
