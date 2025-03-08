package com.challenge.avriocgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.challenge.avriocgallery.navigation.AvriocNavHost
import com.challenge.avriocgallery.ui.theme.AvriocGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvriocGalleryTheme {
                AvriocNavHost()
            }
        }
    }
}