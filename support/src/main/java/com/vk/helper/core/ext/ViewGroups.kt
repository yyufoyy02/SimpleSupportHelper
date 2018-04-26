/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

// Aliases to other public API.

package com.vk.helper.core.ext

import android.support.annotation.Px
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewGroup

/** Returns `true` if [view] is found in this view group. */
inline operator fun ViewGroup.contains(view: View) = indexOfChild(view) != -1

/** Returns the number of views in this view group. */
inline val ViewGroup.size get() = childCount

/** Returns true if this view group contains no views. */
inline fun ViewGroup.isEmpty() = childCount == 0

/** Returns true if this view group contains one or more views. */
inline fun ViewGroup.isNotEmpty() = childCount != 0

/** Performs the given action on each view in this view group. */
inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}

/** Performs the given action on each view in this view group, providing its sequential index. */
inline fun ViewGroup.forEachIndexed(action: (index: Int, view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(index, getChildAt(index))
    }
}

/** Returns a [MutableIterator] over the views in this view group. */
operator fun ViewGroup.iterator() = object : MutableIterator<View> {
    private var index = 0
    override fun hasNext() = index < childCount
    override fun next() = getChildAt(index++) ?: throw IndexOutOfBoundsException()
    override fun remove() = removeViewAt(--index)
}

/** Returns a [Sequence] over the child views in this view group. */
val ViewGroup.children: Sequence<View>
    get() = object : Sequence<View> {
        override fun iterator() = this@children.iterator()
    }

/**
 * Sets the margins in the ViewGroup's MarginLayoutParams. This version of the method sets all axes
 * to the provided size.
 *
 * @see ViewGroup.MarginLayoutParams.setMargins
 */
inline fun ViewGroup.MarginLayoutParams.setMargins(@Px size: Int) {
    setMargins(size, size, size, size)
}

/**
 * Updates the margins in the [ViewGroup]'s [ViewGroup.MarginLayoutParams].
 * This version of the method allows using named parameters to just set one or more axes.
 *
 * @see ViewGroup.MarginLayoutParams.setMargins
 */
inline fun ViewGroup.MarginLayoutParams.updateMargins(
        @Px left: Int = leftMargin,
        @Px top: Int = topMargin,
        @Px right: Int = rightMargin,
        @Px bottom: Int = bottomMargin
) {
    setMargins(left, top, right, bottom)
}
