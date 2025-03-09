package com.feature.challenge.gallery.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.common.ui.theme.AtomicGalleryTheme
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.utils.AppScreenContent

@Composable
fun MessageContent(
    @DrawableRes imageRes: Int,
    title: String,
    buttonText: String? = null,
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = title,
            textAlign = TextAlign.Center
        )
        buttonText?.let {
            Spacer(Modifier.size(8.dp))
            Button(onClick = onButtonClick) {
                Text(buttonText)
            }
        }
    }
}


@Preview(name = "Message Content Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Message Content Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MessageContentPreview() {
    AtomicGalleryTheme {
        AppScreenContent("Title", onBackClick = {}) {
            MessageContent(
                title = stringResource(R.string.title_atomic_gallery),
                buttonText = "Done",
                onButtonClick = {},
                imageRes = R.drawable.ic_empty
            )
        }
    }
}