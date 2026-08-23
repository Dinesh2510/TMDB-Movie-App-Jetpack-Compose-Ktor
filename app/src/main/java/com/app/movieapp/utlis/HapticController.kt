/*
 * Copyright (c) 2026 Dinesh2510
 * File : HapticController.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-23 23:33
 * Last modified: 2026-08-23 23:33
 *
 * Author : Dinesh
 * GitHub : https://github.com/Dinesh2510
 * YouTube : https://www.youtube.com/@pixeldesigndeveloper
 * Website : https://pixeldev.in
 *
 * Copyright (c) 2026 Dinesh. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package com.app.movieapp.utlis
/*
 * Project     : TMDB Ktor
 * File        : HapticUtils.kt
 * Module      : com.app.movieapp.core.haptics
 *
 * Copyright © 2026 Dinesh — https://pixeldev.in
 * SPDX-License-Identifier: Apache-2.0
 */


import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat

/**
 * Every semantic haptic event used across the app, mapped 1:1 to
 * HapticFeedbackConstantsCompat. OS-version fallback is handled
 * automatically inside ViewCompat.performHapticFeedback — no manual
 * SDK_INT checks needed here.
 */
enum class AppHaptic {

    // ── Taps & clicks ─────────────────────────────────────────────
    Click,              // CONTEXT_CLICK — generic tap on a clickable item (card, chip, icon)
    LongPress,          // LONG_PRESS — long-press triggering a context menu / preview
    VirtualKey,         // VIRTUAL_KEY — press on a custom on-screen key/button
    VirtualKeyRelease,  // VIRTUAL_KEY_RELEASE — release of the above

    // ── Keyboard (search bar, RSVP form, chat input) ────────────────
    KeyboardTap,        // KEYBOARD_TAP — soft keyboard key press
    KeyboardRelease,    // KEYBOARD_RELEASE — soft keyboard key release

    // ── Text fields ──────────────────────────────────────────────
    TextHandleMove,     // TEXT_HANDLE_MOVE — text selection handle dragged

    // ── Ticks (sliders, ratings, clock-style pickers) ────────────
    ClockTick,          // CLOCK_TICK — single discrete tick (star rating step)
    SegmentTick,        // SEGMENT_TICK — moving between a small set of choices (tab, list item)
    SegmentFrequentTick,// SEGMENT_FREQUENT_TICK — high-density ticking (slider drag, minute picker)

    // ── Outcome signals ──────────────────────────────────────────
    Confirm,            // CONFIRM — success (added to watchlist, RSVP sent, download complete)
    Reject,             // REJECT — failure (validation error, blocked action, API error)

    // ── Toggles ───────────────────────────────────────────────────
    ToggleOn,           // TOGGLE_ON — favorite/bookmark/switch turned ON
    ToggleOff,          // TOGGLE_OFF — favorite/bookmark/switch turned OFF

    // ── Gestures (swipe, pull-to-refresh, drag-and-drop) ─────────
    GestureStart,       // GESTURE_START — swipe/drag gesture begins
    GestureEnd,         // GESTURE_END — swipe/drag gesture completes
    GestureThresholdActivate,   // crossed the "will trigger on release" threshold (pull-to-refresh armed)
    GestureThresholdDeactivate, // moved back under threshold (pull-to-refresh cancelled)
    DragStart,          // DRAG_START — drag-and-drop pick-up (reorder watchlist, reorder list items)

    // ── Explicit no-op ────────────────────────────────────────────
    None                // NO_HAPTICS — intentional skip, e.g. disabled state
}

private fun AppHaptic.toConstant(): Int = when (this) {
    AppHaptic.Click                     -> HapticFeedbackConstantsCompat.CONTEXT_CLICK
    AppHaptic.LongPress                 -> HapticFeedbackConstantsCompat.LONG_PRESS
    AppHaptic.VirtualKey                -> HapticFeedbackConstantsCompat.VIRTUAL_KEY
    AppHaptic.VirtualKeyRelease         -> HapticFeedbackConstantsCompat.VIRTUAL_KEY_RELEASE

    AppHaptic.KeyboardTap               -> HapticFeedbackConstantsCompat.KEYBOARD_TAP
    AppHaptic.KeyboardRelease           -> HapticFeedbackConstantsCompat.KEYBOARD_RELEASE

    AppHaptic.TextHandleMove            -> HapticFeedbackConstantsCompat.TEXT_HANDLE_MOVE

    AppHaptic.ClockTick                 -> HapticFeedbackConstantsCompat.CLOCK_TICK
    AppHaptic.SegmentTick               -> HapticFeedbackConstantsCompat.SEGMENT_TICK
    AppHaptic.SegmentFrequentTick       -> HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK

    AppHaptic.Confirm                   -> HapticFeedbackConstantsCompat.CONFIRM
    AppHaptic.Reject                    -> HapticFeedbackConstantsCompat.REJECT

    AppHaptic.ToggleOn                  -> HapticFeedbackConstantsCompat.TOGGLE_ON
    AppHaptic.ToggleOff                 -> HapticFeedbackConstantsCompat.TOGGLE_OFF

    AppHaptic.GestureStart              -> HapticFeedbackConstantsCompat.GESTURE_START
    AppHaptic.GestureEnd                -> HapticFeedbackConstantsCompat.GESTURE_END
    AppHaptic.GestureThresholdActivate  -> HapticFeedbackConstantsCompat.GESTURE_THRESHOLD_ACTIVATE
    AppHaptic.GestureThresholdDeactivate-> HapticFeedbackConstantsCompat.GESTURE_THRESHOLD_DEACTIVATE
    AppHaptic.DragStart                 -> HapticFeedbackConstantsCompat.DRAG_START

    AppHaptic.None                      -> HapticFeedbackConstantsCompat.NO_HAPTICS
}

/**
 * Single entry point for triggering haptics anywhere in Compose.
 * Uses ViewCompat so fallback behavior for older API levels is
 * handled automatically by AndroidX (see HapticFeedbackConstantsCompat
 * .getFeedbackConstantOrFallback internally).
 */
class HapticController(private val view: View) {
    fun trigger(type: AppHaptic) {
        if (type == AppHaptic.None) return
        ViewCompat.performHapticFeedback(view, type.toConstant())
    }

    /** Ignores the view's "haptics enabled" user setting — use sparingly. */
    fun triggerForced(type: AppHaptic) {
        if (type == AppHaptic.None) return
        ViewCompat.performHapticFeedback(
            view,
            type.toConstant(),
            HapticFeedbackConstantsCompat.FLAG_IGNORE_VIEW_SETTING
        )
    }
}

@Composable
@ReadOnlyComposable
fun rememberHapticController(): HapticController {
    val view = LocalView.current
    return HapticController(view)
}/*
val haptics = rememberHapticController()
haptics.trigger(AppHaptic.Confirm)

fun Modifier.hapticClickable(
    haptic: AppHaptic = AppHaptic.Click,
    onClick: () -> Unit
) = composed {
    val controller = rememberHapticController()
    this.clickable {
        controller.trigger(haptic)
        onClick()
    }
}*/