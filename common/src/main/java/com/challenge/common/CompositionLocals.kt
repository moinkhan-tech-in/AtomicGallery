package com.challenge.common

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

enum class ViewType {
    List,
    Grid;

    fun toggle(): ViewType {
        return when (this) {
            List -> Grid
            Grid -> List
        }
    }
}

enum class ThemeMode {
    Light,
    Dark,
    System;

    fun next(): ThemeMode {
        return when (this) {
            Light -> Dark
            Dark -> System
            System -> Light
        }
    }
}

val LocalViewType = compositionLocalOf { mutableStateOf(ViewType.Grid) }

val LocalThemeMode = compositionLocalOf { mutableStateOf(ThemeMode.System) }