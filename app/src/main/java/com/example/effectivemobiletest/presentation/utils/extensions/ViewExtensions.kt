package com.example.effectivemobiletest.presentation.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun View.setMargins(start: Int = 0, top: Int = 0, end: Int = 0, bottom: Int = 0) {
    val layoutParams = this.layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.setMargins(start, top, end, bottom)
        this.layoutParams = layoutParams
    }
}

fun View.removeMargins() {
    val layoutParams = this.layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.setMargins(0, 0, 0, 0)
        this.layoutParams = layoutParams
    }
}

fun View.setSize(widthPx: Int, heightPx: Int) {
    val layoutParams = this.layoutParams
    layoutParams.width = widthPx
    layoutParams.height = heightPx
    this.layoutParams = layoutParams
}

fun View.setSizeFromDimens(@DimenRes widthResId: Int, @DimenRes heightResId: Int) {
    val widthPx = context.resources.getDimensionPixelSize(widthResId)
    val heightPx = context.resources.getDimensionPixelSize(heightResId)
    setSize(widthPx, heightPx)
}

fun View.getTopOnScreen(resultCallback: (Int) -> Unit) {
    this.post {
        val location = IntArray(2)
        getLocationOnScreen(location)
        resultCallback.invoke(location.first())
    }
}

fun View.getAbsoluteY(): Int {
    val locationOnScreen = IntArray(2)
    this.getLocationOnScreen(locationOnScreen)
    return locationOnScreen[1]
}

fun View.setHeight(newHeight: Int) {
    val layoutParams = this.layoutParams
    layoutParams.height = newHeight
    this.layoutParams = layoutParams
    this.requestLayout()
}

fun View.setForegroundDrawable(@DrawableRes drawableRes: Int) {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    this.foreground = drawable
}

fun View.setPaddings(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    this.setPadding(start ?: paddingLeft, top ?: paddingTop, end ?: paddingRight, bottom ?: paddingBottom)
}

fun View.setPaddingBottom(paddingBottom: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

/** TextView */
fun TextView.setLineThroughText() {
    val spannableString = SpannableString(text)
    spannableString.setSpan(StrikethroughSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannableString
}

fun TextView.setFont(@FontRes id: Int) {
    typeface = ResourcesCompat.getFont(this.context, id)
}

fun TextView.setDrawable(
    @DrawableRes drawableStart: Int = 0,
    @DrawableRes drawableEnd: Int = 0,
    @DrawableRes drawableTop: Int = 0,
    @DrawableRes drawableBottom: Int = 0
): Unit = setCompoundDrawablesWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom)

fun TextView.removeDrawables(): Unit = setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

inline fun TextView.setSelectedStateListener(crossinline selectedStateCallback: (isSelected: Boolean) -> Unit) {
    setOnClickListener {
        isSelected = !isSelected
        selectedStateCallback.invoke(isSelected)
    }
}

fun TextView.enableFadingEffect(fadingView: View) {
    this.post {
        this.ellipsize = null
        val textWidth = this.paint.measureText(this.text.toString())
        val textViewWidth = this.width
        if (textViewWidth >= textWidth)
            fadingView.isGone = true
        else {
            fadingView.isVisible = true
            this.ellipsize = TextUtils.TruncateAt.END
        }
        val width = this.width
        val paint = this.paint
        val originalText = this.text
        val truncatedText = TextUtils.ellipsize(originalText, paint, width.toFloat(), TextUtils.TruncateAt.END).toString()
        this.text = truncatedText.removeSuffix("…")

    }
}

/** EditText */
fun EditText.showSoftKeyboard() {
    if (requestFocus()) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText.focus() {
    requestFocus()
    setSelection(length())
    showSoftKeyboard()
}

fun EditText.setFocus(hasFocus: Boolean) {
    if (hasFocus) focus() else clearFocus()
}

fun EditText.setOnDoneListener(callback: () -> Unit) {
    setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.text(): String = text?.toString() ?: ""

fun EditText.disableZeroAtStart() {
    val filter = InputFilter { source, _, _, dest, _, _ ->
        if (dest.isEmpty() && source.startsWith("0")) {
            return@InputFilter ""
        }
        null
    }
    val currentFilters = filters
    val newFilters = currentFilters.toMutableList().apply {
        add(filter)
    }
    filters = newFilters.toTypedArray()
}

fun View.forWhileDisable(timeInMillis: Long) {
    CoroutineScope(Dispatchers.Main).launch {
        this@forWhileDisable.isEnabled = false
        delay(timeInMillis)
        this@forWhileDisable.isEnabled = true
    }
}

inline fun View.setSingleOnClickListener(timeInMillis: Long = 500, crossinline onClick: (View) -> Unit) {
    setOnClickListener {
        forWhileDisable(timeInMillis)
        onClick(it)
    }
}

fun EditText.isNotNullOrEmpty(): Boolean = text?.toString()?.isNotBlank() == true

private fun EditText.textChanges(): MutableSharedFlow<CharSequence> {
    val sharedFlow = MutableSharedFlow<CharSequence>(replay = 1)
    addTextChangedListener {
        sharedFlow.tryEmit(it ?: "")
    }
    return sharedFlow
}

/** ImageView */
fun ImageView.startVectorImageAnimation() {
    (this.drawable as AnimatedVectorDrawable).start()
}

@SuppressLint("ClickableViewAccessibility")
fun ImageView.setOnPressSelector(pressedColor: Int) {
    this.setOnTouchListener { _, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                this.setColorFilter(pressedColor, PorterDuff.Mode.SRC_IN)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.clearColorFilter()
            }
        }
        false
    }
}

fun ConstraintLayout.setHorizontalBias(viewId: Int, bias: Float) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(this)
    constraintSet.setHorizontalBias(viewId, bias)
    constraintSet.applyTo(this)
}

fun View.setHorizontalBias(constraintLayout: ConstraintLayout, bias: Float): Unit = constraintLayout.setHorizontalBias(this.id, bias)

fun View.isFullyVisible(): Boolean {
    val rect = Rect()
    val isVisible = this.getGlobalVisibleRect(rect)
    return isVisible && rect.height() == this.height && rect.width() == this.width
}

fun View.isPartiallyVisible(): Boolean {
    val rect = Rect()
    return this.getGlobalVisibleRect(rect)
}

fun ViewGroup.setAllViewsEnabled(enabled: Boolean) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.setAllViewsEnabled(enabled)
        }
        child.isEnabled = enabled
    }
    this.isEnabled = enabled
}

fun RecyclerView.scrollToPositionCentered(position: Int) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    val adapter = this.adapter ?: return
    val recyclerViewHeight = this.height

    // Calculate offset to center the item
    val centerOffset = recyclerViewHeight / 2

    // Post the scroll to ensure smooth behavior after layout pass
    this.post {
        val view = layoutManager.findViewByPosition(position)

        if (position >= adapter.itemCount - 1 || view == null) {
            // For the last item(s), ensure it is fully visible
            layoutManager.scrollToPositionWithOffset(position, 0)
        } else {
            // For other items, center them in the RecyclerView
            layoutManager.scrollToPositionWithOffset(position, centerOffset)
        }
    }
}
