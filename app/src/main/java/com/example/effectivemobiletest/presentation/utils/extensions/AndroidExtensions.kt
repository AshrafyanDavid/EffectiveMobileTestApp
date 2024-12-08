package com.example.effectivemobiletest.presentation.utils.extensions

import am.ggtaxi.main.shared.utils.extensions.color
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.effectivemobiletest.App
import com.example.effectivemobiletest.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Locale
import java.util.UUID

fun Fragment.setBackPressedCustomAction(autoRemove: Boolean = true, action: () -> Unit): OnBackPressedCallback {
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action.invoke()
            if (autoRemove) remove()
        }
    }
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    return onBackPressedCallback
}

fun AppCompatActivity.setBackPressedCustomAction(action: () -> Unit, autoRemove: Boolean = true): OnBackPressedCallback {
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action.invoke()
            if (autoRemove) remove()
        }
    }
    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    return onBackPressedCallback
}

fun Context?.hasFeatureFlash(): Boolean = this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) ?: false

fun Context.hasAvailableBrowser(): Boolean {
    val url = "https://www.google.com/"
    val webAddress = Uri.parse(url)
    val intentWeb = Intent(Intent.ACTION_VIEW, webAddress)
    return intentWeb.resolveActivity(packageManager) != null
}

fun Fragment?.hasFeatureFlash(): Boolean = this?.context?.hasFeatureFlash() ?: false

fun getCallStack(): List<String> = Thread.currentThread().stackTrace.mapNotNull { element ->
    if (element.className.contains("am.ggtaxi.main.app") && element.methodName != "getCallStack") {
        "\n${element.className.substringAfter("am.ggtaxi.main.app.")}.${element.methodName}"
    } else null
}

fun List<View>.makeVisible() {
    for (v in this) {
        v.isVisible = true
    }
}

fun List<View>.makeNotVisible() {
    for (v in this) {
        v.isVisible = false
    }
}

fun List<View>.makeGone() {
    for (v in this) {
        v.isGone = true
    }
}

fun Context.showAlertDialog(
    @StringRes title: Int? = null,
    @StringRes message: Int? = null,
    @StringRes positiveButtonText: Int? = null,
    @StringRes negativeButtonText: Int? = null,
    positiveButtonCallback: (DialogInterface) -> Unit = {},
    negativeButtonCallback: (DialogInterface) -> Unit = {}
): AlertDialog {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
        setCancelable(false)
        title?.let { setTitle(getString(it)) }
        message?.let { setMessage(getString(it)) }
        positiveButtonText?.let { setPositiveButton(getString(it)) { dialog, _ -> positiveButtonCallback.invoke(dialog) } }
        negativeButtonText?.let { setNegativeButton(getString(it)) { dialog, _ -> negativeButtonCallback.invoke(dialog) } }
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
    return alertDialog
}

/**hide keyboard*/
fun Activity.hideSoftKeyboard(): Boolean {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (currentFocus != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    } else {
        inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
    }
    return false
}

fun Fragment.hideSoftKeyboard(): Boolean {
    activity?.run {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            return true
        } else {
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }
        return false
    }
    return false
}

/** Parcelable */
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

/** Serializable */
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

/** Drawable */
fun Context.getDrawableFromRes(@DrawableRes res: Int): Drawable? = ResourcesCompat.getDrawable(resources, res, null)
fun Fragment.getDrawableFromRes(@DrawableRes res: Int): Drawable? = ResourcesCompat.getDrawable(resources, res, null)
fun View.getDrawableFromRes(@DrawableRes res: Int): Drawable? = ResourcesCompat.getDrawable(resources, res, null)
fun ViewBinding.getDrawableFromRes(@DrawableRes res: Int): Drawable? = ResourcesCompat.getDrawable(root.resources, res, null)

/** Toast */
fun Context?.showToast(message: Any?, duration: Int = Toast.LENGTH_SHORT) = CoroutineScope(Dispatchers.Main).launch {
    this@showToast?.let { Toast.makeText(it, message.toString(), duration).show() }
}

