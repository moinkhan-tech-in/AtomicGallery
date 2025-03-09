package com.feature.challenge.gallery.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.challenge.common.LocalThemeMode
import com.challenge.common.LocalViewType
import com.challenge.common.ThemeMode
import com.challenge.common.ThemeMode.Dark
import com.challenge.common.ThemeMode.Light
import com.challenge.common.ThemeMode.System
import com.challenge.common.ViewType
import com.feature.challenge.gallery.R

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

    Scaffold(
        topBar = {
            val localViewType = LocalViewType.current
            val uiMode = LocalThemeMode.current
            val context = LocalContext.current

            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = navigationTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(if (isHomeScreen) R.drawable.ic_home else R.drawable.ic_arrow_back),
                        contentDescription = null,
                        modifier = Modifier.clickable(enabled = isHomeScreen.not(), onClick = onBackClick)
                    )
                },
                actions = {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(uiMode.value.icon()),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            uiMode.value = uiMode.value.next()
                            context.showToast(uiMode.value.toString())
                        }
                    )
                    Spacer(Modifier.size(12.dp))
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(localViewType.value.icon()),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            localViewType.value = localViewType.value.toggle()
                            context.showToast(localViewType.value.toString())
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
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    content()
                }
            }
        }
    }
}

fun ThemeMode.icon() = when (this) {
    Light -> R.drawable.ic_light_mode
    Dark -> R.drawable.ic_dark_mode
    System -> R.drawable.ic_theme_auto
}

fun ViewType.icon() = when (this) {
    ViewType.Grid -> R.drawable.ic_list_view_type
    ViewType.List -> R.drawable.ic_grid_view_type
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}