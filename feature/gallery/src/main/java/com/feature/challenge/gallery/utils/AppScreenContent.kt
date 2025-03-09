package com.feature.challenge.gallery.utils

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.feature.challenge.gallery.LocalViewType
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.ViewType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreenContent(
    navigationTitle: String,
    isLoading: Boolean = false,
    isHomeScreen: Boolean = false,
    contentAlignment: Alignment = Alignment.TopStart,
    onBackClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val localViewType = LocalViewType.current
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = navigationTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        painterResource(if (isHomeScreen) R.drawable.ic_home else R.drawable.ic_arrow_back),
                        contentDescription = null,
                        modifier = Modifier.clickable(enabled = isHomeScreen.not(), onClick = onBackClick)
                    )
                },
                actions = {
                    val icon = when(localViewType.value) {
                        ViewType.Grid -> R.drawable.ic_list_view_type
                        ViewType.List -> R.drawable.ic_grid_view_type
                    }
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            localViewType.value = localViewType.value.toggle()
                        }
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            Crossfade(targetState = isLoading, label = "") {
                if (it) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.Center))
                } else {
                    content()
                }
            }
        }
    }
}