fun Fragment?.showToast(message: Any?, duration: Int = Toast.LENGTH_SHORT) = this?.context?.showToast(message, duration)
fun Activity?.showToast(message: Any?, duration: Int = Toast.LENGTH_SHORT) = (this as? Context?)?.showToast(message, duration)
fun showToast(message: Any?, duration: Int = Toast.LENGTH_SHORT) = App.applicationContext().showToast(message, duration)

/** Vibration */
fun Context.doVibrate() {
    val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= 31) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager //s21 , a52, redmi 12T, a54, poco f5 pro
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator //s8, poco x3, motorola
    }
    val vibrationEffect: VibrationEffect = if (Build.VERSION.SDK_INT >= 29) {
        if (vibrator.hasAmplitudeControl()) {
            VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)  //s21, a54, poco f5 pro, poco x3, motorola
        } else {
            val pattern = longArrayOf(0, 50) //a52, redmi 12T
            VibrationEffect.createWaveform(pattern, -1)
        }
    } else {
        VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE) //s8
    }
    vibrator.apply {
        cancel()
        vibrate(vibrationEffect)
    }
}

fun View?.doVibrate(): Unit? = this?.context?.doVibrate()

/** Log */
inline fun <reified T> T.findTopEnclosingClass(): Class<*>? {
    var currentClass: Class<*>? = T::class.java
    var topClass: Class<*>? = T::class.java

    while (currentClass?.enclosingClass != null) {
        topClass = currentClass.enclosingClass
        currentClass = currentClass.enclosingClass
    }
    return topClass
}

inline val <reified T> T.classSimpleName: String get() = findTopEnclosingClass()?.simpleName ?: "TAG"
inline fun <reified T> T.logV(message: Any?, tagSuffix: String = "") = Log.v(classSimpleName + tagSuffix, message.toString())
inline fun <reified T> T.logI(message: Any?, tagSuffix: String = "") = Log.i(classSimpleName + tagSuffix, message.toString())
inline fun <reified T> T.logW(message: Any?, tagSuffix: String = "") = Log.w(classSimpleName + tagSuffix, message.toString())
inline fun <reified T> T.logD(message: Any?, tagSuffix: String = "") = Log.d(classSimpleName + tagSuffix, message.toString())
inline fun <reified T> T.logE(message: Any?, tagSuffix: String = "") = Log.e(classSimpleName + tagSuffix, message.toString())

/** Density */
fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toSp() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
fun Float.toDp(): Float = this / Resources.getSystem().displayMetrics.density
fun Float.toPx(): Float = this * Resources.getSystem().displayMetrics.density
fun Float.toSp(): Float = this * Resources.getSystem().displayMetrics.scaledDensity
fun Int.getPixelFromDips(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.pxToSp(context: Context): Float {
    return this / context.resources.displayMetrics.scaledDensity
}

fun Int.toDp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
}

/** SnackBar */
fun Activity?.showIndefiniteSnackBar(view: View, message: String, actionText: String, actionCallBack: () -> Unit) {
    this?.apply {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(actionText) { actionCallBack.invoke() }
            setActionTextColor(color(R.color.white))
            show()
        }
    }
}

fun Fragment?.showIndefiniteSnackBar(view: View, message: String, actionText: String, actionCallBack: () -> Unit) {
    this?.activity?.showIndefiniteSnackBar(view, message, actionText, actionCallBack)
}

fun BottomSheetDialogFragment?.showIndefiniteSnackBar(message: String, actionText: String, actionCallBack: () -> Unit) {
    this?.activity?.showIndefiniteSnackBar(this.dialog?.window?.decorView ?: return, message, actionText, actionCallBack)
}

/** change language */
fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}

/** Unique Id */
fun getUniqueIntFromUUID(): Int {
    val uuid = UUID.randomUUID()
    return (uuid.mostSignificantBits xor uuid.leastSignificantBits).toInt()
}

fun getUniqueLongFromUUID(): Long {
    val uuid = UUID.randomUUID()
    return uuid.mostSignificantBits xor uuid.leastSignificantBits
}
