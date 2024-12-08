package com.example.effectivemobiletest.shared.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

val Fragment?.nullableViewLifecycleOwner: LifecycleOwner?
    get() = try {
        this?.view?.let { viewLifecycleOwner }
    } catch (e: Exception) {
        null
    }

fun Fragment?.launchViewLifecycleScope(coroutineContext: CoroutineContext = Dispatchers.Main, block: suspend () -> Unit): Job? =
    nullableViewLifecycleOwner?.lifecycleScope?.launch(coroutineContext) { block.invoke() }

fun LifecycleOwner.launchWhenResumed(callback: () -> Unit) {
    lifecycleScope.launch { lifecycle.withResumed(callback) }
}

fun AppCompatActivity?.launchLifecycleScope(coroutineContext: CoroutineContext = Dispatchers.Main, block: suspend () -> Unit): Job? =
    this?.takeIf { !it.isFinishing }?.lifecycleScope?.launch(coroutineContext) { block.invoke() }
