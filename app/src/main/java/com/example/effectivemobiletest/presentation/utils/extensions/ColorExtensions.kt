package am.ggtaxi.main.shared.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

fun Context.color(@ColorRes resColor: Int) = ContextCompat.getColor(this, resColor)
fun Context.colorStateList(@ColorRes resColor: Int) = ContextCompat.getColorStateList(this, resColor)

fun Fragment.getColor(@ColorRes resColor: Int) = requireContext().color(resColor)
fun Fragment.getColorStateList(@ColorRes resColor: Int) = requireContext().colorStateList(resColor)

fun View.getColor(@ColorRes resColor: Int) = context.color(resColor)
fun View.getColorStateList(@ColorRes resColor: Int) = context.colorStateList(resColor)

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

@ColorInt
fun Fragment.getColorFromAttr(@AttrRes attrColor: Int) = requireContext().getColorFromAttr(attrColor)

fun Context.getColorHex(@ColorRes resColor: Int): String = String.format("#%08X", 0xFFFFFFFF.toInt() and color(resColor))

/** View */
fun View.setBackgroundTint(@ColorRes resColor: Int) = background.setTint(context.color(resColor))
fun View.setBackgroundTintFromHex(colorHex: String) {
    val colorInt = Color.parseColor(colorHex)
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(colorInt))
}

/** ImageView */
fun ImageView.setTint(@ColorRes resColor: Int) = setColorFilter(context.color(resColor), PorterDuff.Mode.SRC_IN)

/** TextView */
fun TextView.setTextColorFromRes(@ColorRes colorRes: Int): Unit = setTextColor(context.color(colorRes))



