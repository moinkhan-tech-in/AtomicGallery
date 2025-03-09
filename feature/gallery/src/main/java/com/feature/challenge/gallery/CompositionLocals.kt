package com.feature.challenge.gallery

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

val LocalViewType = compositionLocalOf { mutableStateOf(ViewType.Grid) }