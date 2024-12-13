/*
Copyright 2024 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.foundation.ui.api.ui.icon

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.savvasdalkitsis.uhuruphotos.foundation.icons.api.R
import com.savvasdalkitsis.uhuruphotos.foundation.theme.api.PreviewAppTheme
import com.savvasdalkitsis.uhuruphotos.foundation.theme.api.themes.ThemeMode

@Composable
fun autoTint() = with(colorScheme) {
    rememberLottieDynamicProperties(
        *prop(primary, "primary"),
        *prop(onPrimary, "onPrimary"),
        *prop(primaryContainer, "primaryContainer"),
        *prop(onPrimaryContainer, "onPrimaryContainer"),
        *prop(inversePrimary, "inversePrimary"),
        *prop(secondary, "secondary"),
        *prop(onSecondary, "onSecondary"),
        *prop(secondaryContainer, "secondaryContainer"),
        *prop(onSecondaryContainer, "onSecondaryContainer"),
        *prop(tertiary, "tertiary"),
        *prop(onTertiary, "onTertiary"),
        *prop(tertiaryContainer, "tertiaryContainer"),
        *prop(onTertiaryContainer, "onTertiaryContainer"),
        *prop(background, "background"),
        *prop(onBackground, "onBackground"),
        *prop(surface, "surface"),
        *prop(onSurface, "onSurface"),
        *prop(surfaceVariant, "surfaceVariant"),
        *prop(onSurfaceVariant, "onSurfaceVariant"),
        *prop(surfaceTint, "surfaceTint"),
        *prop(inverseSurface, "inverseSurface"),
        *prop(inverseOnSurface, "inverseOnSurface"),
        *prop(error, "error"),
        *prop(onError, "onError"),
        *prop(errorContainer, "errorContainer"),
        *prop(onErrorContainer, "onErrorContainer"),
        *prop(outline, "outline"),
        *prop(outlineVariant, "outlineVariant"),
        *prop(scrim, "scrim"),
        *prop(surfaceBright, "surfaceBright"),
        *prop(surfaceDim, "surfaceDim"),
        *prop(surfaceContainer, "surfaceContainer"),
        *prop(surfaceContainerHigh, "surfaceContainerHigh"),
        *prop(surfaceContainerHighest, "surfaceContainerHighest"),
        *prop(surfaceContainerLow, "surfaceContainerLow"),
        *prop(surfaceContainerLowest, "surfaceContainerLowest"),
    )
}

@Composable
private fun prop(color: Color, key: String) = arrayOf(
    rememberLottieDynamicProperty(
        property = LottieProperty.COLOR,
        value = color.toArgb(),
        keyPath = arrayOf("**", key, "**")
    ),
    rememberLottieDynamicProperty(
        property = LottieProperty.GRADIENT_COLOR,
        value = arrayOf(color.toArgb(), color.toArgb()),
        keyPath = arrayOf("**", key, "**")
    ),
    rememberLottieDynamicProperty(
        property = LottieProperty.COLOR_FILTER,
        value = PorterDuffColorFilter(color.toArgb(), PorterDuff.Mode.SRC),
        keyPath = arrayOf("**", key, "**")
    ),
)

@Preview
@Composable
fun AutoTintPreview() {
    PreviewAppTheme {
        UhuruIcon(
            icon = R.raw.animation_syncing,
        )
    }
}

@Preview
@Composable
fun AutoTintPreviewDar() {
    PreviewAppTheme(themeMode = ThemeMode.DARK_MODE) {
        UhuruIcon(
            icon = R.raw.animation_syncing,
        )
    }
}