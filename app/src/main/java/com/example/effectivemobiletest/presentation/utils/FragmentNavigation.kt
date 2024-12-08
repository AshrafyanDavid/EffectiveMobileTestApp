package com.example.effectivemobiletest.presentation.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.presentation.main.MainActivity
import kotlinx.coroutines.launch

private const val IS_ADD_TO_BACKSTACK = true
private const val IS_REPLACE = false

enum class FragmentAnimationType {
    SLIDE_HORIZONTAL, SLIDE_VERTICAL, FADE, SLIDE_LEFT_TO_RIGHT
}

fun FragmentManager.openFragment(
    fragment: Fragment,
    containerId: Int = R.id.main_container,
    fragmentTag: String = fragment::class.java.simpleName,
    addToBackStack: Boolean = IS_ADD_TO_BACKSTACK,
    isReplace: Boolean = IS_REPLACE,
    animationType: FragmentAnimationType? = FragmentAnimationType.SLIDE_HORIZONTAL
) {
    commit {
        when (animationType) {
            FragmentAnimationType.SLIDE_HORIZONTAL -> {
                setCustomAnimations(
                    R.anim.slide_in_hor, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_hor
                )
            }

            FragmentAnimationType.SLIDE_LEFT_TO_RIGHT -> {
                setCustomAnimations(
                    R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_left
                )
            }

            FragmentAnimationType.SLIDE_VERTICAL -> {
                setCustomAnimations(
                    R.anim.slide_in_ver, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_ver
                )
            }

            FragmentAnimationType.FADE -> {
                setCustomAnimations(
                    R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out
                )
            }

            else -> {}
        }

        if (isReplace) {
            replace(containerId, fragment, fragmentTag)
        } else {
            add(containerId, fragment, fragmentTag)
        }

        if (addToBackStack) {
            addToBackStack(fragmentTag)
        }
    }
}

fun Fragment.openFragment(
    fragment: Fragment,
    idContainer: Int = R.id.main_container,
    tagFragment: String = fragment::class.java.simpleName,
    addToBackStack: Boolean = IS_ADD_TO_BACKSTACK,
    isReplace: Boolean = IS_REPLACE,
    animationType: FragmentAnimationType? = FragmentAnimationType.SLIDE_HORIZONTAL
) {
    (activity as? MainActivity)?.openFragment(
        fragment = fragment, idContainer = idContainer, animationType = animationType, tagFragment = tagFragment, addToBackStack = addToBackStack, isReplace = isReplace
    )
}

fun AppCompatActivity.openFragment(
    fragment: Fragment,
    idContainer: Int = R.id.main_container,
    tagFragment: String = fragment::class.java.simpleName,
    addToBackStack: Boolean = IS_ADD_TO_BACKSTACK,
    isReplace: Boolean = IS_REPLACE,
    animationType: FragmentAnimationType? = FragmentAnimationType.SLIDE_HORIZONTAL
) {
    supportFragmentManager.openFragment(
        fragment, idContainer, tagFragment, addToBackStack, isReplace, animationType
    )
}

fun Fragment.closeFragment(countFragment: Int = 1) {
    activity?.lifecycleScope?.launch {
        val fm = activity?.supportFragmentManager ?: return@launch
        repeat(countFragment) {
            if (fm.backStackEntryCount > 0) fm.popBackStack()
            else activity?.finish()
        }
    }
}

fun Fragment.closeAllBackStackFragments() {
    activity?.lifecycleScope?.launch {
        val fm = activity?.supportFragmentManager ?: return@launch
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

fun Fragment?.isLastFragment(): Boolean = this?.activity?.supportFragmentManager?.fragments?.lastOrNull()?.tag == this?.tag