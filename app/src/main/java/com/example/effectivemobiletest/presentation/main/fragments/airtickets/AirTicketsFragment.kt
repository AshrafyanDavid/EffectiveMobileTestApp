package com.example.effectivemobiletest.presentation.main.fragments.airtickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.effectivemobiletest.databinding.FragmentAirTicketsBinding
import com.example.effectivemobiletest.presentation.main.fragments.BottomSheetDialogSearch
import com.example.effectivemobiletest.presentation.utils.extensions.setOnDoneListener
import com.example.effectivemobiletest.shared.utils.PreferencesManager
import com.example.effectivemobiletest.shared.utils.extensions.launchViewLifecycleScope
import kotlinx.coroutines.flow.collectLatest
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirTicketsFragment : Fragment() {

    companion object {
        fun getInstance() = AirTicketsFragment()
    }

    private var binding: FragmentAirTicketsBinding? = null
    private val airTicketsViewModel: AirTicketsViewModel by viewModel()
    private var airTicketsAdapter: AirTicketsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAirTicketsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAirTicketsRecyclerView()
        loadCachedFromCountryText()
        setCollectors()
        setListeners()
        observeToKeyboardVisibilityEvent()
    }

    private fun setAirTicketsRecyclerView() {
        binding?.airTicketsRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AirTicketsAdapter().also { airTicketsAdapter = it }
        }
    }

    private fun setCollectors() {
        launchViewLifecycleScope {
            airTicketsViewModel.airTicketsStateFlow.collectLatest { tickets ->
                airTicketsAdapter?.submitList(tickets)
            }
        }
    }

    private fun observeToKeyboardVisibilityEvent() {
        KeyboardVisibilityEvent.setEventListener(requireActivity()) { isOpened ->
            if (!isOpened) {
                binding?.fromCountryEditText?.clearFocus()
            }
        }
    }

    private fun setListeners() {
        binding?.apply {
            toTextView.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialogSearch()
                BottomSheetDialogSearch().show(childFragmentManager, bottomSheetDialog.tag)
            }

            fromCountryEditText.doAfterTextChanged { text ->
                PreferencesManager.fromCountryText = text?.toString() ?: ""
            }

            fromCountryEditText.setOnDoneListener {
                fromCountryEditText.clearFocus()
            }
        }
    }

    private fun loadCachedFromCountryText() {
        val cachedText = PreferencesManager.fromCountryText
        if (cachedText.isNotBlank()) {
            binding?.fromCountryEditText?.setText(cachedText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        airTicketsAdapter = null
    }
}
