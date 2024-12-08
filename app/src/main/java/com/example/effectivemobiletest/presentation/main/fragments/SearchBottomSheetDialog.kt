package com.example.effectivemobiletest.presentation.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.databinding.BottomSheetDialogSearchBinding
import com.example.effectivemobiletest.presentation.main.fragments.country.CountryFragment
import com.example.effectivemobiletest.presentation.utils.extensions.setOnDoneListener
import com.example.effectivemobiletest.presentation.utils.openFragment
import com.example.effectivemobiletest.shared.utils.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class SearchBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        fun getInstance() = SearchBottomSheetDialog()
    }

    private var binding: BottomSheetDialogSearchBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    if (childFragmentManager.fragments.isNotEmpty()) {
                        childFragmentManager.popBackStack()
                        true
                    } else false
                } else false
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetDialogSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCachedText()
        deletingVisibility()
        setListeners()
        observeToKeyboardVisibilityEvent()

        binding?.destinationEditText?.setOnDoneListener {
            navigateToSearchedCountryFragment()
            binding?.destinationEditText?.clearFocus()
        }
    }

    private fun observeToKeyboardVisibilityEvent() {
        KeyboardVisibilityEvent.setEventListener(requireActivity()) { isOpened ->
            if (!isOpened) {
                binding?.destinationEditText?.clearFocus()
                navigateToSearchedCountryFragment()
            }
        }
    }

    private fun setListeners() {
        binding?.apply {
            routeImageView.setOnClickListener {
                dialog?.dismiss()
            }

            whereverImageView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Стамбул")
                navigateToSearchedCountryFragment()
            }
            vacationImageView.setOnClickListener {
                dialog?.dismiss()
            }
            ticketsImageView.setOnClickListener {
                dialog?.dismiss()
            }

            // Стамбул
            stanbulImageView.setOnClickListener {
                destinationEditText.setText("Стамбул")
                navigateToSearchedCountryFragment()
            }
            stanbulTextView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Стамбул")
                navigateToSearchedCountryFragment()
            }
            // Сочи
            sochiImageView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Сочи")
                navigateToSearchedCountryFragment()
            }
            sochiTextView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Сочи")
                navigateToSearchedCountryFragment()
            }
            // Пхукет
            pxuketImageView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Пхукет")
                navigateToSearchedCountryFragment()
            }
            pxuketTextView.setOnClickListener {
                destinationEditText.text = Editable.Factory.getInstance().newEditable("Пхукет")
                navigateToSearchedCountryFragment()
            }

            deleteImageView.setOnClickListener {
                destinationEditText.text?.clear()
            }

            destinationEditText.doAfterTextChanged { text ->
                deletingVisibility()
            }
        }
    }

    private fun deletingVisibility() {
        binding?.apply {
            deleteImageView.isInvisible = destinationEditText.text.isEmpty()
        }
    }

    private fun navigateToSearchedCountryFragment() {
        val destinationPlace: String = binding?.destinationEditText?.text?.toString()?.takeIf { it.isNotBlank() } ?: return
        val fragment = CountryFragment.getInstance(destinationPlace)
        childFragmentManager.openFragment(fragment, containerId = R.id.fragment_container)
    }

    private fun loadCachedText() {
        val cachedText = PreferencesManager.fromCountryText
        binding?.fromCountryTextView?.text = cachedText
    }

    private fun setBehavior() {
        (dialog as? BottomSheetDialog)?.apply {
            with(behavior) {
                state = BottomSheetBehavior.STATE_EXPANDED
                isDraggable = true
                isHideable = true
                skipCollapsed = true
            }

            val bottomSheet = (dialog as BottomSheetDialog?)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
        }
    }

    override fun onStart() {
        super.onStart()
        setBehavior()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}